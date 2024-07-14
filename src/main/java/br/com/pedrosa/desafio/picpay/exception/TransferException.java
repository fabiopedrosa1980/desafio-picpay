package br.com.pedrosa.desafio.picpay.exception;

public class TransferException extends RuntimeException {
    public TransferException(String message){
        super(message);
    }
}
