package br.com.pedrosa.desafio.picpay.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserTypeConfiguration implements CommandLineRunner {

    private final UserTypeRepository userTypeRepository;

    public UserTypeConfiguration(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public void run(String... args) {
        var userTypes = List.of(new UserType(1, "COMMON"), new UserType(2, "SELLER"));
        userTypeRepository.saveAll(userTypes);
    }
}
