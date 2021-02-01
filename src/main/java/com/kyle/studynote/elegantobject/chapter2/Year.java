package com.kyle.studynote.elegantobject.chapter2;


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

    //불완전한 모습
//    private Millis millis;
//    Year(Millis msec){
//        this.millis = msec;
//    }
//    int read() {
//        return this.millis.read()
//        / (1000 * 60 * 24 * 30 * 12) -1970;
//    }
//




}
