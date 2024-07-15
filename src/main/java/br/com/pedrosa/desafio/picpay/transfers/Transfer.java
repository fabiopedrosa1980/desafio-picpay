package br.com.pedrosa.desafio.picpay.transfers;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("transfers")
public record Transfer(@Id Long id, BigDecimal amount, Long payer, Long payee) {

    public Transfer(BigDecimal amount, Long payer, Long payee) {
        this(null, amount, payer, payee);
    }
}
