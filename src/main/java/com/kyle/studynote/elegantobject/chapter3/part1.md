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

    

                       
        
           
         
       


       