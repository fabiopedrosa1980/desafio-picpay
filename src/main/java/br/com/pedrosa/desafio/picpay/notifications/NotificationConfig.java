package br.com.pedrosa.desafio.picpay.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class NotificationConfig {

    @Value("${url.notify}")
    private String urlApi;

    @Bean
    public NotificationClient notificationClient() {
        var httpComponentsClientHttpRequestFactory = getHttpComponentsClientHttpRequestFactory();
        RestClient restClient = RestClient.builder()
                .requestFactory(httpComponentsClientHttpRequestFactory)
                .baseUrl(urlApi).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(NotificationClient.class);
    }

    private HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() {
        var httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(60));
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofSeconds(60));
        return httpComponentsClientHttpRequestFactory;
    }
}
