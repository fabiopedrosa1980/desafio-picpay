package br.com.pedrosa.desafio.picpay.notifications;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class NotificationService {

    @TransactionalEventListener
    public void sendMessage(NotificationEvent notificationEvent){
        System.out.println(STR."Email enviado para \{notificationEvent.email()} \{notificationEvent.message()}");
    }
}
