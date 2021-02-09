2.7 문서를 작성하는 대신 테스트를 만들자

이상적인 코드는 스스로 설명하기 때문에 어떤 추가 문서도 필요 없다. 나도 여기 동의하고 있고, 문서화 또는 주석을 다는 것을 싫어한다.
(사실은 이 핑계로 게으름을 부리는 것이다.)

    Employee jeff = department.employee("Jeff");
    jeff.giveRaise(new Cash("$5000"));
    if (jeff.performance() < 3.5) {
        jeff.fire();
    }

이와같이 코드 자체만으로 의미가 명확하게 전달된다면 문서화가 필요 없다.
하지만

    class Helper {
        int saveAndCheck(float x) { .. }
        float extract(String text) { .. }
        boolean convert(int value, boolean extra) { .. }
    }

위 소스는 메서드 이름도 형편없고 클래스 이름도 엉망이며, 클래스 전반적인 설계도 형편없다.
(자세히 살펴보면 현재 짜는 코딩 중에 위와같은 형태가 많다. 반성..)
좋은 클래스는 목적이 명확하고, 작고, 설계가 우하해야한다.
    
    class webPage {
        String content() { .. }
        void update(String content) { .. }
    }

따라서 코드를 문서화 하는 대신 코드를 깔끔하게 만들어야 한다.
깔금하게 만드는 것에는 테스트 코드가 포함된다.

    class CashTest {
        @Test
        public void summarizes() {
        assertThat(
        new Cash("$5").plus(new Cash("$3")),
        equalTo(new Cash("$8"))
        );
        }
        @Test
        public void deducts() {
        assertThat(
        new Cash("$7").plus(new Cash("-$11")),
        equalTo(new Cash("-$4"))
        );
        }
        @Test
        public void multiplies() {
        assertThat(
        new Cash("$2").mul(3),
        equalTo(new Cash("$6"))
        );
        }
    }
    
2.8 모의 객체(Mock) 대신 페이크 객체(Fake)를 사용하자

    class Cash {
        private final Exchange exchange;
        private final int cents;
        public Cash(Exchange exch, int cnts) {
            this.exchange = exch;
            this.cents = cnts;
        }
        public Cash in(String currency) {
            return new Cash(
                this.exchange;
                this.cents * this.exchange.rate(
                "USD", currency
                )
            );
        }
    }
    
    Cash dollar = new Cash(new NYSE("secret"), 100);
    Cash euro = dollar.in("EUR");

여기서 NYSE 서버가 개입하지 않은 상황에서 Cash 클래스를 테스트하고 싶다면

    Exchange exchange = Mockito.mock(Exchange.class);
    Mockito.doReturn(1.15)
        .when(exchange)
        .rate("USD", "EUR")
       Cash dollar = new Cash(exchange, 500);
       Cash euro = dollar.in("EUR");
       assert "5.75".equals(euro.toString));

위와 같이 모의 객체를 사용 할 수 있다.
하지만, 페이크 객체를 사용한다면

    interface Exchange {
        float rate(String origin, String target);
    
        final class Fake implements Exchange {
        @Override
        float rate(String origin, String target) {
        return 1.2345;
        }
      }
    
    }

중첩된 fake class는 인터페이스의 일부이며 인터페이스와 함께 지공된다.
이 페이크 클래스는 단위 테스트 안에서 Exchange를 쉽게 사용 할 수 있도록 지원한다.

모킹 대신 페이크 클래스를 사용한 단위테스트는
Exchange exchange new Exchange.Fake();
Cash dollar -new Cash(exchange, 500);
Cash euro = dollar.in("EUR")l
aserrt "6.17".equals(euro.toString());

이와같이 단위테스트를 짧게 만들 수 있다. 이는 유지보수성의 향상으로 이어진다.

단위테스트는 실패해선 안된다.
실제 클래스를 변경하였을때, 모킹으로 테스트할 땐 놓치기 쉽지만
Fake를 사용한다면 변경하면서 자연스레 Fake 클래스도 구현하므로 단위테스트가 실패할 일이 없다
(현재 단위테스트에 갖고 있는 불만 중 하나가, 모든 개발이 완료 된후 유지보수로 수정하며 단위테스트를 놓칠때가 많고, 소스 변화로 많은 테스트들이
실패하고 이에 대한 불만을 갖게 된다. Fake 클래스는 아주 좋은 방법같다)
    
    
2.9 인터페이스를 짧게 유지하고 스마트(smart)를 사용하자

    interface Exchange {
        float rate(String target);
        float rate(String source, String target);
    }

이 인터페이스는 섹션의 주제를 설명하는데 충분할지 몰라도, 너무 많은 것을 요구하기 때문에 설계 관점에서는 형편없는 인터페이스다.(이정도가??)
이 인터페이스는 응집도가 낮은 클래스를 만들게 한다.
이에대한 해결로

    interface Exchange {
        float rate(String source, String target);
        final class smart {
            private final Exchange origin;
            public float toUsd(String source) {
                return this.origin.rate(source, "USD");
            }
        }
    }

    float rate = new Exchange.Smart(new NYSE())
        .toUsd("EUR");


이제 smart 클래스에 추가하며 Exchange 인터페이스는 작고, 높은 응집도를 유지할 수 있게 되었다.

기본적으로 인터페이스를 짧게 만들고 '스마트'클래스를 인터페이스와 함께 배포함으로써 공통 기능을 추출하고 코드 중복을 피할 수 있게 되었다.
이 접근 방법은 데코레이터와 매우 유사하다.
데코레이터 예제로하면

    interface Exchange {
        float rate(String source, String target);
        final class Fast implements Exchage {
            @Override
            public float rate(String source, String target) {
            final float rate;
            if (source.equals(target)) {
                rate = 1.0f;
            } else {
                rate = this.origin.rate(source, target);
            }
            return rate;
            }
            public float toUsd(String source) {
                return this.origin.rate(source, "USD");
            }
        }
    }

중첩 클래스인 Exchange.Fast는 데코레이터인 동시에 '스마트'클래스다.
1. Exchange.Fast 는 rate() 메서드를 오버라이드 해서 더 강력하게 만든다.
이 클래스는 source와 target이 동일한 통화를 가리킬 경우 네트워크 호출을 피할 수 있도록 한다.
2. Exchange.Fast는 새로운 메서드인 toUsd()를 추가해서 USD로 쉽게 환율을 변환할 수 있도록 하였다.
    