package br.com.pedrosa.desafio.picpay.exception;

import br.com.pedrosa.desafio.picpay.authorization.AuthorizationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String USUARIO_JA_CADASTRADO_COMO_ESSE_EMAIL_OU_DOCUMENTO = "Usuario ja cadastrado como esse email ou documento";

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail userNotFoundException(UserNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ProblemDetail authorizationException(AuthorizationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(TransferException.class)
    public ProblemDetail transferException(TransferException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(BalanceException.class)
    public ProblemDetail balanceException(BalanceException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(DbActionExecutionException.class)
    public ProblemDetail dataIntegrityViolationException(DbActionExecutionException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, USUARIO_JA_CADASTRADO_COMO_ESSE_EMAIL_OU_DOCUMENTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        var error = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst().orElse(null);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error);
    }

}