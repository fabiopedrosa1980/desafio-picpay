package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.users.User;

public record TransferResponse(User payer, User payee) {
}
