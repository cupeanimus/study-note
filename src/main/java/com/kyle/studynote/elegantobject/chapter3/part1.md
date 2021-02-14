3.1 5개 이하의 public 메서드만 노출하자

클래스가 크고 작다고 생각되는 기준은 public 메서드의 개수가 될 수 있다.
클래스를 작게 만들어서 얻는 이득으로는 우아함, 유지보수성, 응집도, 테스트 용이성 향상이 있다.
여기서 우아함이란 실수할 가능성이 낮은 것을 말한다.

3.2 정적 메서드를 사용하지 말자

    class Webpage {
        public static String read(String uri) {
            // HTTP 요청을 만들고
            // UTF-8 문자열로 변환한다
        }
    }

이제 간단하게 WebPage 클래스를 사용할 수 있다

    String html = Webpage.Read("http://www.java.com");
    여기서 read() 메서드는 강하게 반대하는 정적 메서드의 일종이다.
    정적 메서드를 없애면
    
    class Webpage {
        private final String uri;
        public String content(String uri) {
            // HTTP 요청을 만들고
            // UTF-8 문자열로 변환한다
        }
    }
    
    String html = new Webpage("http://www.java.com").content();

정적메서드와 비슷하게 사용 할 수 있다.
정적메서드를 사용하면 매번 객체를 생성할 필요도 없어 자주 쓰는경우 더 빠르고 효율적이라고 생각하기 쉬우나
OOP를 이해하지 못한 객체 패러다임의 남용일뿐이다.
정적메서드의 치명적인 단점은 유지보수가 어렵다. (이 책에서 주장하는 것은 좋은 코드 = 유지보수성이 좋은 코드, 뭐든지 유지보수로 귀결된다)

3.2.3 유틸리티 클래스
 실제로는 클래스가 아니라 편의를 위해 다른 메서드들이 사용하는 정적 메서드들을 모아 놓은 메서드들의 컬렉션
 
    class Math {
        private Math() {
        // 의도적으로 공백을 남김
        }
        public  static int max(int a, int b) {
            if( a < b) {
                return b;
                }
            return a;
        }        
    }    

유틸리티 클래스를 구현할때는 '클래스'의 인스턴스가 생성되는 것을 방지하기 위해 private ctor을 추가하는 것이 좋다.
유틸리티 클래스는 정적 메서드처럼 단순히 나쁜 요소가 아니라, 나쁜 요소들을 모아 놓은 집합체. 정적 메서드들의 단점들을 몇배로 증폭시키는 끔직한 안티패턴

3.2.4 싱글톤 패턴
정적 메서드 대신 사용할 수 있는 매우 유용한 개념. 
싱글톤 안에는 하나의 정적 메서드가 존재하며, 싱글톤의 형태는 일반적인 객체와 거의 유사해 보인다.

    class Math {
        private static Math INSTANCE = new Math();
        private Math() {};
        public staitc Math getInstance() {
            return Math.INSTANCE;
        }
        public int max(int a, int b) {
            if(a < b) {
                return b;
                }
            return a;
        }        
    }
    
Math 클래스는 싱글톤의 대표적인 예.
Math 클래스의 인스턴스는 오직 하나만 존재할 수 있고, 이 유일한 인스턴스 이름은 INSTANCE 이다.
어디서든 getInstance()를 호출해서 이 인스턴스에 접근 할 수있다.
싱글톤은 유명한 디자인 패턴이지만 사실 끔찍한 안티패턴이다.
유틸리티 클래스와 싱글톤의 차이점을 살펴보자.
유틸리티 클래스

    class Math {
        private Math() {}
        public int max(int a, int b) {
            if(a < b) {
                return b;
                }
            return a;
        }        
    }

두 가지 max() 메서드의 사용 방법 비교


    Math.max(5,9);   //유틸리티 클래스
    Math.getInstance().max(5, 9);    //싱글톤
