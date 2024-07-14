package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.users.UserResponse;

public record TransferResponse(String message, UserResponse payer, UserResponse payee) {
}
