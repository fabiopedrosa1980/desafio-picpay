package br.com.pedrosa.desafio.picpay.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @TransactionalEventListener
    public void sendMessage(NotificationEvent notificationEvent)throws Exception{
        logger.info("Email enviado para {} com a mensagem {}",notificationEvent.email(), notificationEvent.message());
    }
}