위와같이 두 코드가 동일한 일을 수행한다.    
정적 메서드와 유틸리티 클래스라는 기법이 있는데도 불구하고 싱글톤 패턴이 발명된 이유는 무엇일까?
1. 싱글톤은 상태를 캡슐화 할 수 있다.

    Clss User {
        private static User Instance = new User();
        private Stringn name;
        private static User() {}
        public static User getInstance() {
            return User.INSTANCE;
        }
        public String getName(){
            return this.name;
        }
        public String setName(String txt){
            this.name = txt;
        }
    }
끔찍한 코드지만 User는 싱글톤이며, 현재 시스템을 사용하고 있는 사용자를 의미한다.
웹 세션을 구현하기 위해 싱글톤을 채택한 많은 웹 프레임워크에서 사용하는 유명한 접근방식이다.
따라서 위와같은 코드를 많이 본 개발자들은 싱글톤과 유틸리티 클래스 사이의 차이점은 '싱글톤은 상태를 유지'한다고 답한다.
하지만 틀린 대답이다. 싱글톤의 목적은 상태를 유지하는 것이 아니다. 다음은 User 싱글톤과 정확하게 동일한 일을 하는 유틸리티 클래스이다.

    Class User {
        private static String name;
        private User() {}
        public static String getName(){
            return this.name;
        }
        public static String setName(String txt){
            this.name = txt;
        }
    }
    
이 유틸리티 클래스 역시 상태를 유지하기 때문에 앞 선 싱글톤과 아무런 차이가 없다.
그렇다면 싱글톤과 유틸리티클래를 구분하는 핵심적인 차이는 뭘까?
싱글톤은 분리가능한 의존성으로 연결되어 있는데 반해, 유틸리티 클래스는 분리가 불가능한 하드코딩된 결합도를 가진다.
다시 말해서 싱글톤의 장점은 getInstance()와 함께 setInstance()를 추가할 수 있수 있다.
싱글톤 클래스의 Math를 사용한 코드는

    Math.getInstance().max(5,9);
    
이 코드는 Math 클래스에 결합되어 있다. 다시말해, Math 클래스는 이 코드가 의지하고 있는 의존성이다.
코드를 테스트하기 위해 Math 클래스를 요청 처리할 수 있는 상태로 만들어야 하는데
Math 클래스 경우는 매우 간단하지만 싱글톤이라면 모킹을 하거나 테스트하기 쉬운 뭔가로 교체할 필요가 있다.
이럴때는
    
    Math math = new FakeMath();
    Math.setInstance(math);

처럼 내부에 캡슐화된 정적 객체를 교체해서 전체 개념을 테스트할 수 있다.
따라서 올바른 대답은 캡슐화된 객체를 변경할 수 있기 때문에 싱글톤이 유틸리티 클래스보다는 더 좋다는 것이다.
유틸리티 클래스 안에는 객체가 존재하지 않기 때문에 어떤 것도 변경할 수 없다.
유틸리티 클래스는 분리할 수 없는 하드코딩된 의존성이다. OOP에서는 피해야만 하는 최악의 요소라고 할 수 있다.
싱글톤이 유틸리티 클래스보다 낫다고는 하지만 여전히 안티패턴이자 형편없는 개념이다.
논리적인 관점과 기술적인 관점 모두에서 싱글톤은 전역변수 그 이상도, 그 이하도 아니기 때문이다.
OOP에는 전역 범위가 없다. 따라서 전역변수를 위한 자리역시 없다. (웹 서비스에서 전역변수가 필요할때는 어떻게 할까?)

싱글톤은 JAVA에서 전역변수를 사용할 수 있는 방법이다.
정적 메서드가 기술적으로 싱글톤이라는 속임수를 가능하게 한 것이다.

그렇다면 대안은 무엇일까?
캡슐화를 사용하는 것이다.

3.2.5 함수형 프로그래밍

    class Max implements Number {
        private final int a;
        private final int b;
        public Max(int left, int right) {
            this.a = left;
            this.b = right;
        }
        @Override
        public int intValue() {
            return this.a > this.b ? this.a : this.b;
        }
    }
    
    Number x = new Max(5,9);

    동일한 기능을 수행하는 함수를 Lisp으로 구현하면
    (defn max
        (a b)
        (if (> a b) a b))
