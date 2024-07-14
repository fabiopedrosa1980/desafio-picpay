package br.com.pedrosa.desafio.picpay.authorization;

public record AuthorizationDTO(String status, Data data) {
}

record Data(String authorization){}
