package br.com.pedrosa.desafio.picpay.authorization;

import br.com.pedrosa.desafio.picpay.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    public static final String SUCCESS = "success";
    public static final String TRANSFER_NOT_AUTHORIZED = "Transferencia nao autorizada";

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    @Retryable(retryFor = AuthorizationException.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 100))
    public boolean authorize() {
        try {
            logger.info("Validando autorizacao da transferencia {}", LocalDateTime.now());
            var resp = this.authorizationClient.authorized();
            return SUCCESS.equals(resp.status());
        } catch (Exception e) {
            logger.error("Erro autorizar transferencia {}", e.getMessage());
            throw new AuthorizationException(TRANSFER_NOT_AUTHORIZED);
        } finally {
            logger.info("Transferencia autorizada com sucesso");
        }
    }
}
