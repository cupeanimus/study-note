
2.6 불변객체로 만드세요

    //가변
    class Cash {
        private int dollars;
    
        public void setDollars(int val) {
            this.dollars = val;
        }
    }
    
    //불변
    class Cash {
        private final int dollars;
    
        Cash(int val) {
            this.dollars = val;
        }
    }

곱하는 연산자를 추가한다면

    class Cash {
        private int dollars;
    
        public void mul(int factor) {
            this.dollars *= factor;
        }
    }
    
    class Cash {
        private final int dollars;
        public Cash mul(int factor) {
            return new Cash(this.dollars * factor);
        }
    }

불변객체는 새로운 클래스를 생성하여 반환한다.   이게 나은건가??

    //가변객체 사용법
    Cash five = new Cash(5);
    five.mul(10);
    System.out.println(five);   //"$50" 이 출력된다.
    
    //불변객체
    Cash five = new Cash(5);
    Cash fifty = five.mul(10);
    System.out.println(fifty);   //"$50" 이 출력된다.

현재 개발하고 있는 프로젝트에서 불변객체를 사용한다면 프로젝트는 더 복잡해질 것이다. 유지보수까지 고려한다면 더 나은것인가?
개발자는 프로젝트 구조를 처음 접하는 것도 아니고, 파악하는데 시간을 지속적으로 들이는 것도 아니다. 이해의 범주를 생각한다면 팀을 위한 좋은 코드는 무엇인가?

작가의 말은 가변객체 five가 최종적으로 50$를 리턴하고 있고, 이는 혼란스러운 모습이다. money라고 변경한다고 하면 이처럼 간단한 경우라 적용할 수 있다고 한다.
그렇다면 간단한 경우는 가변 사용, 복잡하여 명확하게 할 필요가 있을때 불변을 사용하는 것은 어떤가? 통일되지 않고, 불변, 가변을 나누는 기준이 명확하지 않아서 아예 사용하지 않아야 되는 것인가?

불변 객체가 적합하지 않다고 말하는 예제를 보자

    class Page {
        private final String uri;
        private String html;
        Page(String address) {
            this.uri = address;
            this.html = null;
        }
        public String content() {
            if (this.html == null) {
                this.html = /*네트워크로부터 로드한다 */
            }
            return this.html;
        }
    }
이 예제는 지연 로딩이 작동하는 방식을 잘 보여주고 있다.
객체를 생성하는 시점에 this.html 값은 없다. content()를 호출할 때 저장되기 때문이다.
이를 불변객체로 만드는 것은 java에서 불가능하다.

하지만 다른 방법으로 지연 로딩을 구현할 수 있다. 그리고 프로그래머는 이를 가능해야한다고 말하고 있다. 나라면 어떻게 짰을까?

    @OnlyOnce
    public String content() {
        return  /*네트워크로부터 로드한다 */
    }

null인 시점 한번, 즉 지연로드를 해야만 하는 부분을 떨어뜨려서 구현하였다.

2.6.1 식별자 가변성

    Map<Cash, String> map = new HashMap<>();
    Cash five = new Cash("$5");
    Cash ten = new Cash("$10");
    map.put(five, "five");
    map.put(ten, "ten");
    five.mul(2);
    System.out.println(map);  //{ $10 => "five", $10 => "ten"}

위와 같을때 five, ten을 찾는게 혼란스러워지고, 찾기 어려운 버그로 이어질 수 있다.
명확한 값을 위한 불변 객체의 중요성는 어느정도 이해가 되고 와닿고 있다.
보통 키 값을 static하게 하지 않는가? 키 밸류가 반대로 주로 사용하는 것과 반대로 쓰이는 데 차이는 뭘까?


2.6.2 실패 원자성
완전하고 견고한 상태의 객체를 가지거나 실패하거나 둘 중 하나만 가능한 특성

    class Cash {
        private int dollars;
        private int cents;
        public void mul(int factor) {
            this.dollars *= factor;
            if (/*뭔가 잘못 됐다면*/) {
                throw new RuntimeException("opps...");
            }
            this.cents *= factor;
        }
    }
    
    //위 상황은 중간 에러발생시 dollars만 상태 변환되어, 매우 발견하기 힘든 버그가 발생할 수 있다.
    class Cash {
        private final int dollars;
        private final int cents;
        public void mul(int factor) {
            if (/*뭔가 잘못 됐다면*/) {
                throw new RuntimeException("opps...");
            }
            return new Cash(
                    this.dollars *= factor;
                    this.cents *= factor;
            );
        }
    }
    
불변 객체는 원자적이기 때문에 원자성을 걱정할 필요가 없다
모드 성공하거나 실패하는경우
예를 보면 if 예외상황이 아래와 마찬가지로 우선하고 후에 같이 상태를 변화하는것과는 어떤 차이가 있을까?

