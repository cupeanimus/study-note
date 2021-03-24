1. Checked, Unchecked Exception

Exception은 Checked와 UnChecked로 구분되며 Transaction이 설정되었을때 rollback이 되는게 있고 안되는게 있다고 한다.
웬만한 에러들은 runtimeException을 상속받아 rollback이 진행되어 이를 당연하게만 여겼는데 이게 아닌 것이다.
내가 알고 있는 것들은 unChecked exception이고 rollback이 되지만 이를 상속 받지 않는 IOException, SQLException은 rollback이 일어나지 않는다
따라서 명시적으로 처리하여야 하며 try catch나 throws를 통해 호출한 메서드로 예외를 던지도록 하여야 한다.

참조:
https://cheese10yun.github.io/checked-exception/