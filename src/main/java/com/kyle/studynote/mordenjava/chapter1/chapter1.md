1. java8 새로운 기술
 - 스트림 API
 - 메서드에 코드를 전달하는 기법
 - 인터페이스의 디폴트 메서드

1.2.1 스트림 처리
 스트림 : 한 번에 한 개씩 만들어지는 연속적인 데이터 항목들의 모임.
 핵심 : 기존에는 한 번에 한 항목을 처리했지만 자바 8에서는 하려는 작업을 고수준으로 추상화해서 일련의 스트림으로 만들어 처리 할 수 있다.
        또한, 스트림 파이프라인을 이용해서 입력 부분을 여러 CPU 코어에 쉽게 할당할 수 있다는 부가적인 이득도 얻을 수 있다.
        (스레드라는 복잡한 작업을 사용하지 않고서도 공짜로 병렬성을 얻을 수 있다)  
1.2.2 동작 파라미터화로 메서드에 코드 전달하기
 동작 파라미터화 : 메서드를 다른 메서드의 인수로 넘겨주는 기능을 제공
1.2.3 병렬성과 공유 가변 데이터

1.3 자바 함수
 일급값(시민) - 프로그램을 실행하는 동안 자유롭게 전달할 수 있는 값(int, double과 같은 기본 값, (참조된) 객체)
 이급값(시민) - 자유롭게 전달할 수 없는 값(보통 메서드, 클래스)

1.3.1 함수형을 통해 메서드와 람다를 일급 시민으로 바꿀 수 있게 되었다 (메서드를 값으로 취급)

   File[] hiddenFiles = new File(".").listFiles(new FileFilter()) {
        public boolean accept(File file) {
            return file.isHidden();
        }
   });         
  
  
  -->
  File[] hiddenFiles = new File(".").listFiles(File::isHidden);

 메소드 참조 ::(이 메서드를 값으로 사용하라는 의미)
  
람다 : 익명함수
 메서드를 일급값으로 취급할 뿐만 아니라 람다(익명함수)를 포함하여 함수도 값으로 취급할 수 있다.

프레디케이트(predicate) : 인수로 값을 받아 true나 false로 반환하는 함수

1.4 스트림
1.4.1 멀티스레딩은 어렵다
 
    순차처리 방식 코드
    List<Apple> heavyApples = inventory.stream().filter((Apple a) -> a.getweight() > 150)
                                                .collect(toList());
    병렬처리 방식 코드
    List<Apple> heavyApples = inventory.parallelStream().filter((Apple a) -> a.getweight() > 150)
                                                .collect(toList());    

1.6 함수형 프로그래밍에서 가져온 다른 유용한 아이디어
 함수형 프로그래밍의 핵심 : 1. 메서드와 람다를 일급값으로 사용
                      2. 가변 공유 상태가 없는 병렬 실행을 이용해서 효율적이고 안전하게 함수나 메서드를 호출할 수 있다.
 Optional<T> 클래스를 통해 NullPointer 예외를 피할 수 있도록 한다.                                                                        
 
     
 
         