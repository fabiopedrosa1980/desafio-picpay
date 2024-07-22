package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.users.UserBalanceException;
import br.com.pedrosa.desafio.picpay.users.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transactionService;

    public TransferController(TransferService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransferResponse sendTransfer(@Valid @RequestBody TransferRequest transferRequest) throws TransferException, UserNotFoundException, UserBalanceException {
        return this.transactionService.sendTransfer(transferRequest);
    }
}
