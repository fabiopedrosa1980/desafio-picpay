package br.com.pedrosa.desafio.picpay.notifications;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendMessage(String email){
        System.out.println("Email enviado para " + email);
    }
}
