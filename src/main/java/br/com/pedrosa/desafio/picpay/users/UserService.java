package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String USER_NOT_FOUND = "Usuario nao encontrado %s";

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
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, id)));
    }


    public List<UserResponse> listAll() {
        logger.info("Pesquisando os usuarios");
        return this.userRepository.findAll().stream()
                .map(User::toResponse)
                .toList();
    }

    public User update(User user) {
        return this.userRepository.save(user);
    }
}
