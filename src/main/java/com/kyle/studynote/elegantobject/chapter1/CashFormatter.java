package com.kyle.studynote.elegantobject.chapter1;

public class CashFormatter { //무엇을 하는지로 정해진 클래스명 - 지양
    private int dollars;
    CashFormatter(int dlr) {
        this.dollars = dlr;
    }
    public  String format() {
        return String.format("$ %d", this.dollars);
    }
}