Lisp 코드가 훨씬 짧은데 객체를 사용하는 이유는?
FP(함수형 프로그래밍)보다 OOP의 표현력이 더 뛰어나고 강력하기 때문
이상적인 OOP 언어에는 클래스와 함께 함수가 포함되어야 한다. 작은 프로시저로 동작하는 java의 메서드가 아니라,
하나의 출구만 포함하는 순수한 FP 패러다임에 기반하는 진정한 함수를 포함해야 한다. 
이런 언어를 사용하는 것이 가장 이상적인 상황이라고 생각.

3.2.6
조합 가능한 데코레이터(다른 객체를 감싸는 객체)
    
    names = new Sorted(
        new Unique(
            new Capitalized(
                new Replaced(
                    new FileNames(
                        new Directory(
                            "/var/users/*.xml:
                        )
                    ),
                "([^.]+)\\.xml",
                "$1"
                )
            )                
        )
    );
    
필자 관점에서 이 코드는 매우 깔끔하면서도 객체지향적이고, 3.2.2에서 설명한 것처럼 순수하게 선언형이다.(이 책을 읽기 전이라면 지저분하다는 느낌을 받았을 것이다)
이 코드는 어떤 일도 실제로 '수행하지는 않지만', 디렉토리안의 모든 파일 이름(FileNames)을 정규 표현식을 이용해서 치환하고 대문자로 변경하고, 중복된 이름을 제거하기 위해
유일하게 만들고, 다시 정렬해서 컬렉션에 담은 후 이 컬렉션을 가리키는 names 객체를 선언한다. 방금 객체를 어떻게 만들었는지를 설명하지 않아도 이 객체가 무엇인지를 선언만으로 설명하였다.
(이것이 객체지향이 추구하는 것. 어떤 형태를 띄든간에 모양이 아닌 표현?에 중점)
필자는 이런 객체들을 조합가능한 데코레이터라고 부른다. 각 클래스들이 하나의 테코레이터이다. 데코레이터의 상태는 내부에 캡슐화하고 있는 객체들의 상태와 동일하다.
프로그래머는 데코레이터를 조합하는 일을 제외한 다른 일은 하지 말아야 한다.
데코레이터를 조합한 후 어떤 시점에 app.run()을 호출하면 데코레이터로 구성된 전체 피라미드가 반응하기 시작할 것이다.
if, for, switch, while과 같은 절차적인 문장이 포함되어 있어서는 안된다. 지나치가 이상적으로 들릴 수도 있지만 매우 현실적인 이야기다.
연산자 if 대신 클래스 If를 제공하지 못할 이유는 없다(하지만 주어진 연산자까지 클래스 객체로 처리하는 것이 생산성, 유지보수에 좋은가? 좋은 개발인가?)

    float rate;
    if (client.age() > 65) {
        rate = 2.5;
    } else {
        rate = 3.0;
    }
    
    객체지향적인 코드로 변경하면
    float rate = new If(
        client.age() > 65,
        2.5, 3.0)
    );
    객체지향적으로 조금 더 개선하면?
    float rate = new If(
        new Greater(client.age(), 65),
        2.5, 3.0)
    );                 
    최종적으로는
    float rate = new If(
        new GreaterThan(new AgeOf(client), 65),
        2.5, 3.0)
    );    
    확실하게 코드만으로 어떤 값을 의미하는지는 알 수 있다.
객체지향 프로그래밍이란 더 작은 객체들을 기반으로 더 큰 객체들을 조합하는 작업이다.
지금까지 설명하 ㄴ내용들이 정적 메서드와 관련이 있을까? 정적 메서는 조합이 불가능하다.
이것이 OOP에서 정적 메서드를 사용해서는 안되는 또 다른 이유이다.


   


 
        
            
       
        
              

                       
        
           
         
       


       