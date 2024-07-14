package br.com.pedrosa.desafio.picpay.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final RestTemplate restTemplate;
    private final String urlAuth;

    public AuthorizationService(RestTemplate restTemplate, @Value("${url.auth}") String urlAuth) {
        this.restTemplate = restTemplate;
        this.urlAuth = urlAuth;
    }

    @Retryable(retryFor = AuthorizationException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100))
    public boolean authorize() {
        try {
            logger.info("Iniciando a autorizacao da transacao");
            var response = restTemplate.getForEntity(urlAuth, AuthorizationResponse.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new AuthorizationException("Transferencia nao autorizada");
            }
            var authorization = response.getBody();
            return Objects.requireNonNull(authorization).data().authorization().equals("true");
        } catch (Exception e) {
            logger.error("Erro ao obter autorizacao {}", e.getMessage());
            throw new AuthorizationException("Erro ao efetuar autorizacao");
        } finally {
            logger.info("Finalizado a autorizacao da transacao");
        }
    }

}
