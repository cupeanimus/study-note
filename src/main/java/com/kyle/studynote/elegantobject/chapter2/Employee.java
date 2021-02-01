package com.kyle.studynote.elegantobject.chapter2;

//클래스 안의 모든 퍼블릭 메서드가 인터페이스를 구현하도록 해야한다고 한다. 업무에선 인터페이스 사용은 유지보수에서 오히려 귀찮은 요소라 할때가 많다. 깊게 비교해 볼 필요가 있다.
//인터페이스를 사용하면 시스템의 다른 부분이 변경 사항을 알지 못한 채 한 부분을 실수로 변경하더라도 시스템이 무너지지 않게 유지할 수 있다.
// 요소들 사이의 계약으로 인터페이스는 우리가 전체적인 환경을 구조화 된 상태로 유지할 수 있게 해준다.
public class Employee {
    private CashInterface salary;
}

//클래스 이름을 지을때 builder(뭔가를 만들고 새로운 객체를 반환하는 메소드)는 명사, 조정자는 동사로 한다.
// int pow(int base, int powser); speed, employee, parsedCell
//void save(String content); put, remove, quicklyPrint

// 조정자는 반환하는 게 없는 void라고 한다면 메소드 실행결과를 체크하는 건 어떻게 하는가? 체크를 해야하는 경우라면 builder인가? -> document class