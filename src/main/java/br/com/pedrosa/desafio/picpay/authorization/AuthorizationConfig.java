package br.com.pedrosa.desafio.picpay.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AuthorizationConfig {

    @Value("${url.auth}")
    private String urlApi;

    @Bean
    public AuthorizationClient authorizationClient() {
        RestClient restClient = RestClient.builder().baseUrl(urlApi).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(AuthorizationClient.class);
    }
}
