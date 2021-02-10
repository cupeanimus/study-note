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

     