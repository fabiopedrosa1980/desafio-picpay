package br.com.pedrosa.desafio.picpay.authorization;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}