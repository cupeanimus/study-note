2.1 가능하면 적게 캡슐화 하라.
근거는 없지만 4개 이하의 객체를 캡슐화 할 것을 권장한다(dto, entity 성격의 객체는 어떻게 하나? 조합으로?)
Java의 결함을 해결하기 위해 == 연산자 대신 equals() 메서드를 오버라이드하자(필자는 Lombok의 @EqualsAndHashCode 어노테이션을 사용한다)

2.2 최소한 뭔가는 캡슐화 하라.
    
    public class Year {
        private Number num;
        Year(final Millis msec) {
            this.num = new Min(
                    new Div(
                            msec,
                            new Mul(1000, 60, 60, 24, 30, 12)
                    ),
                    1970
            );
        }

    //또는
    Year(final Millis msec) {
        this.num = msec.div(
                1000.mul(60).mul(60).mul(24).mul(30).mul(12)
        ).min(1970);
    }
    int read() {
        return this.num.intValue();
    }

2.3 항상 인터페이스를 사용하라.
기술적인 관점에서 객체 분리란 상호작용하는 다른 객체를 수정하지 않고도 해당 객체를 수정할 수 있도록 만든다는 의미이고 이를 가능하게 하는 가장 훌륭한 도구가
바로 인터페이스다

    interface Cash {
        Cash multiply(float factor);
    }
    
    public class DefaultCash implements Cash {
        private int dollars;
        DefaultCash(int dlr) {
            this.dollars = dlr;
        }
    
        @Override
        Cash multiply(float factor) {
            return new DefaultCash((int) (this.dollars * factor));
        }
    }

여기서 Employee 클래스는 Cash 인터페이스의 구현 방법에 관심이 없고, 메서드가 어떻게 동작하는지도 관심 없다.
즉, 동작 방식을 알지 못한다 <- Cash 인터페이스를 이용하면 Employee클래스와 DefaultCash 클래스를 느슨하게 분리할 수 있다는 의미.

올바르게 설계된 클래스라면 최소한 하나의 인터페이스라도 구현하지 않는 퍼블릭 메서드를 포함해서는 안된다.
즉 인터페이스에 구현하지 않는 퍼블릭 메서드를 포함해서는 안된다.

    잘못된 예
    class Cash {
        public int cents() {
            //어떤 작업 수행
        }    
    }

클래스가 존재하는 이유는 다른 누군가가 클래스의 서비스를 필요로 하기 때문. 서비스는 계약이자 인터페이스이기 때문에
클래스가 제공하는 서비스는 어딘가에 문서화 되어야 한다. 게다가 서비스 제공자들은 서로 경쟁한다. 다시 말하자면 동일한 인터페이스를 구현하는 여러 클래스들이 존재한다는 뜻이며
각각의 경쟁자는 서로 다른 경쟁자를 쉽게 대체할 수 있어야 한다. 이것이 느슨한 결합도의 의미이다.

(실제 작업하며 인터페이스 없이 객체에 메서드를 바로 구현하는 경우가 많다. 인터페이스를 거치지 않기 때문에 단계가 개발이 편리하고 유지보수가 더 쉽다고 생각했고, 회사 업무에서도 요구받았다.
이 두 방법에 대해 좀 더 생각해보려 한다.)


 

    



    