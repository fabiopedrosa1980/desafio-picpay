package br.com.pedrosa.desafio.picpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserTypeException extends PicPayException {
    private final String message;

    public UserTypeException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public ProblemDetail problemDetail(){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,message);
    }
}
