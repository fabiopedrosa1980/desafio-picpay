package br.com.pedrosa.desafio.picpay.notifications;

import br.com.pedrosa.desafio.picpay.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotificationSuccess() {
        NotificationRequest notificationRequest = new NotificationRequest("test@example.com", "Test message");

        when(notificationClient.notifyUser(notificationRequest)).thenReturn(Mono.empty());

        Mono<Void> result = notificationService.sendNotification(notificationRequest);

        StepVerifier.create(result)
                .verifyComplete();

        verify(notificationClient, times(1)).notifyUser(notificationRequest);
    }

    @Test
    void testSendNotificationFailure() {
        NotificationRequest notificationRequest = new NotificationRequest("test@example.com", "Test message");

        when(notificationClient.notifyUser(notificationRequest)).thenReturn(Mono.error(new RuntimeException("Client error")));

        Mono<Void> result = notificationService.sendNotification(notificationRequest);

        StepVerifier.create(result)
                .expectError(NotificationException.class)
                .verify();

        verify(notificationClient, times(1)).notifyUser(notificationRequest);
    }

    @Test
    void testSend() {
        NotificationRequest notificationRequest = new NotificationRequest("test@example.com", "Test message");

        when(notificationClient.notifyUser(notificationRequest)).thenReturn(Mono.empty());

        notificationService.send(notificationRequest);

        verify(notificationClient, times(1)).notifyUser(notificationRequest);
    }

    @Test
    void testSendWithError() {
        NotificationRequest notificationRequest = new NotificationRequest("test@example.com", "Test message");

        when(notificationClient.notifyUser(notificationRequest)).thenReturn(Mono.error(new RuntimeException("Client error")));

        notificationService.send(notificationRequest);

        verify(notificationClient, times(1)).notifyUser(notificationRequest);
    }
}
