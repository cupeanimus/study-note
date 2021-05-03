모든 원시값과 문자열을 포장하라 - 더 나은 소프트웨어를 향한 9단계

참고 : https://ka0oll.tistory.com/34

원시값 - byte, short, int, long, float, double, boolean, char 그리고 String을 포장하라는 의미

장점
1.해당 값에 의미를 부여할 수 있다.
돈을 예로 들면

    void sell(int price);
    
    void sell(Money price);
    
    
    class Money{
    
        private int value;
    
        Money(int value){
            this.value = value;
        }
    }
와 같이 Money라는 객체를 생성함으로써 값에 의미를 부여할 수 있다.

2.해당 리터얼에 대한 제약조건을 부여 할 수 있다.
   
    class PositiveNum{
   
       private int value;
   
       PositiveNum(int value){
           if(value < 0){
               throw new IllegalException();
           }
           this.value = value;
       }
    }

PositiveNum과 같이 제약조건이 걸린 값을 바로 알 수 있다.
     
   