package br.com.pedrosa.desafio.picpay.exception;

public record FieldErrorResponse(String fieldName, String errorMessage) {
}
