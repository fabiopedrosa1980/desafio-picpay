package br.com.pedrosa.desafio.picpay.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class NotificationConfigTest {

    @InjectMocks
    private NotificationConfig notificationConfig;

    @BeforeEach
    public void setUp() {
        String urlApi = "http://mockurl.com";
        ReflectionTestUtils.setField(notificationConfig, "urlApi", urlApi);
    }

    @Test
    public void shouldReturnNotificationClient() {
        NotificationClient notificationClient = notificationConfig.notificationClient();
        assertNotNull(notificationClient);
    }
}
