package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public static final String USUARIO_NAO_ENCONTRADO = "Usuario nao encontrado %s";
    public static final String USUARIO_COM_SALDO_INSUFICIENTE = "Usuario com saldo insuficiente";
    public static final String LOJISTA_NAO_PODE_FAZER_TRANSFERENCIA = "Lojista nao pode fazer transferencia";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(UserRequest userRequest) {
        logger.info("Criando o usuario");
        var user = this.userRepository.save(userRequest.toEntity(userRequest));
        return User.toResponse(user);
    }

    public User findById(Long id) throws UserNotFoundException {
        logger.info("Pesquisando o usuario");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(USUARIO_NAO_ENCONTRADO,id)));
    }

    public void validateUser(User payer, BigDecimal value) throws BalanceException, TransferException {
        logger.info("Validando se o usuario pode fazer a transferencia");
        if (!hasBalance(payer, value)) {
            logger.error(USUARIO_COM_SALDO_INSUFICIENTE);
            throw new BalanceException(USUARIO_COM_SALDO_INSUFICIENTE);
        }
        if (isInvalidUserType(payer)) {
            logger.error(LOJISTA_NAO_PODE_FAZER_TRANSFERENCIA);
            throw new TransferException(LOJISTA_NAO_PODE_FAZER_TRANSFERENCIA);
        }
    }

    private boolean isInvalidUserType(User payer) {
        logger.info("Verificando se o usuario é lojista");
        return payer.userType() == UserTypeEnum.SELLER.getValue();
    }

    private boolean hasBalance(User payer, BigDecimal value) {
        logger.info("Verificando o saldo do usuario");
        return payer.balance().compareTo(value) >= 0;
    }

    public User updateBalance(User user, BigDecimal value) {
        logger.info("Atualizando o saldo do {}", UserTypeEnum.findById(user.userType()));
        var balance = buildBalanceFromUser(user.balance(), value, user.userType());
        var userWithNewBalance = getUserWithNewBalance(user, balance);
        return this.userRepository.save(userWithNewBalance);
    }

    private User getUserWithNewBalance(User user, BigDecimal balance) {
        return new User(
                user.id(),
                user.name(),
                user.document(),
                user.email(),
                user.password(),
                user.userType(),
                balance);
    }

    private BigDecimal buildBalanceFromUser(BigDecimal actualBalance, BigDecimal value, int type) {
        return type == UserTypeEnum.COMMON.getValue() ?
                actualBalance.subtract(value) : actualBalance.add(value);
    }

    public List<UserResponse> listAll() {
        logger.info("Pesquisando os usuarios");
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .map(User::toResponse)
                .toList();
    }
}
