package br.com.pedrosa.desafio.picpay.authorization;

import br.com.pedrosa.desafio.picpay.exception.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthorizationService {
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
            var response = restTemplate.getForEntity(urlAuth, AuthorizationDTO.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new AuthorizationException("Transferencia nao autorizada");
            }
            var authorization = response.getBody();
            return Objects.requireNonNull(authorization).data().authorization().equals("true");
        } catch (Exception e) {
            throw new AuthorizationException("Erro ao efetuar autorizacao");
        }
    }
}
