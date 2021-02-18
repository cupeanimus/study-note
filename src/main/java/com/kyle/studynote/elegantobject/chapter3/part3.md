3.5 절대 getter와 setter를 사용하지 말자

3.5.1 객체 대 자료구조
객체와 자료구조의 차이점은 무엇이고, 자료구조가 OOP에서 해로운 이유는 무엇일까?
우선 객체와 자료구조의 차이점을 설명하자면,

    C
    struct Cash {
        int dollars;
    }
    
    printf("Cash value is %d", cash.dollars;
    
    C++
    #include <string>
    class Cash {
    public:
        Cash(int v): dollars(v) {};
        std:string print() const;
        private:
            int dollars;
        };    
    
    printf("Cash value is %s", cash.print());

차이점을 보면 struct의 경우 멤버인 dollars에 직접 접근한 후 해당 값을 정수로 취급하고, struct를 가지고는 어떤 일도 하지 않는다.

클래스는 다르다. 클래스는 어떤 식으로든 멤버에게 접근하는 것을 허용하지 않는다. 게다가 자신의 멤버를 노출하지도 않는다.
print()가 실제로 어떤 방식으로 동작하는 지도 알 수 없고, 캡슐화된 어떤 멤버가 이 작업에 개입하는 지도 알 수 없다. 이것이 바로 캡슐화이고 OOP가 지향하는 가장 중요한 설계 원칙 중 하나이다.
자료구조는 투명하지만 객체는 불투명하다.
자료구조는 글래스박스이지만 객체는 블랙박스이다.
자료구조는 수동적이지만 객체는 능동적이다.
자료구조는 죽어어있지만 객체는 살아있다.

(이러한 차이점들로) 자료구조보다 객체가 더 좋다고 생각한다.
어째서 객체와 자료구조를 동시에 가질 수 없을까? (객체만 사용하는 것이 더 좋기 때문)

절차적 프로그래밍이든, 함수형 프로그래밍이든, 객체지향 프로그래밍이든 모든 프로그래밍 스타일의 핵심 목표는 가시성의 범위를 축소해서 사물을 단순화시키는 것이다.
특정한 시점에 이해해야 하는 범위가 작을수록, 소프트웨어의 유지보수성이 향상되고 이해하고 수정하기도 쉬워진다.

3.2.1에서 절차적인 프로그래밍과 객체지향 프로그래밍 사이의 차이점을 살펴보면서 절차적인 세상보다 더 단순한 세상을 창조하기 위해 OOP가 발명된 것을 알 수 있었다.
OOP에서는 코드가 데이터를 지배하지 않는다. 대신 필요한 시점에 객체가 자신의 코드를 실행시킨다.
OOP에서 바이트보다 더 복잡한 요소를 사용할 때마다, 우리는 절차적인 프로그래밍을 향해 퇴보한다. 바이트들을 자료구조로 묶어 객체 사이의 의사소통에 사용하기 시작하면, 애플리케이션의 전반적인 객체 모델이
심각하게 손상되고 나중에 이를 회복하기도 어려워진다. 프로그래머는 객체와 생성자의 관점이 아니라 구문과 연산자의 관점에서 생각하기 시작한다.

객체지향적이고 선언형 스타일을 유지하기 위해서는, 데이터를 객체 안에 감추고 절대로 외부에 노출해서는 안된다. 정확하게 무엇을 캡슐화하고 있고, 자료구조가 얼마나 복잡한 지는 오직 객체만 알고 있어야 한다.

3.5.2 좋은 의도, 나쁜 결과
getter와 setter를 옹호하는 측에서는 정반대라고 주장하겠지만, 근본적으로 getter와 setter는 캡슐화 원칙을 위반하기 위해 설계 되었다.
Java에서 getter와 setter는 클래스를 자료구조로 바꾸기 위해 도입됐다.

    class Cash {
     public int dollars;
    } 
이와 같이 객체의 프로퍼티들을 public으로 만들면서 클래스를 자료 구조로 바꿀 수 있지만 Java의 프로그래밍 기본 규칙을 심각하게 위반한다.
이런 코드를 본 사람은 프로그래머가 OOP를 제대로 이해하지 못했다고 생각한다. 따라서 public 프로퍼티를 추가해야 하는 어색한 상황을 피하기 위해
프로퍼티를 private로 변경하고 모든 프로퍼티에 getter와 setter를 추가하기로 결정한다. 

필자의 요점은 getter와 setter를 사용하면 OOP의 캡슐화 원칙을 손쉽게 위반할 수 있다는 점이다. 겉으로는 메서드처럼 보이지만 실제로는 우리가 데이터에 직접 접근하고 있다는 불쾌한 현실을 가리고 있을 뿐이다.
이는 행동이 아닌 데이터를 표현 할 뿐이기 때문이다.

3.5.3 접두사에 관한 모든 것
getter/setter 안티 패턴에서 유해한 부분은 두 접두사인 get과 set이라는 사실이 중요하다. 두 접두사는 이 객체가 진짜 객체가 아니고 어떤 존중도 받을 가치가 없는 자료구조라는 사실을 명확하게 전달한다.

어떤 데이터를 반환하는 메서드를 포함하는 것은 괜찮다. 

    class Cash {
        private final int value;
        public int dollars() {
            return this.value;
        }    
    }
하지만 이 메서드의 이름을 다음과 같이 짓는것은 적절하지 않다.

    class Cash {
        private final int value;
        public int getDollars() {
            return this.value;
        }    
    }
필자가 메서드 이름에 지나치게 집착한다 생각할 수 있지만 그렇지 않다. 이 차이는 근본적이며 매우 중요하다.
getDollars()는 "데이터 중에 dollars를 찾은 후 반환하세요" 라고 이야기 하는 것이고 dollars() 는 "얼마나 많은 달러가 필요한가요?" 라고 묻는 것이다(??. 객체를 도구로 보느냐 하나의 존재로 보느냐의 차이?)
dollars()는 객체를 데이터의 저장소로 취급하지 않고 객체를 존중한다. 
사용자는 이 메서드를 통해 얼마나 많은 달러가 포함되어 있는 지 알 수 있지만, 이 값이 private 프로퍼티로 저장되어 있다고 가정하지 않는다. 내부 구조에 관해 어떤 것도 가정하지 않으며, 결코 이 객체르 ㄹ자료구조라고 생각하지 않는다.

dollars()는 데이터를 노출하지 않지만, getDollars()는 데이터를 노출한다. getDollars() 메서드를 통해 데이터가 표면에 완전히 드러나 있으며, 클래스의 모든 사용자를 이 데이터를 볼 수 있다.
(즉 getDollars 를통해 해당 객체에 dollars라는 값이 있다는 것을 알 수 있고, 이 값을 가져온다고 생각하기 때문. 즉 이게 바로 접근과 차이가 무엇인가 라고 말하는 것이라 생각한다.)
결론은 getter와 setter가 OOP에서 끔찍한 안티 패턴이라는 것이다.
(setter를 사용하지 말라는 말은 많이 접했다. 이는 객체의 재사용으로 인한 리크스, 잘못된 코딩이라고 여겼고 피해야 한다고 생각했다. 
하지만 getter를 사용하지 말아야 한다는 말을 보거나 들었을 땐 어떻게? 그럼 대안은? 이라는 의구심을 지울 수 없어고 편리를 위해 사용하는게 좋다고 생각했다.
이 글을 통해 OOP에서 getter역할을 지우라는 것이 아닌 getter 워딩의 의미를 지워야 한다라는 생각을 하게 된다.)

3.6 부 ctor 밖에서는 new를 사용하지 말자.
    
    class Cash {
        private final int dollars;
        
        public int euro() {
            return new Exchange().rate("USD", "EUR") * this.dollars; 140
        }    
    }
    
이 예제는 의존성에 문제가 있는 코드의 전형적인 모습을 잘 보여주고 있다.
예제의 euro()메서드 안에서는 new 연산자를 이용해 Exchange 인스턴스를 생성하고 있다. 
이게 왜 문제일까? 
사실 클래스가 작고 단순하며 네트워크나 디스크, 데이터베이스 등의 값비싼 자원을 사용하지 않는다면 전혀 문제가 되지 않는다.

문제를 일으킨 범인의 이름은 '하드코딩된 의존성'이다. Cash 클래스는 Exchange 클래스에 직접 연결되어 있기 때문에, 의존성을 끊기 위해서는
Cash 클래스의 내부 코드를 변경할 수 밖에 없다.
Cash 클래스의 소스 코드가 없는 상황에서 사용하는 것을 상상해보자

    Cash five = new Cash("5.00");
    print("$5 equals to %d", five.euro());
    
이 코드는 print() 메서드의 단위 테스트에서 발췌한 것으로, 테스트를 실행할 때마다 매번 뉴욕 증권 거래소 서버와 네트워크 통신이 발생한다.
여기서 NYSE 서버와 네트워크 통신을 하지 않으려 한다면 현재 설계에선 불가능하고, Cash 코드를 수정할 수 밖에 없다.
클래스가 작다면 큰 문제가 아니지만, 더 큰규모에서는 하드코딩된 의존성이 소프트웨어를 테스트하고 유지보수 어렵게 만든다.
이 문제의 근본 원인은 new 연산자이다.
그럼 객체들이 언제 어디서나 다른 객체를 인스턴스화 할 수 있도록 허용해 놓고, 정작 객체들이 마음대로 생성하려고 하면 못 마땅한 이유는 무엇일까?
예제에서는 Cash가 Exchange의 인스턴스를 직접 생성한다. 그리고 이 점이 바로 문제다.
메서드 내부에서 new 연산자를 사용할 수 없도록 금지 했다고 가정하자. 이제 객체가 새로운 객체를 직접 생성할 수 없기 때문에, 
새로운 객체를 ctor의 인자로 전달 받아 private 프로퍼티 안에 캡슐화할 수 밖에 없을 것이다. 다음은 수정된 Cash 클래스이다.
    
    class Cash {
        private final int dollars;
        private final Exchange exchange;
        
        Cash(int value, Exchange exch) {
            this.dollars = value;
            this.exchange = exch;
        }    
        
        public int euro() {
            return this,exchange.rate("USD", "EUR") * this.dollars; 
        }    
    }

문제가 해결됐다. 다음은 print를 테스트하는 올바른 코드다.

    Cash five = new Cash(5, new FakeExchange());
    print("$5 equals to %d", five.euro());

수정한 코드에서는 ctor의 두 번째 인자에서 Exchange 인스턴스를 전달해야 한다. Cash 클래스는 더이상 Exchange 인스턴스를 직접 생성할 수 없다.
오직 ctor을 통해 제공된 Exchange와만 협력할 수 있다.
다시말해 객체가 필요한 의존성을 직접 생성하는 대신, ctor을 통해 의존성을 주입한다.
이와 같이 의존성을 주입하는 것은 좋은 프랙티스이다. 모든 객체를 이런 방법으로 설계해야 한다. 섹션 1.2에서 설명한 것처럼 편의를 위해 부 ctor을 여러개 추가할 수 있다.

    class Cash {
        private final int dollars;
        private final Exchange exchange;
     
        Cash() {//부 ctor
        }
        
        Cash(int value) {//부 ctor
            this(value, new NYSE());
        }
        
        Cash(int value, Exchange exch) { //주
            this.dollars = value;
            this.exchange = exch;
        }    
        
        public int euro() {
            return this,exchange.rate("USD", "EUR") * this.dollars; 
        }    
    }                 

앞의 예제를 살펴보면 우직 부 ctor 안에서만 new 연산자를 사용하고 있다는 것을 알 수 있다. 부 ctor을 제외한 어떤 곳에서도 new를 사용할 수 없도록 금지한다면,
객체들은 상호간에 충분히 분리되고 테스트 용이성과 유지보수성을 크게 향상시킬 수 있다.

객체가 다른 객체를 인스턴스화해야만 하는 경우에는 어떻게 해야할까?
다음은 네트워크 소켓으로 인입되는 요청 스트림을 표현한 객체다.

    class Requests {
    private final Socket socket;
    
        public Requests(Socket skt) {
            this.socket = skt;
        }
        
        public Request next() {
            return new SimpleRequest(/* 소켓에서 데이터를 읽는다 */);
        }
    }
Requests 클래스는 next() 메서드를 호출할 때마다 매번 새로운 Request 객체를 생성해서 반환해야 한다.
객체를 생성하기 위해서는 반드시 new 연산자가 필요하지만 next() 메서드는 ctor이 아니다. 따라서 이 설계는 앞의 규칙을 위반한다.
해결하면
    
    class Requests {
        private final Socket socket;
        private final Mapping<String, Request> mapping
        
        public Request(Socket skt) {
            this(skt,
                new Mapping<String, Request>() {
                    @Override
                    public Request map(String data) {
                        return new SimpleRequest(data);
                    }
                });
        }                
        
        public Request(Socket skt, Mapping<String, Request> mpg) {
            this.socekt = skt;
            this.mapping = mpg;
        }
            
        public Request next() {
            return this.mapping.map(/* 소켓에서 데이터를 읽는다 */);
        }
    }                    
Requests 클래스는 텍스트 데이터를 Request 인스턴스로 변환하는 Mapping 인스턴스를 캡슐화한다. new 연산자는 오직 부 ctor 내부에서만 사용된다.
개선된 설계에서는 더 쉽게 Request 클래스의 설정을 변경할 수 있으며 하드코딩된 의존성도 존재하지 않는다.
SimpleRequest 대신 테스트에 적합한 객체를 반환하도록 구현한 Mapping 인스턴스를 주입하는 것도 가능하다.
우리는 메서드나 주 생성자 안에서 new를 사용하는 매순간마다 뭔가를 잘못하고 있다는 사실을 떠올려야한다.
new를 합법적으로 사용할 수 있는 유일한 곳은 부 ctor뿐이다.

이 규칙이 의존성 주입과 제어 역전에 관해 알아야 하는 전부라고 할 수 있다.
부 ctor에서만 new를 사용해야 한다는 간단한 규칙을 불변 객체와 조합하면, 코드는 깔끔해지고 언제라도 의존성을 '주입'할 수 있게 될 것이다.
                