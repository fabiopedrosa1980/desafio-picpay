package br.com.pedrosa.desafio.picpay.notifications;

import br.com.pedrosa.desafio.picpay.exception.NotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Retryable(retryFor = NotificationException.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 500))
    public void send(NotificationEvent notificationEvent) {
        try {
            logger.info("Enviando email {} com a mensagem {} em {}",
                    notificationEvent.email(), notificationEvent.message(), LocalDateTime.now());
            this.notificationClient.notifyUser();
            logger.info("Email enviado com sucesso para {}", notificationEvent.email());
        } catch (Exception e) {
            logger.error("Erro ao enviar notificacao {}", e.getMessage());
            throw new NotificationException("Erro ao notificar cliente " + e.getMessage());
        }
    }
}
