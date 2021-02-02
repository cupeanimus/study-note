package com.kyle.studynote.elegantobject.chapter2;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

//퍼블릭 상수를 사용하지 말자
//개방 폐쇄원칙?
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


//EOL의 중복되고, 이를 피하기 위해
//constants 클래스를 추가하고 public 객체를 만들어서 해결
// 중복은 피했지만 결합도가 높아지고, 응집도가 낮아졌다
public class Constants {
    public static final String EOL = "\r\n";
}

//결합도 증가
//Constants.EOL 내용을 수정하면 다른 두 클래스에 어떤 영향을 미칠지 예상하기 힘들다. EOL이 어떻게 사용하는지 바로 확인 할 수 없기 때문
//상수는 복잡해질수록 문제는 더 심각해진다


//응집도 저하  기능의 일관성
//퍼블릭 상수를 사용하면 객체의 응집도는 낮아진다.
//대안

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

//위와 같이 접미사 기능을 EOLString 클래스 안으로 고립.
//때문에 나아가
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

//와 계약(인터페이스)은 동일하게 유지하면서 동작은 변경할 수 있다.

//내 고양이는 생선을 먹고 우유를 마시는 것을 좋아한다
//내 것은 그것을 먹고 다른 것을 마시는 것을 좋아한다

//중복을 피하기 위해 것을 추상화 한 경우 내 것이 뭔지, 그것이 뭔지 파악해야한다. 처음이 더 빠르게 의미를 파악할 수 있다


String body = new HttpRequest()
        .method("POST")
        .fetch();
//이 코드는 OOP정신에 어긋난다. 수정하면
String body = new PostRequest(new HttpRequest())
        .fetch();

//다른 얘기지만 개발자의 글쓰기였나. 어느 코딩 관련 글에서 .을 뒤에 붙이길 권하는 것을 보았다. 앞에 접하는 부분을 많이 보았고, 글쓴이가 말하고자 하는 바도 앞에 두는게 더 낫다고 생각하기 때문에 나 역시 앞에 두고 사용하고있다.

//2.6 불변객체로 만드세요

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

//곱하는 연산자를 추가한다면
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

//불변객체는 새로운 클래스를 생성하여 반환한다.   이게 나은건가??

//가변객체 사용법
Cash five = new Cash(5);
five.mul(10);
System.out.println(five);   //"$50" 이 출력된다.

//불변객체
Cash five = new Cash(5);
Cash fifty = five.mul(10);
System.out.println(fifty);   //"$50" 이 출력된다.

//현재 개발하고 있는 프로젝트에서 불변객체를 사용한다면 프로젝트는 더 복잡해질 것이다. 유지보수까지 고려한다면 더 나은것인가?
//개발자는 프로젝트 구조를 처음 접하는 것도 아니고, 파악하는데 시간을 지속적으로 들이는 것도 아니다. 이해의 범주를 생각한다면 팀을 위한 좋은 코드는 무엇인가?

//작가의 말은 가변객체 five가 최종적으로 50$를 리턴하고 있고, 이는 혼란스러운 모습이다. money라고 변경한다고 하면 이처럼 간단한 경우라 적용할 수 있다고 한다.
//그렇다면 간단한 경우는 가변 사용, 복잡하여 명확하게 할 필요가 있을때 불변을 사용하는 것은 어떤가? 통일되지 않고, 불변, 가변을 나누는 기준이 명확하지 않아서 아예 사용하지 않아야 되는 것인가?

//불변 객체가 적합하지 않다고 말하는 예제를 보자
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
//이 예제는 지연 로딩이 작동하는 방식을 잘 보여주고 있다.
//객체를 생성하는 시점에 this.html 값은 없다. content()를 호출할 때 저장되기 때문이다.
//이를 불변객체로 만드는 것은 java에서 불가능하다.

//하지만 다른 방법으로 지연 로딩을 구현할 수 있다. 그리고 프로그래머는 이를 가능해야한다고 말하고 있다. 나라면 어떻게 짰을까?
@OnlyOnce
public String content() {
    return  /*네트워크로부터 로드한다 */
}

//null인 시점 한번, 즉 지연로드를 해야만 하는 부분을 떨어뜨려서 구현하였다.

//2.6.1 식별자 가변성
Map<Cash, String> map = new HashMap<>();
Cash five = new Cash("$5");
Cash ten = new Cash("$10");
map.put(five, "five");
map.put(ten, "ten");
five.mul(2);
System.out.println(map);  //{ $10 => "five", $10 => "ten"}

//위와 같을때 five, ten을 찾는게 혼란스러워지고, 찾기 어려운 버그로 이어질 수 있다.
//명확한 값을 위한 불변 객체의 중요성는 어느정도 이해가 되고 와닿고 있다.
//보통 키 값을 static하게 하지 않는가? 키 밸류가 반대로 주로 사용하는 것과 반대로 쓰이는 데 차이는 뭘까?
