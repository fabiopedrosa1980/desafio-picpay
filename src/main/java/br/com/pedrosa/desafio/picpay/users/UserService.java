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
    private static final String USER_NOT_FOUND = "Usuario nao encontrado %s";
    private static final String INSUFFICIENT_BALANCE = "Usuario com saldo insuficiente";
    private static final String SELLER_CANNOT_TRANSFER = "Lojista nao pode fazer transferencia";

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
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND,id)));
    }

    public void validateUser(User payer, BigDecimal value) throws BalanceException, TransferException {
        logger.info("Validando se o usuario pode fazer a transferencia");
        if (!payer.hasBalance(value)) {
            logger.error(INSUFFICIENT_BALANCE);
            throw new BalanceException(INSUFFICIENT_BALANCE);
        }
        if (payer.isSeller()) {
            logger.error(SELLER_CANNOT_TRANSFER);
            throw new TransferException(SELLER_CANNOT_TRANSFER);
        }
    }

    public List<UserResponse> listAll() {
        logger.info("Pesquisando os usuarios");
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .map(User::toResponse)
                .toList();
    }

    public User update(User user) {
        return this.userRepository.save(user);
    }
}
