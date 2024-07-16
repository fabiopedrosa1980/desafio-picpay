package br.com.pedrosa.desafio.picpay.authorization;

import br.com.pedrosa.desafio.picpay.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Mock
    private RestTemplate restTemplate;

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
        successResponse = new AuthorizationResponse("success",new Data("true"));
        failResponse = new AuthorizationResponse("success",new Data("false"));

        // Inject urlAuth manually since @Value does not work in unit tests
        authorizationService = new AuthorizationService(restTemplate, urlAuth);
    }

    @Test
    void authorize_ShouldReturnTrue_WhenResponseIsSuccessful() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(AuthorizationResponse.class)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));

        // Act
        boolean result = authorizationService.authorize();

        // Assert
        assertTrue(result);
        verify(restTemplate).getForEntity(urlAuth, AuthorizationResponse.class);
    }

    @Test
    void authorize_ShouldReturnFalse_WhenResponseIsUnsuccessful() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(AuthorizationResponse.class)))
                .thenReturn(new ResponseEntity<>(failResponse, HttpStatus.OK));

        // Act
        boolean result = authorizationService.authorize();

        // Assert
        assertFalse(result);
        verify(restTemplate).getForEntity(urlAuth, AuthorizationResponse.class);
    }

    @Test
    void authorize_ShouldThrowAuthorizationException_WhenResponseStatusIsNotOk() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(AuthorizationResponse.class)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(AuthorizationException.class, () -> authorizationService.authorize());
        verify(restTemplate).getForEntity(urlAuth, AuthorizationResponse.class);
    }

    @Test
    void authorize_ShouldThrowAuthorizationException_WhenExceptionOccurs() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(AuthorizationResponse.class)))
                .thenThrow(new RuntimeException("Network error"));

        // Act & Assert
        assertThrows(AuthorizationException.class, () -> authorizationService.authorize());
        verify(restTemplate).getForEntity(urlAuth, AuthorizationResponse.class);
    }
}
