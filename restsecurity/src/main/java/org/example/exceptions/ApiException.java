package org.example.exceptions;

import io.javalin.http.HttpStatus;
import lombok.Getter;

@Getter
public class ApiException extends Exception{
    private HttpStatus status;
    public ApiException(int code, String msg){
        super(msg+", "+code);
        status = HttpStatus.forStatus(code);
    }


}
