package br.com.pedrosa.desafio.picpay.notifications;

import br.com.pedrosa.desafio.picpay.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationRequest notificationRequest;

    @BeforeEach
    void setUp() {
        notificationRequest = new NotificationRequest("test@example.com", "Test message");
    }

    @Test
    void send_ShouldCallNotifyUser_WhenNotificationRequestIsValid() {
        // Arrange
        doNothing().when(notificationClient).notifyUser();

        // Act
        notificationService.send(notificationRequest);

        // Assert
        verify(notificationClient, times(1)).notifyUser();
    }

    @Test
    void send_ShouldThrowNotificationException_WhenNotifyUserThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Erro qualquer")).when(notificationClient).notifyUser();

        // Act & Assert
        assertThrows(NotificationException.class, () -> notificationService.send(notificationRequest));

        verify(notificationClient).notifyUser();
    }
}
