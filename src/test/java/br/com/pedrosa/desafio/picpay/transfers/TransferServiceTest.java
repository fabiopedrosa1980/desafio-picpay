package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.authorization.AuthorizationService;
import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import br.com.pedrosa.desafio.picpay.notifications.NotificationEvent;
import br.com.pedrosa.desafio.picpay.users.User;
import br.com.pedrosa.desafio.picpay.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static br.com.pedrosa.desafio.picpay.Constants.RECEIVED_TRANSFER;
import static br.com.pedrosa.desafio.picpay.Constants.SUCCESSFUL_TRANSFER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TransferService transferService;

    private TransferRequest transferRequest;
    private User payer;
    private User payee;

    @BeforeEach
    void setUp() {
        transferRequest = new TransferRequest(new BigDecimal("100.0"),1L, 2L );
        payer = new User(1L, "Name","12345678901","payer@example.com","12345",1, new BigDecimal(500));
        payee = new User(2L, "Name","123456789011234","payee@example.com","12345",2, new BigDecimal(500));
    }

    @Test
    void sendTransfer_ShouldProcessTransferSuccessfully() throws TransferException, UserNotFoundException, BalanceException {
        // Arrange
        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        doNothing().when(userService).validateUser(payer, new BigDecimal("100.0"));
        when(authorizationService.authorize()).thenReturn(true);
        when(userService.updateBalance(payer, new BigDecimal("100.0"))).thenReturn(new User(1L, "Name","12345678901","payer@example.com","12345",1, new BigDecimal(400)));
        when(userService.updateBalance(payee, new BigDecimal("100.0"))).thenReturn(new User(2L, "Name","123456789011234","payee@example.com","12345",2, new BigDecimal(600)));
        doNothing().when(eventPublisher).publishEvent(new NotificationEvent(payer.email(), SUCCESSFUL_TRANSFER));
        doNothing().when(eventPublisher).publishEvent(new NotificationEvent(payee.email(), RECEIVED_TRANSFER));

        // Act
        TransferResponse transferResponse = transferService.sendTransfer(transferRequest);

        // Assert
        assertNotNull(transferResponse);
        assertEquals("Transferencia realizada com sucesso", transferResponse.message());
        verify(transferRepository).save(any(Transfer.class));
        verify(eventPublisher).publishEvent(new NotificationEvent(payer.email(), SUCCESSFUL_TRANSFER));
        verify(eventPublisher).publishEvent(new NotificationEvent(payee.email(), RECEIVED_TRANSFER));
    }

    @Test
    void sendTransfer_ShouldThrowUserNotFoundException() throws UserNotFoundException {
        // Arrange
        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> transferService.sendTransfer(transferRequest));
    }

    @Test
    void sendTransfer_ShouldThrowBalanceException() throws UserNotFoundException, BalanceException {
        // Arrange
        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        doThrow(new BalanceException("Insufficient balance")).when(userService).validateUser(payer, new BigDecimal("100.0"));

        // Act & Assert
        assertThrows(BalanceException.class, () -> transferService.sendTransfer(transferRequest));
    }

    @Test
    void sendTransfer_ShouldThrowTransferExceptionWhenUnauthorized() throws UserNotFoundException, BalanceException {
        // Arrange
        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        doNothing().when(userService).validateUser(payer, new BigDecimal("100.0"));
        when(authorizationService.authorize()).thenReturn(false);

        // Act & Assert
        assertThrows(TransferException.class, () -> transferService.sendTransfer(transferRequest));
    }

}
