package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.PicPayException;

public class BalanceException extends PicPayException {

    public BalanceException(String message) {
        super(message);
    }


}
