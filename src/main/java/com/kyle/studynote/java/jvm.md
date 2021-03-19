JVM(Java Virtual Machine) 구조

작성 블로그 글 - https://cupeanimus.tistory.com/81

다른 블로그들을 참고하여 잡힌 개념 :
JVM의 영역은 Class Loader, Execution Engine, Garbage Collector, Runtime Data Area 네가지로 나뉜다.
1. Class Loader - 말 그대로 class와 연관. java 파일을 컴파일하면 .class가 생성되고, 이러한 클래스 파일들을 엮어 메모리에 적재하는 역할

2. Execution Engine - Class Loader에 의해 메모리에 적재된 클래스들을 기계어로 변경해 명령어 단위로 실행하는 역할

3. Garbage Collector - Heap 메모리 영역에 적재된 객체들 중에 참조되지 않은 객체들을 제거하는 역할

4. Runtime Data Area - Method Area, Heap Area, Stack Area, Pc register, Native Method Stack
 - Method : 클래스 멤버 변수의 이름, 데이터 타입, 접근 제어자 정보같은 필드 정보와 메소드의 이름, 리턴 타입, 
 파라미터, 접근 제어자 정보같은 메소드 정보, Type정보(Interface인지 class인지), 
 Constant Pool(상수 풀 : 문자 상수, 타입, 필드, 객체 참조가 저장됨), static 변수, final class 변수등이 생성되는 영역이다.
 - Heap area (힙 영역) : new 키워드로 생성된 객체와 배열이 생성되는 영역이다.
                        메소드 영역에 로드된 클래스만 생성이 가능하고 Garbage Collector가 참조되지 않는 메모리를 확인하고 제거하는 영역이다.
 - Stack area (스택 영역) : 지역 변수, 파라미터 등이 생성되는 영역, 동적으로 객체를 생성하면 실제 객체는 Heap에 할당되고 해당 레퍼런스만 Stack에 저장된다. Stack은 스레드별로 독자적으로 가진다.
 - PC Register : Thread(쓰레드)가 생성될 때마다 생성되는 영역으로 Program Counter 즉, 현재 쓰레드가 실행되는 부분의 주소와 명령을 저장하고 있는 영역이다.
 - 5. Native method stack : 자바 외 언어로 작성된 네이티브 코드를 위한 메모리 영역이다.