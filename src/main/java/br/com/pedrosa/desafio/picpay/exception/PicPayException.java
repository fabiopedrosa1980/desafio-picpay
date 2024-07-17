package br.com.pedrosa.desafio.picpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class PicPayException extends RuntimeException{
    private String message;

    public PicPayException(String message) {
        this.message = message;
    }

    public ProblemDetail problemDetail(){
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,message);
        return pb;
    }
}
