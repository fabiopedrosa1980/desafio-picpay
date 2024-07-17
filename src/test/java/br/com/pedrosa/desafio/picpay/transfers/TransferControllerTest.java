package br.com.pedrosa.desafio.picpay.transfers;

import br.com.pedrosa.desafio.picpay.exception.BalanceException;
import br.com.pedrosa.desafio.picpay.exception.TransferException;
import br.com.pedrosa.desafio.picpay.exception.UserNotFoundException;
import br.com.pedrosa.desafio.picpay.users.UserResponse;
import br.com.pedrosa.desafio.picpay.users.UserTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransferRequest transferRequest;
    private TransferResponse transferResponse;

    @BeforeEach
    void setUp() {
        transferRequest = new TransferRequest(new BigDecimal("100.00"), 1L, 2L);
        transferResponse = new TransferResponse("Transferencia realizada com sucesso",
                new UserResponse(1L, "Payer", "123456789", "payer@example.com", UserTypeEnum.COMMON.getName(), new BigDecimal(400.0)),
                new UserResponse(2L, "Payee", "987654321123", "payee@example.com", UserTypeEnum.SELLER.getName(), new BigDecimal(800.0)));
    }

    @Test
    void sendTransfer_ShouldReturnTransferResponse_WhenRequestIsValid() throws Exception {
        // Arrange
        when(transferService.sendTransfer(any(TransferRequest.class))).thenReturn(transferResponse);

        // Act & Assert
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transferencia realizada com sucesso"))
                .andExpect(jsonPath("$.payer.id").value(1L))
                .andExpect(jsonPath("$.payer.document").value("123456789"))
                .andExpect(jsonPath("$.payer.email").value("payer@example.com"))
                .andExpect(jsonPath("$.payer.userType").value("COMMON"))
                .andExpect(jsonPath("$.payer.balance").value(new BigDecimal(400.0)))
                .andExpect(jsonPath("$.payee.id").value(2L))
                .andExpect(jsonPath("$.payee.document").value("987654321123"))
                .andExpect(jsonPath("$.payee.email").value("payee@example.com"))
                .andExpect(jsonPath("$.payee.userType").value("SELLER"))
                .andExpect(jsonPath("$.payee.balance").value(new BigDecimal(800.0)));


        verify(transferService).sendTransfer(any(TransferRequest.class));
    }

    @Test
    void sendTransfer_ShouldReturnError_WhenTransferExceptionOccurs() throws Exception {
        // Arrange
        doThrow(new TransferException("Transferencia nao autorizada"))
                .when(transferService).sendTransfer(any(TransferRequest.class));

        // Act & Assert
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Transferencia nao autorizada"));

        verify(transferService).sendTransfer(any(TransferRequest.class));
    }

    @Test
    void sendTransfer_ShouldReturnError_WhenUserNotFoundExceptionOccurs() throws Exception {
        // Arrange
        doThrow(new UserNotFoundException("Usuario nao encontrado"))
                .when(transferService).sendTransfer(any(TransferRequest.class));

        // Act & Assert
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Usuario nao encontrado"));

        verify(transferService).sendTransfer(any(TransferRequest.class));
    }

    @Test
    void sendTransfer_ShouldReturnError_WhenBalanceExceptionOccurs() throws Exception {
        // Arrange
        doThrow(new BalanceException("Saldo insuficiente"))
                .when(transferService).sendTransfer(any(TransferRequest.class));

        // Act & Assert
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Saldo insuficiente"));

        verify(transferService).sendTransfer(any(TransferRequest.class));
    }
}
