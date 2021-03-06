6. 정보 가져오

6.1 질의 메서드를 사용해 정보를 가져온다
명령 메서드는 반환타입이 void이고 상태 변경, 메일 전송, 파일 저장 등 부수 효과를 만들어 내는데 사용할 수 있다.
(->메일전송이나 파일 저장 후 응답을 받아 정상인지 알고싶을 수 있는데 이건 이 메소드 내에서 에러처리를 통해 체크하는건가 싶다)
정보를 가져오는데는 질의 메서드를 통해 한다.

-메서드는 항상 명령 또는 질의 메서드여야 한다는 규칙을 따른다.

6.2 질의 메서드의 반환 값은 단일 타입이어야 한다
-> null 사용금지

6.3 초기 상태를 노출하는 질의 메서드는 피한다
-객체는 내부 정보를 숨기는 게 더 낫다.
-getItems()나 countItems()와 같은 이름은 사용하지 않는 것이 좋다 <- 객체에 무언가 하라는 명령처럼 들리기 때문
평소 getItems와 같은 메서드를 통해 가공된 값을 받아 처리하였는데 그렇다면 어떤 이름이 좋을까?
itemCount?

질의형과 명령형에 대해 반복되는 것은
-질의형 함수 : 요청한 값을 리턴만 한다
-명령형 함수 : 상태만 바꾼다

6.4 원하는 질의에 대한 특정 메서드와 반환 타입을 정의한다
USD/EUR의 오늘 환율이 필요할 때, https://fixer.io에 호출하는 api 메서드, ExchangeRate로 답을 나타내는 메서드로 나눠서 작업 할 수 있다
요구, 질문, 반환 되는 답을 명확히 알 수 있도록 구조를 설계한다.

6.5 시스템 경계를 넘는 질의에는 추상화를 정의한다
'현재 환율은 얼마인가?'라는 질문은 메모리에 있는 것을 바탕으로 응용 프로그램 스스로 답할 수 없는 질문이다.
이때는 시스템 경계를 넘어, 네트워크를 통해 도달할 수 있는 원격 서비스에 연결해야 한다.
응용 프로그램이 시스템 경계를 넘으면, 이면에서 벌어지는 해당 호출의 저수준 통신 상세 내용을 숨길 수 있게 추상화를 도입해야 한다.
추상화 :
- 서비스 클래스 대신 서비스 인터페이스를 사용
- 세부 구현을 생략

환율 조회경우
HttpClient 인터페이스를 추가하고 FixerApi에서 사용
-> 다른 Http 클라이언트 구현으러 전활할때 유리
또는 ExchangeRate 인터페이스를 FixerApi 클래스에서 구현
->다른 환율 제공자로 쉽게 전환 가능
->CurrencyConverter용 단위 테스트를 작성하고 인터넷 연결 없이 테스트 대역을 주입할 수 있다.

이것은 SOLID 원칙에서 의존성 역전 원칙으로 알려진, 서비스 의존성 추상화와 비슷한 규칙이라 할 수 있다.

의존성 역전 원칙 : 자신보다 변화기 쉬운 것에 의존하지 마라
ex) 자동차가 특정 타이어(스노우 타이어)에 의존한다면 계절이 바뀔때마다 자동차는 영향에 노출되어 있다. 
이를 자동차가 타이어 인터페이스를 의존하게 변경한다면 그 영향을 받지 않게 된다.

    객체지향 5대 원칙 - SOLID 원칙 : SRP(단윌 책임 원칙), OCP(개방-폐쇄 원칙), LSP(리스코프 치환 원칙), DIP(의존 역전 원칙), ISP(인터페이스 분리 원칙)
    
    1. Single Responsibility Principle(단일 책임 원칙)
    - 소프트웨어의 설계 부품(클래스, 함수 등 -> 객체)은 단 하나의 책임만을 가져야 한다.
    
    2. Open-Closed Principle(개방-폐쇄 원칙)
    - 기존의 코드를 변경하지 않고(Closed) 기능을 수정하거나 추가할 수 있도록(Open) 설계해야 한다.
    
    3.Liskov Substitution Principle(리스코프 치환 원칙)
    - 자식 클래스는 부모클래스에서 가능한 행위를 수행할 수 있어야 한다.
    -> 부모 클래스와 자식 클래스 사이의 행위에는 일관성이 있어야 한다는 원칙
    -> 부모 클래스의 인스턴스 대신 자식 클래스의 인스턴스를 사용해도 문제가 없어야 한다
    
    4. Dependency Inversion Principle(의존 역전 원칙)
    - 의존 관계를 맺을 때, 변화하기 쉬운 것보단 변화하기 어려운 것에 의존해야 한다
    
    5. Interface Segregation Principle(인터페이스 분리 원칙)
    - 한 클래스는 자신이 사용하지 않는 인터페이스는 구현하지 말아야 한다. 하나의 일반적인 인터페이스보다는, 여러개의 구체적인 인터페이스가 낫다
    -> 인터페이스에는 모두 사용되는 것끼리 묶여 있어야 한다

6.6 질의 메서드에 테스트 대역용 스텁을 사용한다
- 주입한 서비스의 답을 알 고 있기 때문에 예측 가능한 방법으로 해당 메서드의 논리를 테스트 할 수 있다
-> 여기에 관한 의문은 이 부분을 과연 그럼 테스트해야할까? 생성하고 구현하는데 있어 오류가 발생할 부분이 있을까?
 마치 1+1 = 2, 5 * 5 = 25와 같은 일차원적인 부분을 검산하는 기분이 든다.
 
6.7 질의 메서드는 명령 메서드가 아니라 다른 질의 메서드를 사용해야 한다
-> 응답이 필요하기 때문에 또 다른 질의 메서드를 통해 답을 가져온다.

웹 응용프로그램에서 새 객체를 등록할 때를 생각해보면 결과(200)를 반환해야하지만 등록하는 것은 명령에 해당한다.
이런경우 사용자 등록과 반환 부분을 나눠서 처리할 수 있다
->하지만 이부분이 과연 나은것인가? jpa로 등록할 경우 id를 반환하는데 우리는 명령메서드로 저장하고, 다시 질의메서드를 통해 가져와야할까?
원칙은 중요하지만 프로그래머는 유연해야한다고 생각한다. 그렇다면 정답에 가까운 것은 무엇일까?
다행히 책에서도 벗어 날 수 없는 규칙이 돼서는 안된다고 강조한다.


질의 메서드의 이름은 과연 어떻게 지어야 할까? 어제부터 이 것에 대해 많은 생각을 해봤고 검색을 해봤지만 마땅한 답은 없다.
이 책에서의 몇 가지 되지 않는(itemcount()) 것을 통해 유추하자면
메서드 이름은 이것은? ...이다. 와같이 이것은? 이라는 질문의 답을 지으면 되는 것 같다.
 







