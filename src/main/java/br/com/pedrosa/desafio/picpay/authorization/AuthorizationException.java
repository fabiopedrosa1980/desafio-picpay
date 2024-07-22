package br.com.pedrosa.desafio.picpay.authorization;

import br.com.pedrosa.desafio.picpay.exception.PicPayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AuthorizationException extends PicPayException {
    public final String message;

    public AuthorizationException(String message) {
        super(message);
        this.message = message;
    }

    public ProblemDetail problemDetail(){
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,message);
    }

}