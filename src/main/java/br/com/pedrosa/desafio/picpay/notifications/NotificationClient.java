package br.com.pedrosa.desafio.picpay.notifications;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface NotificationClient {

    @PostExchange("/notify")
    Mono<Void> notifyUser(@RequestBody NotificationRequest notificationRequest);
}
