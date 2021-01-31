package com.kyle.studynote.elegantobject.chapter1;

public class Cash { //클래스가 무엇인지에 대한 기반 - 지향
    //-er로 끝나는 이름을 가진 클래스명은 잘못 지어졌다고한다. 그렇다면 흔히 쓰는 -controller는 어떻게 지어야할까. 아니면 구조가 프로젝트 구조가 어떻게 바껴야 할까?
    //cotr의 주된 작업은 제공된 인자를 사용해서 캡슐화하고 있는 프로퍼티를 초기화하는 일. cotr = constructor
    //초기화 로직을 단 하나의 ctor에만 위치시키고 주 cotr이라고 칭하면, 다른 부 cotr이 주 cotr을 호출하도록 설계
    private int dollars;

// 틀렸다  - 유지보수의 차이,( 이러한 부분 유지보수는 자주 일어나는가? 자주 일어나지 않는다 해도 규칙을 적용해야하나?
//    Cash(float dlr){
//        this.dollars = (int) dlr;
//    }

//Cash(String  dlr){
//    this.dollars = Cash.parse(dlr);
//}
//

//유지보수를 위해 복잡성과 중복제거를 고려

    Cash(float dlr){
        this((int) dlr);
    }

    Cash(String  dlr){
        this(Cash.parse(dlr));
    }

    private static int parse(String dlr) {
        return Integer.parseInt(dlr);
    }


    Cash(int dlr) {
        this.dollars = dlr;
    }

    public  String format() {
        return String.format("$ %d", this.dollars);
    }
}
