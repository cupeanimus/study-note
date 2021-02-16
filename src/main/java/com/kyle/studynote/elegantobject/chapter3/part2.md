3.3 인자의 값으로 NULL을 절대 허용하지 말자
코드 어딘가에 NULL이 존재한다면 커다란 실수를 저지르고 있는 것이다.

    public Interable<File> find(String mask) {
        //디렉토리를 탐색해서 "*.txt"와 같은 형식의
        //마스크에 일치하는 모든 파일을 찾는다.
        //마스크가 NULL인 경우에는 모든 파일을 반환한다
    }
전달할 객체가 없으므로 값이 없는 것으로 간주하라는 의사를 표현할 수 있도록 사용자에게 진짜 객체 대신 NULL을 허용하는 것은 일반적인 방법이다.
    
    public Interable<File> findALL();
    public Interable<File> find(String mask);

실제로 위 두 메서드를 하나로 합 칠수 있는 편리한 방법으로 보인다.
이 방식이 논리적이라고 생각 할 수 있겠지만, 각각의 객체가 자신의 행동을 온전히 책임진다는 객체 패러다임과는 상반되는 아이디어이다.
NULL을 허용하는 find() 메서드를 구현하기 위해서는 다음과 같이 분기를 처리할 필요가 있다.

    public Interable<File> find(String mask) {
        if (mask == null) {
        //모든 파일을 찾는다
        } else {
        //마스크를 사용해서 파일을 찾는다
        }     
    }

이 코드에서 문제가 되는 부분은 mask == null이다.
mask 객체에게 이야기 하는 대신, 이 객체를 피하고 무시한다. 
객체를 존중한다면 다음과 같이 행동할 수 있다.

    public Interable<File> find(String mask) {
        if(mask.empty)) {
        //모든 파일을 찾는다
        } else {
        //마스크를 사용해서 파일을 찾는다
        }     
    }
    
    더 개선하면
    public Interable<File> find(String mask) {
        Collection<File> files = new LinkedList<>(); 
        for (File file : /* 모든 파일 */)
            if (mask.matches(file)) {
                files.add(file);
            }
        }
        return files;             
    }        

mask 객체를 존중했다면 조건의 존재 여부를 객체 스스로 결정하게 했을 것이다. (객체 관점의 코딩 = 객체지향이라 이해해도 될 것 같다)
겉모습만으로 객체를 판단해서는 안되고, '진짜' 객체라면 대화에 응할 것이고 NULL이면 대응하지 않겠다는 식으로 객체와 의사소통해서는 안된다.
NULL 여부를 체크함으로써 객체가 맡아야 하는 상당량의 책임을 빼앗게 된다. 이것은 외부에서 자신의 데이터를 다뤄주기만을 기대하고 스스로를 책임질 수 없는
멍청한 자료구조로 객체를 퇴하시키는 것이다.

검색 조건을 지정하기 위해 find() 메서드에 전달하는 Mask 인터페이스가 있다
    
    interface Mask {
        boolean matches(File file);
    }
    
    이 인터페이스의 적절한 구현은 '글롭(glob)' 패턴('*.txt' 형식의 패턴)을 캡슐화 하고 이 패턴에 대해 파일 이름을 매칭시킬 것이다.
    Null객체는 다음과 같이 구현할 수 있다.
    
    class AnyFile implements Mask{
        @Override
        boolean matches(File file) {
        retrun true)
        }
    }
    
AnyFile은 Mask의 특별한 경우로, 어떤 내부 로직도 포함하지 않는다. 어떤 파일을 전달하더라도 항상 true를 반한환다. 이제부터 null 값을 전달하는 대신,
AnyFile의 인스턴스를 생성해서 find()메서드에 전달하면 된다. find() 메서드는 무슨 일이 일어나고 있는지 전혀 알지 못한채, 여전히 올바른 Mask가 전달되었다고 생각 할 것이다.
메서드가 인자의 값으로 NULL을 허용하지 않기로 가정했는데, 클라이언트가 여전히 NULL을 전달한다면 어떻게 해야할까?
기본적으로 두 가지 방법이 있다. 하나는 방어적인 방법으로 NULL을 체크한 후 예외를 던진다.

    public Iterable<File> find(Mask mask) {
        if(mask == null) {
        throw new IllegalArgumentException(
            "Mask can't be NULL; please provide an object.");
        }
    //마스크를 사용해서 파일을 찾아 반환한다
    }
