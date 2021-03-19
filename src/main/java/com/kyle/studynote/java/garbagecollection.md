garbage collection

작성 블로그 글 - https://cupeanimus.tistory.com/80

다른 블로그들을 참고하여 잡힌 개념 :
JVM의 Runtime Data Area 에서 참조를 잃은 Heap 메모리 데이터를 삭제 하는게 garbage collector의 역할.
garbage collection이 일어나는 동안에는 수행하는 쓰레드를 제외한 모든 쓰레드가 일시정지된다
(따라서 튜닝이나 프로그램을 구현할때 이를 고려하는 것으로 실력의 차이를 알 수 있다) 
garbage collection은 young 영역과 old 영역, (perm 영역)이 있으며, 
Young 영역에서 발생한 GC를 Minor GC, 나머지 두 영역에서 발생한 GC를 Major GC(Full GC)라고 한다.
Young 영역 : 새롭게 생성한 객체가 위치, 대부분의 객체가 금방 unreachable 상태가 되기 때문에 많은 객체가 Young 영역에 생성되었다가 사라진다.
Old 영역 : Young 영역에서 reachable 상태를 유지해 살아남은 객체가 여기로 복사된다. 대부분 Young 영역보다 크게 할당하며, 크기가 큰 만큼 Young 영역보다 GC는 적게 발생한다.
Perm 영역 : Method Area라고도 한다. 클래스와 메소드 정보와 같이 자바 언어 레벨에서는 거의 사용되지 않는 영역이다.


가비지 컬렉션을 학습을 기록하기 위에 쓰는 글이며, 다른 분들이 잘 정리한 글들을 적은 것이며 참조한 글들은 

아래와 같습니다. 

yaboong.github.io/java/2018/06/09/java-garbage-collection/

d2.naver.com/helloworld/1329

www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html

 

가비지 컬렉션

 - 자바가 메모리 누수현상을 방지하는 또 다른 방법이 가비지 컬렉션이다.

 - 프로그래머는 힙을 사용할 수 있는 만큼 자유롭게 사용하고, 더 이상 사용되지 않는 오브젝트들은 가비지 컬렉션을 담당하는 프로세스가 자동으로 메모리에서 제거하도록 하는 것이 가비지 컬렉션의 기본 개념이다.

 - Heap 영역의 오브젝트 중 stack 에서 도달 불가능한 (Unreachable) 오브젝트들은 가비지 컬렉션의 대상이 된다.

 

 

1.가비지 컬렉션 과정
stop-the-world : gc를 실행하기 위해 JVM이 애플리케이션 실행을 멈추는 것.
stop-the-world이 발생하면 GC를 실행하는 쓰레드를 제외한 나머지 쓰레드는 모두 작업을 멈춘다.
GC 작업을 완료한 이후에야 중단했던 작업을 다시 시작한다. 어떤 GC 알고리즘을 사용하더라도 stop-the-world는 발생한다
->즉, 대개 GC 튜닝이란 stop-the-world 시간을 줄이는 것

 

Java경우 개발자가 프로그램 코드로 메모리를 명시적으로 해제하지 않기 때문에 가비지컬렉터가 더이상 필요없는 객체를 찾아 지우는 작업을 한다. 이 가비지 컬렉터는 두 가지 가설 (전제조건)하에 만들어졌다

 - 대부분의 객체는 금방 접근 불가능 상태(unreachable)가 된다.

 - 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다.

(wek generational hypothesis)

 

이 가설의 장점을 최대한 살리기 위해 HotSpot VM에서는 크게 2개로 물리적 공간을 나누었다. 

 - Young 영역 : 새롭게 생성한 객체의 대부분이 여기에 위치한다. 대부분의 객체가 금방 접근 불가능 상태가 되기 때문에 매우 많은 객체가 Young 영역에 생성되었다가 사라진다. 이 영역에서 객체가 사라질때 Minor GC가 발생한다고 말한다.

 - Old 영역 : 접근 불가능 상태로 되지 않아 Young 영역에서 살아남은 객체가 여기로 복사된다. 대부분 Young 영역보다 크게 할당하며, 크기가 큰 만큼 Young 영역보다 GC는 적게 발생한다. 이 영역에서 객체가 사라질 때 Major GC(혹은 Full GC)가 발생한다.

