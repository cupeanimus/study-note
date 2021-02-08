2.1 가능하면 적게 캡슐화 하라.
근거는 없지만 4개 이하의 객체를 캡슐화 할 것을 권장한다(dto, entity 성격의 객체는 어떻게 하나? 조합으로?)
Java의 결함을 해결하기 위해 == 연산자 대신 equals() 메서드를 오버라이드하자(필자는 Lombok의 @EqualsAndHashCode 어노테이션을 사용한다)

2.2 최소한 뭔가는 캡슐화 하라.
    
    public class Year {
        private Number num;
        Year(final Millis msec) {
            this.num = new Min(
                    new Div(
                            msec,
                            new Mul(1000, 60, 60, 24, 30, 12)
                    ),
                    1970
            );
        }

    //또는
    Year(final Millis msec) {
        this.num = msec.div(
                1000.mul(60).mul(60).mul(24).mul(30).mul(12)
        ).min(1970);
    }
    int read() {
        return this.num.intValue();
    }

2.3 항상 인터페이스를 사용하라.
기술적인 관점에서 객체 분리란 상호작용하는 다른 객체를 수정하지 않고도 해당 객체를 수정할 수 있도록 만든다는 의미이고 이를 가능하게 하는 가장 훌륭한 도구가
바로 인터페이스다

    interface Cash {
        Cash multiply(float factor);
    }
    
    public class DefaultCash implements Cash {
        private int dollars;
        DefaultCash(int dlr) {
            this.dollars = dlr;
        }
    
        @Override
        Cash multiply(float factor) {
            return new DefaultCash((int) (this.dollars * factor));
        }
    }

여기서 Employee 클래스는 Cash 인터페이스의 구현 방법에 관심이 없고, 메서드가 어떻게 동작하는지도 관심 없다.
즉, 동작 방식을 알지 못한다 <- Cash 인터페이스를 이용하면 Employee클래스와 DefaultCash 클래스를 느슨하게 분리할 수 있다는 의미.

올바르게 설계된 클래스라면 최소한 하나의 인터페이스라도 구현하지 않는 퍼블릭 메서드를 포함해서는 안된다.
즉 인터페이스에 구현하지 않는 퍼블릭 메서드를 포함해서는 안된다.

    잘못된 예
    class Cash {
        public int cents() {
            //어떤 작업 수행
        }    
    }

클래스가 존재하는 이유는 다른 누군가가 클래스의 서비스를 필요로 하기 때문. 서비스는 계약이자 인터페이스이기 때문에
클래스가 제공하는 서비스는 어딘가에 문서화 되어야 한다. 게다가 서비스 제공자들은 서로 경쟁한다. 다시 말하자면 동일한 인터페이스를 구현하는 여러 클래스들이 존재한다는 뜻이며
각각의 경쟁자는 서로 다른 경쟁자를 쉽게 대체할 수 있어야 한다. 이것이 느슨한 결합도의 의미이다.

(실제 작업하며 인터페이스 없이 객체에 메서드를 바로 구현하는 경우가 많다. 인터페이스를 거치지 않기 때문에 단계가 개발이 편리하고 유지보수가 더 쉽다고 생각했고, 회사 업무에서도 요구받았다.
이 두 방법에 대해 좀 더 생각해보려 한다.)


2.4 메서드 이름을 신중하게 선택하라.
빌더의 이름은 명사로, 조정자의 이름은 동사로 짓는다.

빌더와 조정자 혼합하기
    
    public class Document {
        OutputPipe ouput(); //빌더
    }
    class OutputPiepe{
        void write(InputStream content);
        int bytes();
        long time();
    }
    
boolean 값 결과로 반환

    //접두사 is 중복이므로 is 제거, 메서드를 읽을때에는 is를 추가하고 실제 코드에선 뺀다
    boolean empty();
    boolean readable();
    boolean negative();

2.5 퍼블릭 상수를 사용하지 마라.

    public class Records {
        private static final String EOL = "\r\n";
        
        void write(Writer out) throws IOException {
            for (Record rec : this.all) {
                out.write(rec.toString);
                out.write(Records.EOL);
            }
        }
    }
    
    class Rows {
        private static final String EOL = "\r\n";
    
        void print(PrintStream pnt) {
            for (Row row : this.fetch()) {
                pnt.print(
                        "{ %s }%s", row, Rows.EOL
                );
            }
        }
    }

  
EOL의 중복되고, 이를 피하기 위해
constants 클래스를 추가하고 public 객체를 만들어서 해결
중복은 피했지만 결합도가 높아지고, 응집도가 낮아졌다

    public class Constants {
        public static final String EOL = "\r\n";
    }

결합도 증가
Constants.EOL 내용을 수정하면 다른 두 클래스에 어떤 영향을 미칠지 예상하기 힘들다. EOL이 어떻게 사용하는지 바로 확인 할 수 없기 때문
상수는 복잡해질수록 문제는 더 심각해진다


응집도 저하  기능의 일관성
퍼블릭 상수를 사용하면 객체의 응집도는 낮아진다.
대안

    class EOLString {
        private final String origin;
        EOLString(String src) {
            this.origin = src;
        }
    
        @Override
        public String toString() {
            return String.format("%s\r\n",origin);
        }
    }
    
    class NewRecords {
     void write(Writer out) {
         for (Record rec : this.all) {
             out.write(new EOLString(rec.toString()));
         }
     }
    }
    
    class newRows {
        void print(PrintStream pnt) {
    
            for (Row row : this.fetch()) {
                pnt.print(
                        new EOLString(
                                String.format("{ %s }",row)
                        )
                );
            }
        }
    }

위와 같이 접미사 기능을 EOLString 클래스 안으로 고립.
때문에 나아가

    class EOLString {
        private final String origin;
        EOLString(String src) {
            this.origin = src;
        }
    
        public String toString() {
            if (/* Windows의 경우 */) {
                throw new IllegalStateException(
                        "현재 Windows에서 실행 중이기 때문에 EOL을 사용할 수 없습니다. 죄송합니다"
                );
            }
            return String.format("%s\r\n",origin);
        }
    }

와 계약(인터페이스)은 동일하게 유지하면서 동작은 변경할 수 있다.
내 고양이는 생선을 먹고 우유를 마시는 것을 좋아한다
내 것은 그것을 먹고 다른 것을 마시는 것을 좋아한다
중복을 피하기 위해 것을 추상화 한 경우 내 것이 뭔지, 그것이 뭔지 파악해야한다. 처음이 더 빠르게 의미를 파악할 수 있다

    String body = new HttpRequest()
            .method("POST")
            .fetch();
    //이 코드는 OOP정신에 어긋난다. 수정하면

    String body = new PostRequest(new HttpRequest())
            .fetch();

다른 얘기지만 개발자의 글쓰기였나. 어느 코딩 관련 글에서 .을 뒤에 붙이길 권하는 것을 보았다. 앞에 접하는 부분을 많이 보았고, 글쓴이가 말하고자 하는 바도 앞에 두는게 더 낫다고 생각하기 때문에 나 역시 앞에 두고 사용하고있다.



    