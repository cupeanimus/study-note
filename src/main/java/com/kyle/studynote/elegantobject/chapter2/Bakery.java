package com.kyle.studynote.elegantobject.chapter2;

import java.io.InputStream;

public class Bakery {
    Food cookBrownie();
    Drink BrewCupOfCoffee(String flavor);

    Food cook_brownie(){
        //브라우니를 요리해서
        //반환한다
    }

    Drink brew_cup_of_coffe(char flavor){
        //커피를 끓여서
        //반환한다
    }

    //메서드의 이름을 동사로 지을 때에는 객체에게 무엇을 할지 알려주어야 한다. 객체에게 무엇을 만들라고 요청하는 것은 예의에 어긋난다.

    //잘못된 이름
    InputStream load(URL url);
    String read(File file);
    int add(int x, int y);

    //수정
    InputStream stream(URL url);
    String content(File file);
    int sum(int x, int y);
}