영역별 데이터 흐름은 다음과 같다

가비지 컬렉션을 학습을 기록하기 위에 쓰는 글이며, 다른 분들이 잘 정리한 글들을 적은 것이며 참조한 글들은 

아래와 같습니다. 

yaboong.github.io/java/2018/06/09/java-garbage-collection/

d2.naver.com/helloworld/1329

www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html

 

가비지 컬렉션

 - 자바가 메모리 누수현상을 방지하는 또 다른 방법이 가비지 컬렉션이다.

 - 프로그래머는 힙을 사용할 수 있는 만큼 자유롭게 사용하고, 더 이상 사용되지 않는 오브젝트들은 가비지 컬렉션을 담당하는 프로세스가 자동으로 메모리에서 제거하도록 하는 것이 가비지 컬렉션의 기본 개념이다.

 - Heap 영역의 오브젝트 중 stack 에서 도달 불가능한 (Unreachable) 오브젝트들은 가비지 컬렉션의 대상이 된다.

 

 

1.가비지 컬렉션 과정
stop-the-world : gc를 실행하기 위해 JVM이 애플리케이션 실행을 멈추는 것.
stop-the-world이 발생하면 GC를 실행하는 쓰레드를 제외한 나머지 쓰레드는 모두 작업을 멈춘다.
GC 작업을 완료한 이후에야 중단했던 작업을 다시 시작한다. 어떤 GC 알고리즘을 사용하더라도 stop-the-world는 발생한다
->즉, 대개 GC 튜닝이란 stop-the-world 시간을 줄이는 것

 

Java경우 개발자가 프로그램 코드로 메모리를 명시적으로 해제하지 않기 때문에 가비지컬렉터가 더이상 필요없는 객체를 찾아 지우는 작업을 한다. 이 가비지 컬렉터는 두 가지 가설 (전제조건)하에 만들어졌다

 - 대부분의 객체는 금방 접근 불가능 상태(unreachable)가 된다.

 - 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다.

(wek generational hypothesis)

 

이 가설의 장점을 최대한 살리기 위해 HotSpot VM에서는 크게 2개로 물리적 공간을 나누었다. 

 - Young 영역 : 새롭게 생성한 객체의 대부분이 여기에 위치한다. 대부분의 객체가 금방 접근 불가능 상태가 되기 때문에 매우 많은 객체가 Young 영역에 생성되었다가 사라진다. 이 영역에서 객체가 사라질때 Minor GC가 발생한다고 말한다.

 - Old 영역 : 접근 불가능 상태로 되지 않아 Young 영역에서 살아남은 객체가 여기로 복사된다. 대부분 Young 영역보다 크게 할당하며, 크기가 큰 만큼 Young 영역보다 GC는 적게 발생한다. 이 영역에서 객체가 사라질 때 Major GC(혹은 Full GC)가 발생한다.

영역별 데이터 흐름은 다음과 같다

참고로, HotSpot VM에서는 보다 빠른 메모리 할당을 위해서 두 가지 기술을 사용한다. 하나는 bump-the-pointer라는 기술이며, 다른 하나는 TLABs(Thread-Local Allocation Buffers)라는 기술이다.

 

3.Old 영역에 대한 GC

 

Serial GC
Parallel GC
Parallel Old GC(Parallel Compacting GC)
Concurrent Mark & Sweep GC(이하 CMS)
G1(Garbage First) GC
이 중에서 운영 서버에서 절대 사용하면 안 되는 방식이 Serial GC다. Serial GC는 데스크톱의 CPU 코어가 하나만 있을 때 사용하기 위해서 만든 방식이다. Serial GC를 사용하면 애플리케이션의 성능이 많이 떨어진다.

