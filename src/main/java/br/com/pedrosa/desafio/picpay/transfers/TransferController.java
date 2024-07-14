package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
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
    public void sendTransfer(@Valid @RequestBody TransferDTO transactionDTO) throws TransferException,UserNotFoundException, BalanceException {
        this.transactionService.sendTransfer(transactionDTO);
    }
}
