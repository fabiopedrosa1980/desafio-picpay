package br.com.pedrosa.desafio.picpay.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class NotificationConfigTest {

    @InjectMocks
    private NotificationConfig notificationConfig;

    @BeforeEach
    public void setUp() {
        // Set the URL property value using ReflectionTestUtils
        String urlApi = "http://mockurl.com";
        ReflectionTestUtils.setField(notificationConfig, "urlApi", urlApi);
    }

    @Test
    public void testCreateWebClient() {
        // Call the method to create the WebClient bean
        WebClient webClient = notificationConfig.createWebClient();

        // Assertions
        assertNotNull(webClient);
        // Here you could add more assertions to verify WebClient configuration if needed
    }

    @Test
    public void testCreateProxyFactory() {
        // Create a WebClient instance
        WebClient webClient = notificationConfig.createWebClient();

        // Call the method to create the HttpServiceProxyFactory bean
        HttpServiceProxyFactory proxyFactory = notificationConfig.createProxyFactory(webClient);

        // Assertions
        assertNotNull(proxyFactory);
        // Here you could add more assertions to verify HttpServiceProxyFactory configuration if needed
    }

    @Test
    public void testNotificationClient() {
        // Create a WebClient instance
        WebClient webClient = notificationConfig.createWebClient();

        // Create an HttpServiceProxyFactory instance
        HttpServiceProxyFactory proxyFactory = notificationConfig.createProxyFactory(webClient);

        // Call the method to create the NotificationClient bean
        NotificationClient notificationClient = notificationConfig.notificationClient(proxyFactory);

        // Assertions
        assertNotNull(notificationClient);
        // Here you could add more assertions to verify NotificationClient configuration if needed
    }
}
