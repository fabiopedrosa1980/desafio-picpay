package br.com.pedrosa.desafio.picpay.exception;

public class AuthorizationException extends PicPayException {
    public String message;

    public AuthorizationException(String message) {
        super(message);
    }


}