2.6.3 시간적 결합

    Cash price = new Cash();
    price.setDollars(29);
    price.setCents(95);
    System.out.println(price); //"$29.95"
    
    Cash price = new Cash();
    price.setDollars(29);
    System.out.println(price);
    price.setCents(95);

아래와 같이 순서가 바껴도 실행된다. 유지보수를 고려하였을때 좋은 코드는 아니라고 한다.(하지만 이런 소스는 현재 작업중인 프로젝트에도 많이 보이고있다. 코딩할 때 이점을 신경써야겠다.

    Cash price = new Cash(29,95);
    System.out.println(price); //"$29.95"

위와같이 불변객체를 이용하여 문제를 해결 할 수 있다.

2.6.4 부수효과 제거

    void print(Cash price) {
            System.out.println("Today price is: "+price);
            price.mul(2);
            System.out.println(
                    "Buy now, tomorrow price is: "+price
            );
    }
    
    Cash five = new Cash(5);
    print(five);
    System.out.println(price); //"$10"
//위와 같을때 이를 찾으려면 많은 시간과 수고가 예상된다.
//반면 Cash 클래스가 불변이라면 이를 의심하지 않고, 이를 살펴볼 필요가 없다.

2.6.5 null 참조 없애기

    class User{
        private final int id;
        private String name = null;
        public User(int num) {
            this.id = num;
        }
        public void setName(String txt) {
            this.name = txt;
        }
    
    }

여기서 name은 set하기 전까지 null을 유지하게 된다.
안전성을 위해 이 부분을 if name != null로 항상 체크해야할 수 있다. 이를 놓쳤다면 세그먼테이션 오류와 마주하게 될 것이다.
또한 null을 참조하는 객체는 유지보수성이 저하될 수 밖에 없다.

이러한 경우는 다른 클래스가 필요하지만 새로운 클래스를 만들기 귀찮을때 발생한다. 재사용

2.6.6 스레드 안정성
스레드에 안전하지 않은 인스턴스를 생성한 클래스

    class Cash {
        private final int dollars;
        private final int cents;
        public void mul(int factor) {
            this.dollars *= factor;
            this.cents *= factor;
    
        }
    }

이상없어 보이지만 두 개의 병렬 스레드 안에서 이 객체를 실행하면?
두 스레드 안에서 다음 두 줄을 실행
"$30.20"와 "$60.40"이 예상됨

    Cash price = new Cash("15.10");
    price.mul(2);
    System.out.println(price);

하지만 실행해보면 매번 $60.20과 같은 다른 숫자가 출력된다.
이유는 달러에 2를 곱한상태에서 미처 cents를 곱하지 못한 시점에 첫번째 스레드가 println()을 실행하였을때이다.
즉 짧은 시간동안 오류가 있는 상태가 유지된다

명시적인 동기화를 이용하면 가변 클래스 역시 스레드에 안전하게 만들 수 있다

    class Cash {
        private final int dollars;
        private final int cents;
        public void mul(int factor) {
            synchronized (this) {
                this.dollars *= factor;
                this.cents *= factor;
            }
        }
    }

정상적으로 발생하겠지만 이 방법에는 몇 가지 문제점이 있다
1. 가변 클래스에서 스레드 안정성을 추가하는 일이 쉽지 않다
2. 동기화 로직을 추가하는 일은 성능상의 비용을 초래한다. 각각 스레드가 객체를 사용하기 위해선 객체가 다른 스레드로부터 해방될 때가지 기다려야하기 때문이다.
3. 데드락이 발생할 수 있다 (데드락 : 무한정 기다리는 상태)

    class Cash {
        private final int dollars;
        private final int cents;
    
        public Cash(int dollars, int cents) {
            this.dollars = dollars;
            this.cents = cents;
        }
    
        @Override
        public String toString() {
            return String.format(
                    "$%d.%d, this.dollars. this.cents"
            );
        }
        public void mul(int factor) {
            synchronized (this) {
                this.dollars *= factor;
                this.cents *= factor;
            }
        }
    }
    
    final Cash cash = new Cash(15, 10);
    final CountDownLatch start = new CountDownLatch(1);
    final Callbable<Object> script = new Callable<>() {
        @Override
        public Object call() throws Exception {
            start.await(); //여기
            cash.mul(2);
            System.out.println(cash);
            return null;
        }
    };
    final ExcecutorService svc =
            Executors.newCachedThreadPool();
    svc.submit(script); //첫번째 시도
    svc.submit(script); //두번째 시도
    start.countDown();

2.6.7 더 작고 더 단순한 객체
객체가 더 단순할수록 응집도는 더 높아지고, 유지보수하기도 더 쉬워진다
최고의 소프트웨어는 단순하다. 이해하고, 수정하고, 문서화하고, 지원하고, 리팩토링하기 쉽다.
작고 명확한 객체를 만드려고 노력해보자.