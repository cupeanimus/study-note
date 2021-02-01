package com.kyle.studynote.elegantobject.chapter2;

import lombok.EqualsAndHashCode;

//클래스 내 객체를 4개 이하로 하고, 이를 넘어가면 잘못된 것으로 간주 리팩토링한다고 한다. 그렇다면 jpa의 entity인경우는? response할 return요소는 어떻게 할 것인가?
@EqualsAndHashCode //?
public class Cash {
    private Integer digits;
    private Integer cents;
    private String currency;
    //java의 결함을 해결하기 위해 == 대신 equals() 메소드를 오버라이드하길 권하고, 역자는 @EqualsAndHashCode 어노테이션을 사용한다고 한다.

}
