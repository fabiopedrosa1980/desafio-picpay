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
import static org.mockito.ArgumentMatchers.any;
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
        transferRequest = new TransferRequest(new BigDecimal("100.0"), 1L, 2L);
        payer = new User(1L, "Payer", "12345678901", "payer@example.com", "12345", 1, new BigDecimal("500.0"));
        payee = new User(2L, "Payee", "123456789011234", "payee@example.com", "12345", 2, new BigDecimal("500.0"));
    }

    @Test
    void shouldProcessTransferSuccessfully() throws TransferException, UserNotFoundException, BalanceException {
        // Arrange
        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        when(authorizationService.authorize()).thenReturn(true);
        when(userService.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transferRepository.save(any(Transfer.class))).thenReturn(new Transfer(new BigDecimal("100.0"), payer.id(), payee.id()));

        // Act
        TransferResponse response = transferService.sendTransfer(transferRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Transferencia realizada com sucesso", response.message());
        assertEquals(new BigDecimal("400.0"), response.payer().balance());
        assertEquals(new BigDecimal("600.0"), response.payee().balance());

        verify(transferRepository, times(1)).save(any(Transfer.class));
        verify(notificationService, times(1)).sendNotification(new NotificationRequest("payee@example.com", "Transferencia recebida com sucesso"));
    }

    @Test
    void shouldThrowUserNotFoundException() throws UserNotFoundException {
        // Arrange
        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> transferService.sendTransfer(transferRequest));
    }

    @Test
    void shouldThrowBalanceException() throws UserNotFoundException, BalanceException {
        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        when(authorizationService.authorize()).thenReturn(true);
        when(transferService.sendTransfer(transferRequest)).thenThrow(new BalanceException("Saldo insuficiente"));

        // Act & Assert
        assertThrows(BalanceException.class, () -> transferService.sendTransfer(transferRequest));
    }


    @Test
    void shouldThrowTransferExceptionWhenUnauthorized() throws UserNotFoundException, BalanceException {
        // Arrange
        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        when(authorizationService.authorize()).thenThrow(new TransferException("Transferencia nao autorizada"));

        // Act & Assert
        assertThrows(TransferException.class, () -> transferService.sendTransfer(transferRequest));
    }

    @Test
    void shouldThrowTransferExceptionWhenSellerTriesToTransfer() throws UserNotFoundException, BalanceException {
        // Arrange
        when(userService.findById(1L)).thenReturn(payee);
        when(userService.findById(2L)).thenReturn(payee);

        // Act & Assert
        assertThrows(TransferException.class, () -> transferService.sendTransfer(transferRequest));
    }


}
