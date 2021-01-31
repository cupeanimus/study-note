package com.kyle.studynote.elegantobject.chapter1;


public class Shapes {
    public Shapes make(String name){
//        if(name.equals("circle")){
//            return new Circle();
//        }
//        if (name.equals("rectangle")){
//            return new Rectangle();
//        }
        throw new IllegalArgumentException("not found");
    }
}
