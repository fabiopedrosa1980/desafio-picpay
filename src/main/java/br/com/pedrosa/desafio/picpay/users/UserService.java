package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final int USER_TYPE_USER = 1;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(UserDTO userDTO) {
        return this.userRepository.save(userDTO.toEntity(userDTO));
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario nao encontrado " + id));
    }

    public boolean hasBalance(Long idUser, BigDecimal value) throws UserNotFoundException {
        var userBalance = findById(idUser);
        return userBalance.balance().compareTo(value) >= 0;
    }

    public void updateBalance(Long id, BigDecimal value, int type) throws UserNotFoundException {
        var user = findById(id);
        var balance = buildBalanceFromUser(user.balance(), value, type);
        var userWithNewBalance = new User(
                user.id(),
                user.name(),
                user.document(),
                user.email(),
                user.password(),
                user.userType(),
                balance);
        this.userRepository.save(userWithNewBalance);
    }

    private static BigDecimal buildBalanceFromUser(BigDecimal actualBalance, BigDecimal value, int type) {
        return type == USER_TYPE_USER ?
                actualBalance.subtract(value) : actualBalance.add(value);
    }
}
