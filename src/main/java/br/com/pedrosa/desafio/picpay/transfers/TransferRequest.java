package br.com.pedrosa.desafio.picpay.transfers;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull @DecimalMin(value = "0.0",inclusive = false, message = "Valor da transacao deve ser no minimo 0.1") BigDecimal value,
        @NotNull(message = "Usuario da transacao deve ser informado") Long payer,
        @NotNull(message = "Lojista da transacao deve ser informado") Long payee) {

    public Transfer toEntity(TransferRequest transferRequest){
        return new Transfer(
                transferRequest.value,
                transferRequest.payer,
                transferRequest.payee);
    }

}
