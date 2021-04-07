일급 컬렉션

배포로인해 자정이 넘도록 근무를 할 것 같아 낮에 잠깐 공부하였던 일급 컬렉션을 올리려고 한다.

참조 : 
https://velog.io/@bosl95/%E3%85%87%E3%84%B9%E3%84%B4%E3%85%87


일급 컬렉션이란 - Collection을 Wrapping하면서, Wrapping한 Collection 외 다른 멤버 변수가 없는 상태를 일급 컬렉션이라 한다.

예제 코드를 보면 collection객체를 묶어 하나의 객체로 표현하는 것으로 이해된다.

public class Person {
    private String name;
    private List<Car> cars;
    // ...
}

public class Car {
    private String name;
    private String oil;
    // ...
}


-->

public class Person {
    private String name;
    private Cars cars;
    // ...
}

// List<Car> cars를 Wrapping
// 일급 컬렉션
public class Cars {
    // 멤버변수가 하나 밖에 없다!!
    private List<Car> cars;
    // ...
}

public class Car {
    private String name;
    private String oil;
    // ...
}

사용 이유는
상태와 행위를 따로 관리하여 중복을 없애는 장점이 있어서다.



