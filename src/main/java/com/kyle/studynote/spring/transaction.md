https://cupeanimus.tistory.com/83

다수의 트랜잭션이 경쟁시 발생할 수 있는 문제
 

다수의 트랜잭션이 동시에 실행되는 상황에선 트랜잭션 처리방식을 좀 더 고려해야 한다.

예를들어 특정 트랜잭션이 처리중이고 아직 커밋되지 않았는데 다른 트랜잭션이 그 레코드에 접근한 경우 다음과 같은 문제가 발생할 수 있다.

▶ Problem1 - Dirty Read

 

 - 트랜잭션 A가 어떤 값을 1에서 2로 변경하고 아직 커밋하지 않은 상황에서 트랜잭션B가 같은 값을 읽는 경우 트랜잭션 B는 2가 조회 된다.

 

 - 트랜잭션 B가 2를 조회 한 후 혹시 A가 롤백된면 결국 트랜잭션B는 잘못된 값을 읽게 된 것이다. 즉, 아직 트랜잭션이 완료되지 않은 상황에서 데이터에 접근을 허용할 경우 발생할 수 있는 데이터 불일치이다.

 

 

▶ Problem2 - Non-Repeatable Read

 

 - 트랜잭션 A가 어떤 값 1을 읽었다. 이후 A는 같은 쿼리를 또 실행할 예정인데, 그 사이에 트랜잭션 B가 값 1을 2로 바꾸고 커밋해버리면 A가 같은 쿼리 두번을 날리는 사이 두 쿼리의 결과가 다르게 되어 버린다.

 

 - 즉, 한 트랜잭션에서 같은 쿼리를 두번 실행했을 때 발생할 수 있는 데이터 불일치이다.

Dirty Read에 비해서는 발생 확률이 적다.

 

 

▶ Problem3 - Phantom Read

 

 - 트랜잭션 A가 어떤 조건을 사용하여 특정 범위의 값들[0,1,2,3,4]을 읽었다.

이후 A는 같은 쿼리를 실행할 예정인데, 그 사이에 트랜잭션 B가 같은 테이블에 값[5,6,7]을 추가해버리면 A가 같은 쿼리 두번을 날리는 사이 두 쿼리의 결과가 다르게 되어 버린다.

 

 - 즉, 한 트랜잭션에서 일정 범위의 레코드를 두번 이상 읽을 때 발생하는 데이터 불일치이다.

 

 

스프링 트랜잭션 속성
위와 같은 상황 (다수의 트랜잭션이 경쟁시) 을 방지할 수 있는 속성, 이외 별도 속성들을 확인해보자.

 

 

[ 1. isolation (격리수준) ]

 

 - 트랜잭션에서 일관성이 없는 데이터를 허용하도록 하는 수준을 말한다. 

 

▶ DEFAULT

기본 격리 수준(기본설정, DB의 Isolation Level을 따름)

 

 

▶ READ_UNCOMMITTED (level 0)

 - 커밋되지 않는(트랜잭션 처리중인) 데이터에 대한 읽기를 허용

 

 - 즉 어떤 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 B라는 아직 완료되지 않은(Uncommitted 혹은 Dirty) 데이터 B를 읽을 수 있다.

 

 - Problem1 - Dirty Read 발생

 

우리 개발 환경에서는 해당 Transaction 레벨을 허용하지 않아
[ READ_COMMITTED와 SERIALIZABLE만이 적합한 트랜잭션 레벨입니다 ] 라는 오류가 발생한다. 다른 옵션들은 문제 발생하지 않음.

 

 

▶ READ_COMMITTED (level 1)

 

 - 트랜잭션이 커밋 된 확정 데이터만 읽기 허용

 

 - 어떠한 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 해당 데이터에 접근할 수 없다.

 

 - Problem1 - Dirty Read 방지

 

 

▶ REPEATABLE_READ (level 2)

 

 - 트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정이 불가능하다.

 

 - 선행 트랜잭션이 읽은 데이터는 트랜잭션이 종료될 때까지 후행 트랜잭션이 갱신하거나 삭제가 불가능 하기때문에 같은 데이터를 두 번 쿼리했을 때 일관성 있는 결과를 리턴한다.

 

 - Problem2 - Non-Repeatable Read 방지

 

 

▶ SERIALIZABLE (level 3)

 

 - 데이터의 일관성 및 동시성을 위해 MVCC(Multi Version Concurrency Control)을 사용하지 않음

(MVCC는 다중 사용자 데이터베이스 성능을 위한 기술로 데이터 조회 시 LOCK을 사용하지 않고 데이터의 버전을 관리해 데이터의 일관성 및 동시성을 높이는 기술)

 - 트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정 및 입력이 불가능하다.

 

 - Problem3 - Phantom Read 방지

   (※ 격리 수준이 올라갈 수록 성능 저하의 우려가 있음)

 

 

사용 예)

@Transactional(isolation=Isolation.DEFAULT) public void something (int a) { … }


트랜잭션 전파 설정
REQUIRED, SUPPORTS, REQUIRES_NEW, MANDATORY, REQUIRES_NEW, NOT_SUPPORTED, NEVER, NESTED

 

트랜잭션 내부에서 트랜잭션을 호출할 때 상위 클래스에서 선언된 트랜잭션에  포함될 것인지 독립적으로 할 것인지와같이 어떻게 처리할 지 정하는 것

전파 설정 옵션

트랜잭션의 전파 설정은 '@Transactional'의 옵션 'propagation'을 통해 설정할 수 있습니다. 각 옵션은 아래와 같습니다.

 

 

REQUIRED (기본값)

부모 트랜잭션이 존재한다면 부모 트랜잭션으로 합류합니다. 부모 트랜잭션이 없다면 새로운 트랜잭션을 생성합니다.

중간에 롤백이 발생한다면 모두 하나의 트랜잭션이기 때문에 진행사항이 모두 롤백됩니다.

 

REQUIRES_NEW

무조건 새로운 트랜잭션을 생성합니다. 각각의 트랜잭션이 롤백되더라도 서로 영향을 주지 않습니다.

MANDATORY
 
부모 트랜잭션에 합류합니다. 만약 부모 트랜잭션이 없다면 예외를 발생시킵니다.

NESTED

부모 트랜잭션이 존재한다면 중첩 트랜잭션을 생성합니다. 중첩된 트랜잭션 내부에서 롤백 발생시 해당 중첩 트랜잭션의 시작 지점 까지만 롤백됩니다. 중첩 트랜잭션은 부모 트랜잭션이 커밋될 때 같이 커밋됩니다.

부모 트랜잭션이 존재하지 않는다면 새로운 트랜잭션을 생성합니다.

SUPPORTS

부모 트랜잭션이 있다면 합류합니다. 진행중인 부모 트랜잭션이 없다면 트랜잭션을 생성하지 않습니다.

 

NOT_SUPPORTED

부모 트랜잭션이 있다면 보류시킵니다. 진행중인 부모 트랜잭션이 없다면 트랜잭션을 생성하지 않습니다.
 
 