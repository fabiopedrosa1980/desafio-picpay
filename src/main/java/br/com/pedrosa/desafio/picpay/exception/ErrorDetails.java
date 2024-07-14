package br.com.pedrosa.desafio.picpay.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorDetails(@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime timesstamp, String message, String details) {
}
