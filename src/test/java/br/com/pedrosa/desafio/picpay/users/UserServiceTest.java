package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static br.com.pedrosa.desafio.picpay.constants.Constants.USER_NOT_FOUND;
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
    void shouldCreateUserSuccessfully() {
        // Arrange
        UserRequest userRequest = new UserRequest("Payee", "987654321", "payee@example.com", "password", UserTypeEnum.COMMON, new BigDecimal("500.00"));
        when(userRepository.save(any(User.class))).thenReturn(payee);

        // Act
        UserResponse userResponse = userService.create(userRequest);

        // Assert
        assertNotNull(userResponse);
        assertEquals("Payee", userResponse.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnUser_WhenUserExists() throws UserNotFoundException {
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
    void shouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        assertEquals(String.format(USER_NOT_FOUND, 1L), exception.problemDetail().getDetail());
    }



    @Test
    void shouldUpdateUserBalance() {
        // Arrange
        BigDecimal transferValue = new BigDecimal("100.00");
        User updatedPayer = new User(1L, "Payer", "123456789", "payer@example.com", "password", UserTypeEnum.COMMON.getValue(), new BigDecimal("400.00"));
        updatedPayer = updatedPayer.debit(transferValue);
        when(userRepository.save(any(User.class))).thenReturn(updatedPayer);


        // Act
        updatedPayer = userService.update(updatedPayer);

        // Assert
        assertNotNull(updatedPayer);
        assertEquals(new BigDecimal("300.00"), updatedPayer.balance());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnListOfUsers() {
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
