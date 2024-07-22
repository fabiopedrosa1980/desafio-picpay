package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.PicPayException;

public class UserTypeException extends PicPayException {
    public UserTypeException(String message) {
        super(message);
    }
}
