package br.com.pedrosa.desafio.picpay.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationRequest notificationRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationRequest = new NotificationRequest("test@example.com", "Test message");
    }

    @Test
    void shouldSendNotificationSuccess() {
        notificationService.sendNotification(notificationRequest);

        ArgumentCaptor<NotificationRequest> captor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationClient, times(1)).notifyUser(captor.capture());
        assertEquals(notificationRequest, captor.getValue());
    }

    @Test
    void shouldSendNotificationException() {
        doThrow(new RuntimeException("Erro ao notificar payee")).when(notificationClient).notifyUser(any());

        NotificationException exception = assertThrows(NotificationException.class, () ->
                notificationService.sendNotification(notificationRequest));

        verify(notificationClient, times(1)).notifyUser(notificationRequest);
    }
}
