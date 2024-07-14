package br.com.pedrosa.desafio.picpay.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("users")
public record User(@Id Long id, String name, String document, String email, String password, @Column("user_type") int userType, BigDecimal balance) {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.id,
                user.name,
                user.document,
                user.email,
                UserTypeEnum.findById(user.userType),
                user.balance
        );
    }
}
