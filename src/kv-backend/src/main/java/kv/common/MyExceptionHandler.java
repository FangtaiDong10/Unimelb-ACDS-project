package kv.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(KVException.class)
    @ResponseBody
    public KVJsonResult returnKVException(KVException kvException) {
        kvException.printStackTrace();
        return KVJsonResult.error(kvException.getMessage());
    }
}
