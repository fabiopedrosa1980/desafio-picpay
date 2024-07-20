package br.com.pedrosa.desafio.picpay.notifications;

import br.com.pedrosa.desafio.picpay.exception.NotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String NOTIFICATION_ERROR = "Erro ao notificar payee {}";

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void send(NotificationRequest notificationEvent) {
        sendNotification(notificationEvent).subscribe(
                null,
                NotificationService::logError
        );
    }

    private static void logError(Throwable throwable) {
        logger.error(NOTIFICATION_ERROR, throwable.getMessage());
    }

    public Mono<Void> sendNotification(NotificationRequest notificationRequest) {
        logger.info("Enviando email {} com a mensagem {} em {}",
                notificationRequest.email(), notificationRequest.message(), LocalDateTime.now());

        return notificationClient.notifyUser(notificationRequest)
                .doOnError(e -> {
                    logError(e);
                    throw new NotificationException(NOTIFICATION_ERROR);
                })
                .doOnSuccess(_ -> logger.info("Email enviado com sucesso para {}", notificationRequest.email()));
    }
}
