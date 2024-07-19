package br.com.pedrosa.desafio.picpay.authorization;

import br.com.pedrosa.desafio.picpay.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Mock
    private AuthorizationClient authorizationClient;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Value("${url.auth}")
    private String urlAuth;

    private AuthorizationResponse successResponse;
    private AuthorizationResponse failResponse;

    @BeforeEach
    void setUp() {
        urlAuth = "http://fakeurl/auth";
        // Initialize mock responses
        successResponse = new AuthorizationResponse("success");
        failResponse = new AuthorizationResponse("fail");

        // Inject urlAuth manually since @Value does not work in unit tests
        authorizationService = new AuthorizationService(authorizationClient);
    }

    @Test
    void authorize_ShouldReturnTrue_WhenResponseIsSuccessful() {
        // Arrange
        when(authorizationClient.authorized()).thenReturn(successResponse);

        // Act
        boolean result = authorizationService.authorize();

        // Assert
        assertTrue(result);
        verify(authorizationClient).authorized();
    }

    @Test
    void authorize_ShouldReturnFalse_WhenResponseIsUnsuccessful() {
        // Arrange
        when(authorizationClient.authorized()).thenReturn(failResponse);

        // Act
        boolean result = authorizationService.authorize();

        // Assert
        assertFalse(result);
        verify(authorizationClient).authorized();
    }

    @Test
    void authorize_ShouldThrowAuthorizationException_WhenAuthorizationExceptionOccurs() {
        // Arrange
        when(authorizationClient.authorized()).thenThrow(new AuthorizationException("Transferencia nao autorizada"));;

        // Act & Assert
        assertThrows(AuthorizationException.class, () -> authorizationService.authorize());
        verify(authorizationClient).authorized();
    }

    @Test
    void authorize_ShouldThrowAuthorizationException_WhenExceptionOccurs() {
        // Arrange
        when(authorizationClient.authorized()).thenThrow(new RuntimeException("Transferencia nao autorizada"));

        // Act & Assert
        assertThrows(AuthorizationException.class, () -> authorizationService.authorize());
        verify(authorizationClient).authorized();
    }


}
