5. 객체 사용하기

5.1 메서드를 구현하는 템플릿
메서드를 디자인 할때 탬플릿
    
    [유효범위] function methodName(type name, ...) void|[반환-타입]
    {
        [사전 조건 확인]
        
        [실패 시나리오]
        
        [행복한 경로]
        
        [사후 조건 확인]
        
        [반환 void | 특정-반환-타입]
    }
5.1.1 사전 조건 확인
-제공한 인자가 올바른지 확인
-단언 함수 사용 -> 생성자로 옮겨 더이상 유효성 체크가 필요없게 할 수 있다

5.1.2 실패 시나리오
-메서드 자체가 실패 시나리오로 인식할 수 없는 시나리오

5.1.3 행복한 경로
-잘못된 것 없이 해당 작업을 수행하는 부분
-때때로 코드 대부분은 실패 시나리오를 처리하기 위한 것

5.1.4 사후 조건 확인
-메서드가 하기로 한 일을 제대로 수행했는지 확인
-사후 조건 확인은 안전점검으로, 절대로 일어나서는 안된다

5.1.5 반환 값
-좋은 규칙은 빨리 반환 하는 것. 반환할 것을 아는 즉시 if절 몇 개를 건너뛴 다음 반환하는게 아닌 바로 반환한다.

5.2 예외의 몇 가지 규칙

5.2.1 사용자 정의 예외 클래스는 필요할 때만 사용한다
사용자 정의 예외 클래스를 추가하는 것이 유용한 경우

    1. 특정 예외 타입을 더 높여 붙잡고 싶을때
    try {
    
    } catch ( SomeSpecific exception) {
    
    }

    2.단일 예외 타입을 인스턴스화 하는 방법이 여럿일 때
    final class CouldNotDeliverOrder extends RuntimeException{
        
        public static function itWasAlreadyDelivered(): CouldNotDeliverOrder
        {
        
        }
        public static function insufficientQuantitiesInStock(): CouldNotDeliverOrder
        {
                
        }
    }
    
    3.명명한 생성자를 사용해 예외를 인스턴화 하고 싶을때
    final class CouldNotFindProduct extends RuntimeException
    {
        public static function withId(
            ProductId productId
        ): CouldNotFindProduct {
            return new CouldNotFindProduct(
                'could not find a product with Id "{productId}"
            );
        }        
    }
    
명명 한 생성자를 사용하면 클라이언트 쪽 코드를 더욱 명확하게 할 수 있다.
결론은 디버깅 하기 쉽도록 에러를 명확하게 파악할 수 있도록 하는 것

5.2.5 유효하지 않은 인자나 논리 예외 클래스 명명하기
-예외 클래스 이름에 'Exception'을 넣을 필요는 없다.
-유효하지 않은 인자나 논리 오류를 나타낼 때 'InvalidEmailAddress', 'InvalidTargetPosition'과 같이 'Invalid...'를 사용한다

5.2.3 실행 중 예외 클래스 명명하기
-죄송하지만...(Sorry, I...)으로 시작하는 문장을 완성하는 것이다. 뒷부분에 예외 클래스 이름을 넣으면 된다. 
수행하려 했으나 성공적으로 마칠 수 없게 된 사정을 전달하면 된다. CouldNotFindProduct, CouldNotStoreFile 등이 있다.

5.2.4 명명한 생성자를 사용해 실패 이유를 나타낸다
-사용한 데이터를 받아 보여줄 수 있다.
-메서드 이름을 통해 실패 사유를 보여줄 수 있다

5.2.5 상세한 메시지를 추가한다
-명명한 생성자는 클라이언트에 유용하다. 예외의 생성자에서 해당 예외의 메세지를 설정하여 전달 할 수 있다.
   
        