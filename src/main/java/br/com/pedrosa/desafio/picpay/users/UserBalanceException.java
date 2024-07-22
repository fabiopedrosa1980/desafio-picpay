package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.PicPayException;

public class UserBalanceException extends PicPayException {

    public UserBalanceException(String message) {
        super(message);
    }


}
