package br.com.pedrosa.desafio.picpay.users;

import java.math.BigDecimal;

public record UserResponse(
        Long id,
        String name,
        String document,
        String email,
        String userType,
        BigDecimal balance
) {
}
