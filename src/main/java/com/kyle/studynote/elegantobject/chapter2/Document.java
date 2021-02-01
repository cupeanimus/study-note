package com.kyle.studynote.elegantobject.chapter2;

import java.io.InputStream;

//빌더 조정자 따로
public class Document {
    OutputPipe ouput();
}
class OutputPiepe{
    void write(InputStream content);
    int bytes();
    long time();
}

//boolean 값 결과로 반환
//접두사 is 중복이므로 is 제거, 메서드를 읽을때에는 is를 추가하고 실제 코드에선 뺀다
boolean empty();
boolean readable();
boolean negative();




