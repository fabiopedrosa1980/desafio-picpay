package br.com.pedrosa.desafio.picpay.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserRequest(
        @NotBlank(message = "Nome deve ser informado") String name,
        @Size(min = 11, max = 14, message = "Documento invalido") String document,
        @Email(message = "Email invalido") String email,
        @NotBlank(message = "Senha deve ser informada") String password,
        @NotNull(message = "Tipo de usuario deve ser informado") UserTypeEnum userType,
        @NotNull(message = "Valor da transacao deve ser informado")  BigDecimal balance
) {

    public User toEntity(UserRequest userRequest){
        return new User(null,
                userRequest.name,
                userRequest.document,
                userRequest.email,
                userRequest.password,
                userRequest.userType.getValue(),
                userRequest.balance);
    }
}
