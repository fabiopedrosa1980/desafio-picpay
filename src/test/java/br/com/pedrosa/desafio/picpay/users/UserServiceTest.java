package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User payer;
    private User payee;

    @BeforeEach
    void setUp() {
        payer = new User(1L, "Payer", "123456789", "payer@example.com", "password", UserTypeEnum.COMMON.getValue(), new BigDecimal("500.00"));
        payee = new User(2L, "Payee", "987654321123", "payee@example.com", "password", UserTypeEnum.COMMON.getValue(), new BigDecimal("300.00"));
    }

    @Test
    void createUser_ShouldCreateUserSuccessfully() {
        // Arrange
        UserRequest userRequest = new UserRequest("Payee", "987654321", "payee@example.com", "password", UserTypeEnum.COMMON,new BigDecimal("500.00"));
        when(userRepository.save(any(User.class))).thenReturn(payee);

        // Act
        UserResponse userResponse = userService.create(userRequest);

        // Assert
        assertNotNull(userResponse);
        assertEquals("Payee", userResponse.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() throws UserNotFoundException {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));

        // Act
        User user = userService.findById(1L);

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.id());
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        assertEquals(String.format(UserService.USUARIO_NAO_ENCONTRADO, 1L), exception.getMessage());
    }

    @Test
    void validateUser_ShouldThrowBalanceException_WhenInsufficientBalance() {
        // Arrange
        BigDecimal transferValue = new BigDecimal("600.00");

        // Act & Assert
        BalanceException exception = assertThrows(BalanceException.class, () -> userService.validateUser(payer, transferValue));
        assertEquals(UserService.USUARIO_COM_SALDO_INSUFICIENTE, exception.getMessage());
    }

    @Test
    void validateUser_ShouldThrowTransferException_WhenUserIsSeller() {
        // Arrange
        User seller = new User(3L, "Seller", "111111111", "seller@example.com", "password", UserTypeEnum.SELLER.getValue(), new BigDecimal("1000.00"));
        BigDecimal transferValue = new BigDecimal("100.00");

        // Act & Assert
        TransferException exception = assertThrows(TransferException.class, () -> userService.validateUser(seller, transferValue));
        assertEquals(UserService.LOJISTA_NAO_PODE_FAZER_TRANSFERENCIA, exception.getMessage());
    }

    @Test
    void validateUser_ShouldPass_WhenUserIsValid() {
        // Arrange
        BigDecimal transferValue = new BigDecimal("100.00");

        // Act & Assert
        assertDoesNotThrow(() -> userService.validateUser(payer, transferValue));
    }

    @Test
    void updateBalance_ShouldUpdateUserBalance() {
        // Arrange
        BigDecimal transferValue = new BigDecimal("100.00");
        User updatedPayer = new User(1L, "Payer", "123456789", "payer@example.com", "password", UserTypeEnum.COMMON.getValue(), new BigDecimal("400.00"));
        when(userRepository.save(any(User.class))).thenReturn(updatedPayer);

        // Act
        User userWithUpdatedBalance = userService.updateBalance(payer, transferValue.negate());

        // Assert
        assertNotNull(userWithUpdatedBalance);
        assertEquals(new BigDecimal("400.00"), userWithUpdatedBalance.balance());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void listAll_ShouldReturnListOfUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(payer, payee));

        // Act
        List<UserResponse> users = userService.listAll();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }
}
