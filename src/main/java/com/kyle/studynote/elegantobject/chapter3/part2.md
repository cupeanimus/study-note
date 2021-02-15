3.3 인자의 값으로 NULL을 절대 허용하지 말자
코드 어딘가에 NULL이 존재한다면 커다란 실수를 저지르고 있는 것이다.

    public Interable<File> find(String mask) {
        //디렉토리를 탐색해서 "*.txt"와 같은 형식의
        //마스크에 일치하는 모든 파일을 찾는다.
        //마스크가 NULL인 경우에는 모든 파일을 반환한다
    }
전달할 객체가 없으므로 값이 없는 것으로 간주하라는 의사를 표현할 수 있도록 사용자에게 진짜 객체 대신 NULL을 허용하는 것은 일반적인 방법이다.
    
    public Interable<File> findALL();
    public Interable<File> find(String mask);

실제로 위 두 메서드를 하나로 합 칠수 있는 편리한 방법으로 보인다.
이 방식이 논리적이라고 생각 할 수 있겠지만, 각각의 객체가 자신의 행동을 온전히 책임진다는 객체 패러다임과는 상반되는 아이디어이다.
NULL을 허용하는 find() 메서드를 구현하기 위해서는 다음과 같이 분기를 처리할 필요가 있다.

    public Interable<File> find(String mask) {
        if (mask == null) {
        //모든 파일을 찾는다
        } else {
        //마스크를 사용해서 파일을 찾는다
        }     
    }

이 코드에서 문제가 되는 부분은 mask == null이다.
mask 객체에게 이야기 하는 대신, 이 객체를 피하고 무시한다. 
객체를 존중한다면 다음과 같이 행동할 수 있다.

    public Interable<File> find(String mask) {
        if(mask.empty)) {
        //모든 파일을 찾는다
        } else {
        //마스크를 사용해서 파일을 찾는다
        }     
    }
    
    더 개선하면
    public Interable<File> find(String mask) {
        Collection<File> files = new LinkedList<>(); 
        for (File file : /* 모든 파일 */)
            if (mask.matches(file)) {
                files.add(file);
            }
        }
        return files;             
    }        

mask 객체를 존중했다면 조건의 존재 여부를 객체 스스로 결정하게 했을 것이다. (객체 관점의 코딩 = 객체지향이라 이해해도 될 것 같다)
겉모습만으로 객체를 판단해서는 안되고, '진짜' 객체라면 대화에 응할 것이고 NULL이면 대응하지 않겠다는 식으로 객체와 의사소통해서는 안된다.
NULL 여부를 체크함으로써 객체가 맡아야 하는 상당량의 책임을 빼앗게 된다. 이것은 외부에서 자신의 데이터를 다뤄주기만을 기대하고 스스로를 책임질 수 없는
멍청한 자료구조로 객체를 퇴하시키는 것이다.

검색 조건을 지정하기 위해 find() 메서드에 전달하는 Mask 인터페이스가 있다
    
    interface Mask {
        boolean matches(File file);
    }
    
    이 인터페이스의 적절한 구현은 '글롭(glob)' 패턴('*.txt' 형식의 패턴)을 캡슐화 하고 이 패턴에 대해 파일 이름을 매칭시킬 것이다.
    Null객체는 다음과 같이 구현할 수 있다.
    
    class AnyFile implements Mask{
        @Override
        boolean matches(File file) {
        retrun true)
        }
    }
    
AnyFile은 Mask의 특별한 경우로, 어떤 내부 로직도 포함하지 않는다. 어떤 파일을 전달하더라도 항상 true를 반한환다. 이제부터 null 값을 전달하는 대신,
AnyFile의 인스턴스를 생성해서 find()메서드에 전달하면 된다. find() 메서드는 무슨 일이 일어나고 있는지 전혀 알지 못한채, 여전히 올바른 Mask가 전달되었다고 생각 할 것이다.
메서드가 인자의 값으로 NULL을 허용하지 않기로 가정했는데, 클라이언트가 여전히 NULL을 전달한다면 어떻게 해야할까?
기본적으로 두 가지 방법이 있다. 하나는 방어적인 방법으로 NULL을 체크한 후 예외를 던진다.

    public Iterable<File> find(Mask mask) {
        if(mask == null) {
        throw new IllegalArgumentException(
            "Mask can't be NULL; please provide an object.");
        }
    //마스크를 사용해서 파일을 찾아 반환한다
    }
두 번째 방법은 개인적으로 선호하는 방법으로 NULL을 무시하는 것이다. 여기서 인자가 절대 NULL이 아니라고 가정하고 어떤 대비도 하지 않는다. 메서드를 실행하는 도중
인자에 접근하면 NullPointException이 던져지고 메서드 호출자는 자신이 실수했다는 사실을 인지하게 된다.
중요하지 않은 NULL 확인 로직으로 코드를 오렴시켜서는 안된다. NullPointException은 잘못된 위치에 NULL이 전달됐다는 사실을 알려주는 올바른 지표다.
(NullPointException으로 에러를 방지하기 위해 많은 부분에 null 체크를 하고 있다. 필자가 말하는 대로 굳이 이렇게 체크해서 방지하는 것보다 에러를 발생시켜 잘못된 사실을 인지하고
이러한 호출이 오지 않도록 하는것도 좋은 방법이라 생각된다.)      
                    
        
  