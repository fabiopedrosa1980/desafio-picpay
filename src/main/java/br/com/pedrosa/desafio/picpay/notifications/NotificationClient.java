package br.com.pedrosa.desafio.picpay.notifications;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface NotificationClient {

    @PostExchange("/notify")
    void notifyUser(@RequestBody NotificationRequest notificationRequest);
}
