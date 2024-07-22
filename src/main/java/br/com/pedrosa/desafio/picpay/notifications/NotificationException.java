package br.com.pedrosa.desafio.picpay.notifications;

import br.com.pedrosa.desafio.picpay.exception.PicPayException;

public class NotificationException extends PicPayException {

    public NotificationException(String message) {
        super(message);
    }
}
