package br.com.pedrosa.desafio.picpay.users;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
}
