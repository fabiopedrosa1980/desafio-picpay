package br.com.pedrosa.desafio.picpay.authorization;

public record AuthorizationResponse(String status, Data data) {
}

record Data(String authorization){}
