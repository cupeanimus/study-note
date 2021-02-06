1.1 -er로 끝나는 이름을 사용하지 말자.

-er로 끝나는 이름을 가진 클래스명은 잘못 지어졌다고한다. 그렇다면 흔히 쓰는 -controller는 어떻게 지어야할까. 아니면 구조가 프로젝트 구조가 어떻게 바껴야 할까?
cotr의 주된 작업은 제공된 인자를 사용해서 캡슐화하고 있는 프로퍼티를 초기화하는 일. cotr = constructor
초기화 로직을 단 하나의 ctor에만 위치시키고 주 cotr이라고 칭하면, 다른 부 cotr이 주 cotr을 호출하도록 설계
(util성 기능을 담당하는 class 이름은 어떻게 지어야 할까?)

아래는 하나의 주 cotr(생성자) 다수의 부 cotr(one primary, many secondary)' 원칙을 따르지 않은 코드제 예제
    public class Cash { //클래스가 무엇인지에 대한 기반 - 지향
    
        private int dollars;
        Cash(float dlr){    .
            this.dollars = (int) dlr;
        }
    
        Cash(String  dlr){
        this.dollars = Cash.parse(dlr);
        }
        
        Cash(int dlr) {
            this.dollars = dlr;
        }
    }    

인자에 손대지 말라는 법칙을 어긴 코드 예제
    
    class Cash {
        private int dollars;
        Cash(String  dlr){
            this.dollars = Integer.parsInt(dlr);
        }
    }
객체 초기화에는 코드가 없어야 하고 인자를 건드려서는 안된다.
대신, 필요하다면 인자들을 다른 타입의 객체로 감싸거나 가공하지 않는 형식(raw form)으로 캡슐화해야 한다.

인자로 전달된 텍스트를 건드리지 않고 동일한 작업을 수행한 예제

    class Cash {
        private Number dollars;
        Cash(String dlr) {
            this.dollars = new StringAsInteger(dlr);
        }
    }
    class StringAsInteger implements Number {
        private String source;
        StringAsInteger(String src) {
            this.source = src;
        }
        int intValue() {
            return Integer.parsInt(this.source);
        }        
    }

첫번째는 객체로 초기화 하는 시점에 바로 텍스트를 숫자로 변환 하고
두번째는 실제로 사용하는 시점까지 객체의 변환작업을 연기한다.
Cash 클래스는 주 ctor 한개와 부 ctor 총 ctor을 포함한 모습

    class Cash {
        private Number dollars;
        Cash(String dlr) {  //부 ctor
            this.dollars = new StringAsInteger(dlr);
        }
        Cash(Number dlr) { //주 ctor
            this.dollars = dlr;
        }          
    }

표면적으로 두 Cash 클래스로부터 인스턴스를 생성하는 과정은 동일해보이지만,
첫번째 예제의 객체 five는 숫자 5를 캡슐화 하지만 두번째 예제의 five는 Number처럼 보이는 StringAsInterger 인스턴스를 캡슐화한다.
        
    class StringAsInteger implements Number {
        private String text;
        StringAsInteger(String txt) {
            this.text = txt;
        }
        public int intValue() {
            return Integer.parsInt(this.text);
        }        
    }
    
    vs
    
    class StringAsInteger implements Number {
        private int num;
        public StringAsInteger(String txt) {
            this.num = Integer.parsInt(txt);
        }
        public int intValue() {
            return this.num;
        }        
    }
    
    Number num = new StringAsInteger("123");
    num.intValue();
    num.intValue();
    
    
위 두 코드에서 더 빠른코드는 첫번째 코드다.
ctor에 코드가 없을경우 성능 최적화가 더 쉽기 때문에 실행속도가 더 빨라진다.
텍스트 파싱은 객체를 초기화하는 시점에 단 한 번 수행하기 때문에 아래 코드가 더 비효율적이다
이러한 이유로 파싱이 여러 번 수행되지 않도록 하고 싶다면 데코레이터를 추가해서 최초의 파싱 결과를 캐싱할 수 있다.

    class CashNumber implements Number {
        private Number origin;
        private Collection<Integer> cached = new ArrayList<>(1);
        public CachedNumber(Number num) {
            this.origin = num;
        }
        public int IntValue(){
            if(this.cached.isEmpty()) {
            this.cached.add(this.origin.intValue());
            }
         return this.cached.get(0);   
        }
    }    
위는 OOP에서 악명 높은 null을 피하기 위해 ArrayList를 사용한다. 
    
    Number num = new CachedNumber(
        new StringAsInteger("123")
        );
    num.intValue(); //첫 번째 파싱
    num.intValue(); //여기에는 파싱하지 않음

위와같은 코드는 쉽고 투명하다. 객체를 인스턴스화하는 동안에는 객체를 만드는(build)일 외에는 어떤 일도 수행하지 않는다.
실제 작업은 객체의 메서드가 수행하고, 이 과정을 제어할 수 있고 최적화할 수 있다.
따라서 생성자에서 코드를 없애면 사용자가 쉽게 제어할 수 있는 투명한 객체를 만들 수 있으며, 객체를 이해하고 재사용하기도 쉬워진다.
