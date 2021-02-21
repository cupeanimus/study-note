4.1 절대 NULL을 반환하지 말자.

    public String tile() {
        if (/* title이 없다면 */) {
            return null;
        }
        return "Elegant Object";
    }

    String tile = x.tile();
    print(title.length);
와 같다면 title.length를 호출할때마다 항상 NullPointException 예외가 던져질지 모른다는 사실에 불안할 수 밖에 없다. 
여기서 문제는 예외가 아니라 객체에 대한 신뢰가 무너졌다는 사실이다. 반환된 값이 객체인지부터 확인해야 하기 때문에, 객체에게 작업을 요청한 후 안심하고 결과에 의지할 수 없다.
객체라는 사상에는 우리가신뢰하는 엔티티라는 개념이 담겨져 있다. 객체는 우리의 의도에 관해 전혀 알지 못하는 데이터 조각이 아니다. 쉽게 접근할 수 있는 메모리 공간과 유용한 서브루틴만을 제공하는 단순한 데이터 조각이 아니고,
한 위치에서 다른 위치로 보내는 표식도 아니고, 데이터를 담는 봉투도 절대 아니다.
객체는 자신만의 생명주기, 자신만의 행동, 자신만의 상태를 가지는 살아있는 유기체다.

    if(title == null) {
        print("Can't print; it's not a tile)"
        return;
    }
이와 같이 반환값을 검사하는 것은 애플리케이션에 대한 신뢰가 부족하다는 신호다.
NULL을 사용하면 소프트웨어에 대한 신뢰가 손상되고 소프트웨어가 유지보수 불가능할정도로 엉망이 된다.

4.1.1 빠르게 실패하기 vs 안전하게 실패하기
소프트웨어 견고성과 실패 탄력회복성 관련해서 상반되는 두가지 철학이 존재한다.
안전하게 실패하기는 버그, 입출력 문제, 메모리 오버플로우 등이 발생한 상황에서도 소프트웨어가 계속 실행될 수 있도록 최대한 많은 노력을 기울일 것을 권장한다.
어떤 상황이 닥치더라도 소프트웨어는 생존하기 위해 노력해야 한다. NULL을 반환하는 방법도 일종의 생존 기법이라고 할 수 있다. 
안전하게 실패하기는 요청자의 잘못도 구조하기 위해 노력한다.

빠르게 실패하기는 정반대의 접근 방식을 따른다. 일반 문제가 발생하면 곧바로 실행을 중단하고 최대한 빨리 예외를 던진다. 결과에 대해서는 걱정하지 않는다.
만약 소프트웨어가 부서지기 쉽고 모든 단일 제어 지점에서 중단되도록 설계됐다면, 단위 테스트에서 실패 상황을 손쉽게 재현할 수 있을 것이다. 따라서 문제를 간단하게 수정할 수 있다.
프러덕션 환경에서 실패한다고 해도, 모든 실패 지점이 명확하고 훌륭하게 문서화되어 있기 때문에 상황을 재생하는 테스트를 쉽게 추가할 수 있다. 실패를 감추는 대신 강조한다.
실패를 눈에 잘 띄게 만들고 추적하기 쉽게 만든다. 

어떤 방법이 나을까? 필자는 빠르게 실패하기의 지지자고 생각해보면 나는 안전하게 실패하기에 가까운 코딩을 하였다. (이미 주어진 환경에서 서비스를 개발하였기 때문에 설사 요청자의 잘못이 있더라도 이를 수정하는 것보다 구조하는게 빠르고 편리하였기 때문이다
하지만 나 역시 빠르게 실패하기의 지지자이다.)
빠르게 문제를 찾을수록 더 빠르게 실패하고, 결과적으로 전체적인 품질이 향상된다.

4.1.2 NULL의 대안

    public User user(String name) {
        if( /* 데이터베이스에서 발견하지 못했다면 */){
            return null;
        }
        return /* 데이터베이스로부터 */;
    }        
이 사례가 실제 객체 대신 NULL을 반환하는 가장 흔한 경우이다. 

하지만 우리는 NULL을 대체하는 방법중 하나를 선택해야한다.

첫번째, 메서드를 두 개로 나눈다. 첫 번째 메서드는 객체의 존재를 확인하고, 두 번째 메서드는 객체를 반환한다. 아무 것도 찾지 못한 경우 두번째 메서드는 예외를 던진다.
    
    public boolean exists(String name) {
        if( /* 데이터베이스에서 발견하지 못했다면 */){
            return false;
        }
        return true;
    }
    public User user(String name) {
        return /* 데이터베이스로부터 */;
    }                

이 방법은 데이터베이스에 요청을 두 번 전송하기 때문에 비효율적이라는 단점이 있다.(비효율적인 이 방법을 택해야한다고 생각하는게 쉽지 않다)
이런 단점을 보완할 수 있는 두번째 방법은 NULL을 반환하거나 예외를 던지는 대신 객체 컬렉션을 반환하는 것이다.
    
    public Collection<User> users(String name) {
        if( /* 데이터베이스에서 발견하지 못했다면 */){
            return new ArrayList<>(0);
        }
        return Collections.sigleton(/* 데이터베이스로부터 */);
    }
여기서는 사용자를 발견하지 못한 경우 빈 컬렉션을 반환한다. 기술적으로는 NULL과 크게 다르지 않지만 더 깔끔하다. 
java.util.Optional이나 유사한 도그를 사용하는 방법도 있다. java.util.Optional은 컬렉션과 동일하지만, 오직 하나의 요소만을 포함할 수 있다.
이 방법은 의미론적으로 부정확하기 때문에 OOP와 대립한다고 생각하며 사용을 권하지 않는다(User만 찾는다면??). 메서드 이름은 user()지만 실제로 반환하는 객체는
사용자가 아니라 사용자를 포함하는 일종의 봉투이다. 이 방법은 오해의 여지가 있으며 객체지향적인 사고방식과도 거리가 멀다. 

마지막 방법은 널 객체 디자인 패턴이다. 널 객체 패턴에서는 원하는 객체를 발견하지 못할 경우, 겉으로 보기에는 원래의 객체처럼 보이지만 실제로는 다르게 행동하는 객체를 반환한다.
널 객체는 일부 작업은 정상적으로 처리하지만, 나머지 작업은 처리하지 않는다. 이 방법은 객체지향적인 사고방식과도 잘 어울리지만, 제한된 상황에서만 사용 가능하다는 단점이 있다.
요약하자면 절대로 NULL을 반환하지 말아야한다.

보기들 중 두번째 Collection이 가장 나아보이는데 User 관련 API에서 아이디로 단일 유저만 찾거나, 조건으로 전체 유저를 찾을 수 있다. 
(두 방법 모두를 하나의 users()로 찾을 수 있을까? 혹은 찾아야하는가? 이 것에 좀 더 고민해 볼 필요가 있을 것 같다)


4.2 체크 예외만 던지자.
비록 대부분의 객체지향 언어들은 언체크 예외만을 제공하지만 Java는 두 종류의 예외를 모두 제공한다. 이 섹션을 요약하면, 
언체크 예외를 사용하는 것은 실수이며, 모든 예외는 체크 예외여야 한다. 또한 다양한 예외 타입을 만드는 것도 좋지 않은 생각이다.

체크 예외와 언체크 예외의 차이점과 예외 '타입'의 용도에 관해 살펴보자. 
Java에서 체크 예외를 사용하는 경우이다.

    public byte[] content(File file) throws IOException {
        byte[] array = new byte[1000];
        new FileInputStream(file).read(array);
        return array;
    }

먼저 메서드의 시그니처가 throws IOException으로 종료된다는 사실에 주목하자. 이것은 무슨 일이 있어도 content()를 호출하는 쪽에서 IOException 예외를 잡아야 한다는 것을 의미한다.
    
    //체크 예외
    public int length(File file) {
        try {
            return content(file).length();
        } catch (IOException ex) {
            //이 예외에 대해 어떤 처리를 해야 하며
            //바로 여기에서 예외를 해결하거나
            //더 상위 레벨로 전달해야 한다
        }
    }


    //더 이상 예외를 잡지 않는다. 예외를 더 높은 레벨로 확대시킨다
    public int length(File file) throws IOException {
        return content(file).length();
    }
    }                         
