package br.com.pedrosa.desafio.picpay.exception;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String USER_ALREADY_REGISTERED_WITH_EMAIL_OR_DOCUMENT = "Usuario ja cadastrado como esse email ou documento";

    @ExceptionHandler(PicPayException.class)
    public ProblemDetail picPayException(PicPayException ex) {
        return ex.problemDetail();
    }

    @ExceptionHandler(DbActionExecutionException.class)
    public ProblemDetail dbActionExecutionException() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, USER_ALREADY_REGISTERED_WITH_EMAIL_OR_DOCUMENT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ValidateError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        pb.setTitle("Os parâmetros da sua solicitação não foram validados corretamente.");
        pb.setProperty("invalid-params", errors);
        return pb;
    }

}