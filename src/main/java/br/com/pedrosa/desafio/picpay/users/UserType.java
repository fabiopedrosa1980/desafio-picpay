package br.com.pedrosa.desafio.picpay.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_type")
public record UserType(@Id int id, String name) {
}