IOException은 catch 구문을 이용해서 반드시 잡아야 하기 때문에 체크 예외에 속한다.
대조적으로 언체크 예외는 무시할 수 있으며 예외를 잡지 않아도 무방하다. 일단 언체크 예외를 던지면, 누군가 예외를 잡기 전까지 자동으로 상위로 전파된다.    
    
    //언체크예외
    public int length(File file) throws IOException {
        if(!file.exists()) {
            throw new IllegalArgumentException(
                "File doesn't exist; I can't count its length."
            );
        }
        return content(file).length();

     

4.2.1 꼭 필요한 경우가 아니라면 예외를 잡지 말자.

4.2.2 항상 예외를 체이닝하자
예외 던지기의 올바른 예
    
    public int length(File file) throws IOException {
        try {
            return content(file).length();
        catch (IOException ex) {
            throw new Exception("길이를 계산할 수 없다.", ex);
        }
    }

예제에서는 예외를 잡은 즉시 새로운 예외를 던진다. 
원래의 문제를 새로운 문제로 대체함으로써 문제가 발생했다는 사실을 무지하지 않는다. 대신 원래의 문제를 새로운 문제로 감싸서 함께 상위로 던진다.

    public int length(File file) throws IOException {
        try {
            return content(file).length();
        catch (IOException ex) {
            //여기에서 문제'ex'를 무시하고,
            //새로운 메세지를 가지는 새로운 타입의 새로운 문제를 생성한다.
            throw new Exception("계산할 수 없다.");
        }
    }
문제를 발생시킨 근본 원인에 관한 매우 가치있는 정보가 손실되기 때문에 매우 나쁜 프랙티스이다.

다시 한번 강조하지만, 항상 예외를 체이닝하고 절대로 원래 예외를 무시하지 말자.

4.2.3 단 한번만 복구하자.
항상 예외를 잡고, 체이닝하고, 다시 던지자. 가장 최상위 수준에서 오직 한번만 복구하자.

4.2.4 관점-지향 프로그래밍(AOP)을 하자.
AOP는 단순하면서 강력한 프로그래밍 패러다임으로 OOP와 잘 맞는다.

    public String content() throws IOException {
        int attmpt = 0;
        while (true) {
            try {
                return http();
            } catch (IOException ex) {
                if (attempt >= 2) {
                    throw ex;
                }
            }
        }                
    }

    ->
    @RetryOnFailure(attempts =3)
    public String content() throws IOException {
        return http();
    }
컴파일러는 컴파일 시점에 @RetryOnFailure 애노테이션을 발견한 후 content() 메서드를 '실패 재시도' 코드로 둘러싼다. 
이 '실패 재시도' 코드 블록을 '관점(aspect)'이라고 부른다.
기술적으로 관점이란 제어를 위임받아 content()를 언제, 어떻게 호출할지 결정하는 객체를 의미한다.

4.2.5 하나의 예외 타입만으로도 충분하다.
단 한번만 복구한다면 어떤 예외라도 담을 수 있는 예외 객체만 있으면 된다. 올바르게 예외를 체이닝했다면 예외의 타입을 알아야 할 필요가 없다.
                                                         
        