두 번째 방법은 개인적으로 선호하는 방법으로 NULL을 무시하는 것이다. 여기서 인자가 절대 NULL이 아니라고 가정하고 어떤 대비도 하지 않는다. 메서드를 실행하는 도중
인자에 접근하면 NullPointException이 던져지고 메서드 호출자는 자신이 실수했다는 사실을 인지하게 된다.
중요하지 않은 NULL 확인 로직으로 코드를 오렴시켜서는 안된다. NullPointException은 잘못된 위치에 NULL이 전달됐다는 사실을 알려주는 올바른 지표다.
(NullPointException으로 에러를 방지하기 위해 많은 부분에 null 체크를 하고 있다. 필자가 말하는 대로 굳이 이렇게 체크해서 방지하는 것보다 에러를 발생시켜 잘못된 사실을 인지하고
이러한 호출이 오지 않도록 하는것도 좋은 방법이라 생각된다.)      

3.4 충성스러우면서 불변이거나, 아니면 상수이거나

    class Webpage {
        private final URI uri;
        
        WebPage(URI path) {
            this.uri = path;
        }
        
        
        public String content() {
        // HTTP GET 요청을 전송해서
        //웹 페이지의 컨텐츠를 읽은 후,
        //읽혀진 컨텐츠를 UTF-8 문자열로 변환한다
        }
        //(하나의 서비스 개념. 컨텐츠에서 수행하는 일이 너무 많기 때문에 여러 메서드를 포함하고 있을 구조)
    }
    
