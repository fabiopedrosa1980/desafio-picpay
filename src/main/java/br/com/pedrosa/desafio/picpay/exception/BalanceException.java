package br.com.pedrosa.desafio.picpay.exception;

public class BalanceException extends RuntimeException {
    public BalanceException(String message) {
        super(message);
    }
}
