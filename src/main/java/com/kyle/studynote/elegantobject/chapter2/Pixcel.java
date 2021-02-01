package com.kyle.studynote.elegantobject.chapter2;

import java.awt.*;

public class Pixcel {
    void paint(Color color);
}
Pixcel center = new Pixcel(50,50);
center.paint(new Color("red"));//paint 메서드는 반환하지 않는다.

//빌더 패턴을 사용할 경우 with로 시작하는 메서드를 사용해도 무방하다.
class Book {
    Book withAuthor(String author);
    Book withTilte(String title);
    Book whithPage(Page page);
}
