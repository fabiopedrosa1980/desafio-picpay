package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;


@Service
public class UserService {
    public static final String USUARIO_NAO_ENCONTRADO = "Usuario nao encontrado ";
    public static final String USUARIO_COM_SALDO_INSUFICIENTE = "Usuario com saldo insuficiente";
    public static final String LOJISTA_NAO_PODE_FAZER_TRANSFERENCIA = "Lojista nao pode fazer transferencia";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(UserRequest userRequest) {
        var user = this.userRepository.save(userRequest.toEntity(userRequest));
        return user.toResponse(user);
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USUARIO_NAO_ENCONTRADO + id));
    }

    public void validUser(User payer, BigDecimal value) throws BalanceException, TransferException {
        if (!hasBalance(payer, value)) {
            throw new BalanceException(USUARIO_COM_SALDO_INSUFICIENTE);
        }
        if (isInvalidUserType(payer)) {
            throw new TransferException(LOJISTA_NAO_PODE_FAZER_TRANSFERENCIA);
        }
    }

    private boolean isInvalidUserType(User payer) {
        return payer.userType() == UserTypeEnum.SELLER.getValue();
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

    public List<UserResponse> listAll() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .map(user -> user.toResponse(user))
                .toList();
    }
}
