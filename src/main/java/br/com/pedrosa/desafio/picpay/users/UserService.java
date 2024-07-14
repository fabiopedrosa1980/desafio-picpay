package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(UserRequest userRequest) {
        var user =  this.userRepository.save(userRequest.toEntity(userRequest));
        return new UserResponse(
                user.id(),
                user.name(),
                user.document(),
                user.email(),
                UserTypeEnum.findById(user.userType()),
                user.balance());
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario nao encontrado " + id));
    }

    public void validUser(User payer, BigDecimal value) throws BalanceException, TransferException {
        if (!hasBalance(payer, value)) {
            throw new BalanceException("Usuario com saldo insuficiente");
        }
        if (payer.userType() == UserTypeEnum.SELLER.getValue()) {
            throw new TransferException("Lojista nao pode fazer transferencia");
        }
    }

    private boolean hasBalance(User payer, BigDecimal value) {
        return payer.balance().compareTo(value) >= 0;
    }

    public User updateBalance(User user, BigDecimal value) {
        var balance = buildBalanceFromUser(user.balance(), value, user.userType());
        var userWithNewBalance = new User(
                user.id(),
                user.name(),
                user.document(),
                user.email(),
                user.password(),
                user.userType(),
                balance);
        return this.userRepository.save(userWithNewBalance);
    }

    private BigDecimal buildBalanceFromUser(BigDecimal actualBalance, BigDecimal value, int type) {
        return type == UserTypeEnum.COMMON.getValue() ?
                actualBalance.subtract(value) : actualBalance.add(value);
    }
}
