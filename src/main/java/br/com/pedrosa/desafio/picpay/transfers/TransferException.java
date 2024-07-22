package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.exception.PicPayException;

public class TransferException extends PicPayException {

    public TransferException(String message) {
        super(message);
    }

}
