package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.authorization.AuthorizationService;
import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import br.com.pedrosa.desafio.picpay.notifications.NotificationService;
import br.com.pedrosa.desafio.picpay.users.User;
import br.com.pedrosa.desafio.picpay.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {
    public static final String TRANSFERENCIA_REALIZADA_COM_SUCESSO = "Transferencia realizada com sucesso";
    public static final String TRANSFERENCIA_RECEBIDA_COM_SUCESSO = "Transferencia recebida com sucesso";
    public static final String TRANSFERENCIA_NAO_AUTORIZADA = "Transferencia nao autorizada";
    private final TransferRepository transferRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    public TransferService(TransferRepository transactionRepository, UserService userService, AuthorizationService authorizationService, NotificationService notificationService) {
        this.transferRepository = transactionRepository;
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public TransferResponse sendTransfer(TransferRequest transferRequest) throws TransferException, UserNotFoundException, BalanceException {
        var payer = getPayer(transferRequest);
        var payee = getPayee(transferRequest);
        userService.validUser(payer,transferRequest.value());
        checkAuthorization();
        payer = userService.updateBalance(payer, transferRequest.value());
        payee = userService.updateBalance(payee, transferRequest.value());
        transferRepository.save(transferRequest.toEntity(transferRequest));
        notificationService.sendMessage(payer.email(), TRANSFERENCIA_REALIZADA_COM_SUCESSO);
        notificationService.sendMessage(payee.email(), TRANSFERENCIA_RECEBIDA_COM_SUCESSO);
        return new TransferResponse(payer.toResponse(payer),payee.toResponse(payee));
    }

    private void checkAuthorization() {
        if(!authorizationService.authorize()){
            throw new TransferException(TRANSFERENCIA_NAO_AUTORIZADA);
        }
    }

    private User getPayee(TransferRequest transactionDTO) throws UserNotFoundException {
        return userService.findById(transactionDTO.payee());
    }

    private User getPayer(TransferRequest transactionDTO) throws UserNotFoundException {
        return userService.findById(transactionDTO.payer());
    }

}
