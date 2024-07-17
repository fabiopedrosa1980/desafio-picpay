package br.com.pedrosa.desafio.picpay.authorization;

import br.com.pedrosa.desafio.picpay.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    @Retryable(retryFor = AuthorizationException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 300))
    public boolean authorize() {
        try {
            logger.info("Validando autorizacao da transferencia");
            var resp = this.authorizationClient.authorized();
            return resp.data().authorization().equals("true");
        } catch (Exception e) {
            logger.error("Erro autorizar transferencia {}", e);
            throw new AuthorizationException("Transferencia nao autorizada");
        }
    }
}
