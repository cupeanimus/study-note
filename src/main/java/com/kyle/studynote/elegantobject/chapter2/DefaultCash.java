package com.kyle.studynote.elegantobject.chapter2;

public class DefaultCash implements CashInterface {
    private int dollars;
    DefaultCash(int dlr) {
        this.dollars = dlr;
    }

    @Override
    public CashInterface multiply(float factor) {
        return new DefaultCash((int) (this.dollars * factor));
    }
}
