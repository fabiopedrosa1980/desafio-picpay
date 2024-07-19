package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.authorization.AuthorizationService;
import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import br.com.pedrosa.desafio.picpay.notifications.NotificationRequest;
import br.com.pedrosa.desafio.picpay.notifications.NotificationService;
import br.com.pedrosa.desafio.picpay.users.User;
import br.com.pedrosa.desafio.picpay.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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
    private NotificationService notificationService;

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
        User payer = new User(1L, "Payer", "123456789", "payer@example.com", "password", 1, BigDecimal.valueOf(1000));
        User payee = new User(2L, "Payee", "987654321", "payee@example.com", "password", 1, BigDecimal.valueOf(500));
        TransferRequest request = new TransferRequest(BigDecimal.valueOf(200),1L, 2L );

        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        doNothing().when(userService).validateUser(payer, BigDecimal.valueOf(200));
        when(authorizationService.authorize()).thenReturn(true);
        when(userService.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferResponse response = transferService.sendTransfer(request);

        assertNotNull(response);
        assertEquals("Transferencia realizada com sucesso", response.message());
        assertEquals(BigDecimal.valueOf(800), response.payer().balance());
        assertEquals(BigDecimal.valueOf(700), response.payee().balance());

        verify(transferRepository, times(1)).save(any(Transfer.class));
        verify(notificationService, times(1)).send(new NotificationRequest("payer@example.com", "Transferencia realizada com sucesso"));
        verify(notificationService, times(1)).send(new NotificationRequest("payee@example.com", "Transferencia recebida com sucesso"));

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
