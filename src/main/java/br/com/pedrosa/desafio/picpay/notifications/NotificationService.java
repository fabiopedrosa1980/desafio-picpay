package br.com.pedrosa.desafio.picpay.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String NOTIFICATION_ERROR = "Erro ao notificar payee {}";
    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendNotification(NotificationRequest notificationRequest) {
        try {
            logger.info("Enviando email para o favorecido {}", notificationRequest.email());
            notificationClient.notifyUser(notificationRequest);
            logger.info("Email enviado com sucesso para {} com a mensagem {}",
                    notificationRequest.email(), notificationRequest.message());
        } catch (Exception e) {
            logger.error(NOTIFICATION_ERROR, e.getMessage());
            throw new NotificationException(NOTIFICATION_ERROR);
        }

    }
}
