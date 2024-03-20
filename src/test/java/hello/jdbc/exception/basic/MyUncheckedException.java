package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Runtime Exception을 상속받은 예외는 언체크 예외가 된다
 */
@Slf4j
public class MyUncheckedException extends RuntimeException{
    public MyUncheckedException(String message){
        super(message);
    }

    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw(){
        Service service = new Service();
        service.callThrow();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);

    }



    static class Service{
        Repository repository = new Repository();

        public void callCatch(){
            try{
                repository.call();
            }catch (MyUncheckedException e){
                log.info("예외 처리, message= ", e.getMessage(), e);
            }
        }


        public void callThrow(){
            repository.call();;
        }
    }


    /**
     * Unchekced 예외는 예외를 잡거나, 던지지 않아도 된다. 예외를 잡지 않으면 자동으로 밖으로 던진다.
     *
     */
    static class Repository{
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }
}
