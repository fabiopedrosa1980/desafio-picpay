package br.com.pedrosa.desafio.picpay.authorization;

import org.springframework.web.service.annotation.GetExchange;

public interface AuthorizationClient {

    @GetExchange("/authorize")
    AuthorizationResponse authorized();
}
