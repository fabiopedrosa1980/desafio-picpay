package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.users.UserResponse;

public record TransferResponse(UserResponse payer, UserResponse payee) {
}
