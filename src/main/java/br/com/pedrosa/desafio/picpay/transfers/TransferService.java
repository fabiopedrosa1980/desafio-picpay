package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.authorization.AuthorizationService;
import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import br.com.pedrosa.desafio.picpay.notifications.NotificationRequest;
import br.com.pedrosa.desafio.picpay.notifications.NotificationService;
import br.com.pedrosa.desafio.picpay.users.User;
import br.com.pedrosa.desafio.picpay.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private static final String SUCCESSFUL_TRANSFER = "Transferencia realizada com sucesso";
    private static final String RECEIVED_TRANSFER = "Transferencia recebida com sucesso";
    private static final String UNAUTHORIZED_TRANSFER = "Transferencia nao autorizada";
    private final TransferRepository transferRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    public TransferService(TransferRepository transferRepository, UserService userService, AuthorizationService authorizationService, NotificationService notificationService) {
        this.transferRepository = transferRepository;
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public TransferResponse sendTransfer(TransferRequest transferRequest) throws TransferException, UserNotFoundException, BalanceException {
        logger.info("Iniciando a transferencia");
        var payer = userService.findById(transferRequest.payer());
        var payee = userService.findById(transferRequest.payee());

        userService.validateUser(payer, transferRequest.value());
        authorizeTransfer();

        payer = payer.debit(transferRequest.value());
        userService.update(payer);

        payee = payee.credit(transferRequest.value());
        userService.update(payee);

        return processTransfer(transferRequest, payer, payee);
    }

    private TransferResponse processTransfer(TransferRequest transferRequest, User payer, User payee) {
        transferRepository.save(transferRequest.toEntity(transferRequest));
        sendTransferNotification(payee);

        TransferResponse transferResponse = createTransferResponse(payer, payee);
        logger.info("Transferencia finalizada");
        return transferResponse;
    }

    private void sendTransferNotification(User payee) {
        long startTime = System.currentTimeMillis();
        notificationService.send(new NotificationRequest(payee.email(), RECEIVED_TRANSFER));
        long endTime = System.currentTimeMillis();
        logger.info("Enviou as transferencias em {} segundos",(endTime-startTime)/1000);
    }

    private TransferResponse createTransferResponse(User payer, User payee) {
        return new TransferResponse(
                SUCCESSFUL_TRANSFER,
                User.toResponse(payer),
                User.toResponse(payee)
        );
    }

    private void authorizeTransfer() {
        if (!authorizationService.authorize()) {
            throw new TransferException(UNAUTHORIZED_TRANSFER);
        }
    }

}
