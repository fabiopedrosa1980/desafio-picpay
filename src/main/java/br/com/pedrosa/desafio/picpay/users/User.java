package br.com.pedrosa.desafio.picpay.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("users")
public record User(@Id Long id, String name, String document, String email, String password,
                   @Column("user_type") int userType, BigDecimal balance) {

    public User(String name, String document, String email, String password, int userType, BigDecimal balance) {
        this(null, name, document, email, password, userType, balance);
    }

    public User credit(BigDecimal amount){
        return new User(this.id, this.name, this.document, this.email, this.password, this.userType, this.balance.add(amount));
    }

    public User debit(BigDecimal amount){
        return new User(this.id, this.name, this.document, this.email, this.password, this.userType, this.balance.subtract(amount));
    }

    public boolean isSeller() {
        return this.userType() == UserTypeEnum.SELLER.getValue();
    }

    public boolean hasBalance(BigDecimal value) {
        return this.balance().compareTo(value) >= 0;
    }

    public static UserResponse toResponse(User user) {
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
