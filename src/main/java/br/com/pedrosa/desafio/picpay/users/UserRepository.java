package br.com.pedrosa.desafio.picpay.users;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User,Long> {
}
