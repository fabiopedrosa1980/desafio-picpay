package br.com.pedrosa.desafio.picpay.transfers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @Min(value = 1, message = "Valor da transacao deve ser informado") BigDecimal value,
        @NotNull(message = "Usuario da transacao deve ser informado") Long payer,
        @NotNull(message = "Lojista da transacao deve ser informado") Long payee) {

    public Transfer toEntity(TransferRequest transferRequest){
        return new Transfer(
                null,
                transferRequest.value,
                transferRequest.payer,
                transferRequest.payee);
    }

}