여기서 WebPage가 불변일까, 가변일까?
가변 객체라고 생각한다면, 다시 한 번 고민해봐야 한다. 결론부터 말하자면 content()메서드를 호출할 때마다 서로 다른 값이 반환되더라도 이 객체는 불변이다.
여기서 객체의 행동이나 메서드의 반환값은 중요하지 않다. 핵심은 객체가 살아있는 동안 상태가 변하지 않는다는 사실이다. 그리고 이 사실로 인해 대부분의 사람들이 불변 객체의 개념을 혼동한다.
직관적으로 사람들은 불변 객체의 메서드를 호출할 때마다 상수(constant)처럼 매번 동일한 데이터가 반환되리라 기대한다. 다시 말해서 모든 불변 객체가 문자열 리터럴이나 숫자처럼 동작하리라고 생각한다.
실제로도 Java를 포함한 많은 언어들에서 String, URI, Double과 가 같은 대부분의 불변 클래스는 상수처럼 동작한다. 따라서 불변 객체의 정의에 따라 적절해 보일수도 있지만 결론적으로 이런 사고 방식은 틀렸다.
상수처럼 동작하는 객체는 단지 불변성의 특별한 경우일 뿐이기 때문이다.
불변 객체는 그 이상이다. 비록 content() 메서드의 결과를 예측할 수 없더라도 WebPage는 불변 객체에 속한다. WebPage 클래스는 실제 웹 페이지와 통신하기 때문에 우리는 이 객체가 무엇을 돌려줄지 알지 못한다.
content() 메서드가 HTTP를 통해 어떤 데이터를 수신할 지 전혀 예상할 수 없다. 이런 측면에서는 WebPage가 String 클래스와 다르지만 WebPage 역시 String처럼 불변이다.
결과가 변하기 때문에 상수는 아니지만, 객체가 대표하는 엔티티에 충성하기 때문에 불변 객체로 분류한다.
(값이 변하는지 유무로 불변인지 아닌지 판단하는게 주어진 역할이 변하는지 아닌지에 따라 불변 객체를 분류한다고 이해된다)

    public void echo() {
        File f = new File("/temp/test.txt");
        System.out.println("File size: %d, file.length());
    }
여기서 f의 가시성 범위는 echo() 메서드의 '경계'에 대응한다. 예제에서는 디스크에 저장된 파일의 사이즈를 알아내기 위해 객체 f의 length() 메서드와 의사소통하고 있다.
객체 f는 실제 파일의 입장을 대편한다. 우리 관점에서 객체 f는 echo() 메서드 안에서만큼은 파일이다.
WebPage 객체의 상태가 URI이듯이 File 클래스의 상태는 파일 시스템 상에 위치한 파일의 전체 경로이다. 따라서 f의 상태는 /tmp/test.txt이다.

기본적으로 모든 객체는 식별자(identity), 상태(state), 행동(behavior)을 포함한다. 식별자는 f를 다른 객체와 구별한다. 상태는 f가 디스크 상의 파일에 대해 알고 있는 것이다.
행동은 요청을 수신했을 때 f가 할 수 있는 작업을 나타낸다. 불변 객체와 가변 객체의 중요한 차이는 불변 객체에는 식별자가 존재하지 않으며, 절대로 상태를 변경할 수 없다는 점이다.
좀 더 정확하게 말해서 불변 객체의 식별자는 객체의 상태와 완전히 동일하다.

WebPage를 살펴보면 동일한 URI를 가진 두 개의 WebPage 인스턴스를 생성할 경우 이들은 서로 다른 객체일까? 서로 다른 행동을 노출할까?
두 객체는 동일한 실제 웹 페이지를 대표한다. 이 웹페이지의 좌표는 항상 같다. 따라서 별도로 인스턴스를 생성했다고 하더라도, 두 객체는 동일하다.
객체 팩토리(섹션 1.1)를 완벽하게 구현하고 싶다면 이런 제약을 잘 이해하고 동일한 상태를 캡슐화하는 중복된 인스턴스를 생성하지 말아야 한다.

하지만 Java를 포함한 대부분의 OOP 언어에서는 상태가 동일하더라도 서로 다른 객체라고 판단한다. 기본적으로 각 객체는 재정의할 수 있는 자신만의 유일한 식별자를 가진다. 
예를 들어, Java에서는 WebPage 클래스의 식별자를 다음과 같이 정의한다(다음은 의사 구현(pseudo implementation)이며, 실제 equals()는 더 복잡해진다).

    class Webpage {
        private final URI uri;
        WebPage(URI path) {
            this.uri = path;
        }
        
        @Override
        public void equals(Object obj) {
            return this.uri.equals(WebPage.class.cast(obj).uri); //void인데 return???
        }
        
        @Override
        public int hashCode() {
            return this.uri.hashCode();
        }        
    }
equals()와 hashCode() 메서드 모두 캡슐화된 uri 프로퍼티에 기반하며, WebPage 클래스의 객체들을 투명하게(transparent) 만든다. 여기서 '투명하다'는 말은 객체들이 더 이상 자기 자신만의 식별자를 가지지 않는다는 뜻이다.
객체들은 웹 상의 페이지를 대표하며, 객체들이 포함하는 유일한 상태는 URI 형태의 페이지 좌표뿐이다.

반면에 가변 객체는 완전히 다른 방식으로 동작한다. 가변 객체의 상태는 변경이 가능하기 때문에, 상태에 독립적인 식별자를 별도로 포함해야 한다. 
완벽한 객체지향 세계에는 불변 객체만 존재하기 때문에 equals()와 hashCode()라는 두 메서드가 필요하지 않다. 두 메서드의 구현은 모든 클래스에 걸쳐 동일하다.
새롭게 구현하거나 재정의할 필요가 없다. 캡슐화된 상태만으로 불변 클래스의 모든 객체들을 식별할 수 있기 때문이다. 상태는 불변 객체를 식별하기 위한 필요충분 조건이다.(이러한 관점에 util기능을 가진 클래스는 과연 어떻게 표현해야 할 지 의문이 든다)

불변 객체는 실제 객체가 어디에 존재하고 어떤 방식으로 사용해야 하는지 알고 있다. 이것이 전부다. WebPage에 대해서 이 말이 논리적이라고 느껴져야한다.
(이해한 것을 설명하자면 uri 가 네이버와 다음인 웹 페이지를 한번의 호출에서 다뤄야한다면 다음 순서에 따라 네이버 웹 페이지가 다음 웹페이지로 재사용되지 않고 처음부터 끝까지 네이버와 다음의 각 state를 가졌고, state만 다를 뿐 동일 한 역할을 수행하는 WebPage이므로 불변 객체라고 할 수 있다.
 아까 말한 값의 변화가 state의 변화인 것이다)

이해를 위해 숫자 컬렉션 불변 객체로 구현해보자.
오직 불변 객체만을 이용해서 컬렉션을 구현하려면 상수 리스트로 구현하는 방법과 불변 리스트로 구현하는 두 방법이 있다.
상수리스트
    
     class ConstantList<T> {
        private final T{} array;
        
        ConstantList() {
            this(new T[0]);
        }
        
        private ConstantList(T[] numbers) {
            this.array = numbers;
        }
     
        ConstantList with(T number) {
            T[] nums = new T[this.array.length +1];
            System.arraycopy(this.array, 0, nums, 0, this.array.length);
            nums[this.array.length] = number;
            retrun new ConstantList(nums);
        }
        
        Iterable<T> iterate() {
            return Arrays.asList(this.array);
        }
     }
ConstantList는 다음과 같은 방식으로 사용할 수 있다.
    
    ConstantList list = new Constantlist()
    .with(1) //새로운 객체
    .with(15) //또 다른 객체
    .with(5) //그리고 또 다른 객체

상수 리스트에서는 리스트를 수정하거나 새로운 요소를 추가할 때마다 리스트에 포함된 모든 요소의 복사본을 가지는 새로운 리스트를 생성한다.
이런 방식으로 구현된 객체를 불변 객체라고 부르지만, 객체가 대표하는 실제 엔티티와 객체의 상태가 동일한 경우에는 '불변' 대신 '상수'라는 용어를 사용하길 권장한다.
this.array는 ConstantList의 상태인 동시에 ConstantList 객체가 대표하는 엔티티와 동일하다. 객체는 배열을 대표하며, 이 객체의 상태는 바로 그 배열이다.
다시 한 번 강조하지만 상수 객체는 불변 객체의 특별한 경우일 뿐이다.

불변 리스트는 다음과 같은 방법으로 만들 수 있다.

    class ImmutableList<T> {
        private final List<T> items = LinkedList<T>();
        
        void add(T number) {
            this.items.add(number);
        }
    
        Iterable<T> iterate() {
        return Collections.unmodifiableList(this.items);
        }
    }

이 객체가 불변 객체로 보이는가? 이 클래스의 객체들은 수정이 가능해 보인다. 따라서 가변 객체로 볼 수 있지 않을까? 하지만 절대 그렇지 않다.
실제로 수정할 수있지만 객체 자체를 수정하는 것이 아니다. WebPage 클래스를 다시 살펴보자. 새로운 메서드를 추가하면 어떨까?

    class Webpage {
        private final URI uri;
        
        WebPage(URI path) {
            this.uri = path;
        }
        
        public void modify(String content){
            //HTTP PUT 요청을 보내서
            //웹 페이지 내용을 수정한다
        }            
    }                   
                                          
WebPage가 가변 객체로 바뀌었을까? 아니다. WebPage 객체를 다음과 같이 사용하면 무슨 일이 일어날까?
    
    WebPage page = new WebPage("http://localhost:8080");
    page.modify("<html/>");
page 객체의 상태가 바뀌었을까? 그렇지 않다. page는 여전히 불변객체이다. 객체가 대표하는 실제 웹페이지는 불변일까? 확신할 수 없지만 불변이 아닐 확률이 높다.

이 상황은 ImmutableList에서 경험했던 것과 매우 유사하지만 소소한 차이가 있다. ImmutableList 객체가 대표하는 실제 엔티티는 웹이 아니라 메모리에 존재한다는 점에서 다르다.
애초에 Java 언어를 다른 방식으로 설계했었다면 차이가 전혀 없었을지도 모른다. Java 언어에 Memory 클래스가 존재했다면 ImmutableList를 다음과 같이 설계 했을 것이다.

    class ImmutableList<Integer> {
        private final Memory total = new Memory(2);
        private final Memory items = new Memory(100);
        
        
        void add(Integer number) {
            int pos = this.total.read();
            this.items.store(pos, number);
            this.total.store(pos + 1);
        }    
    }

이제 ImmutableList와 WebPage가 비슷해 보일 것이다. this.total과 this.items 둘 모두 ImmutableList의 상태이다.
개념적으로 메모리, 웹페이지, 디스크는 단순히 객체가 대표하는 대상이라는 점에서 모두 동일하다.

결론적으로, 비즈니스 도메인과 기술 도메인에 무관하게 어떤 종류의 시스템이라도 전체적으로 불변객체를 이용해서 설계될 수 있고 설계되어야한다.   
       
                      
        
  