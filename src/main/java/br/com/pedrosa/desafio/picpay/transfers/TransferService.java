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
    private final TransferRepository transferRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private static final int USER_TYPE_SELLER = 2;

    public TransferService(TransferRepository transactionRepository, UserService userService, AuthorizationService authorizationService, NotificationService notificationService) {
        this.transferRepository = transactionRepository;
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public void sendTransfer(TransferDTO transactionDTO) throws TransferException, UserNotFoundException, BalanceException {
        checkBalance(transactionDTO);
        var payer = getPayer(transactionDTO);
        checkValidTransfer(payer);
        var payee = getPayee(transactionDTO);
        checkAuthorization();
        userService.updateBalance(payer.id(),transactionDTO.value(), payer.userType());
        userService.updateBalance(payee.id(),transactionDTO.value(), payee.userType());
        transferRepository.save(transactionDTO.toEntity(transactionDTO));
        notificationService.sendMessage(payee.email());
    }

    private void checkAuthorization() {
        if(!authorizationService.authorize()){
            throw new TransferException("Transferencia nao autorizada");
        }
    }

    private static void checkValidTransfer(User payer) {
        if(payer.userType() == USER_TYPE_SELLER){
            throw new TransferException("Lojista nao pode fazer transferencia");
        }
    }

    private void checkBalance(TransferDTO transactionDTO) throws BalanceException, UserNotFoundException {
        if(!userService.hasBalance(transactionDTO.payer(),transactionDTO.value())){
            throw new BalanceException("Usuario com saldo insuficiente");
        }
    }

    private User getPayee(TransferDTO transactionDTO) throws UserNotFoundException {
        return userService.findById(transactionDTO.payee());
    }

    private User getPayer(TransferDTO transactionDTO) throws UserNotFoundException {
        return userService.findById(transactionDTO.payer());
    }

}
