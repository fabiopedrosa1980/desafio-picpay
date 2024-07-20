package br.com.pedrosa.desafio.picpay.users;

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
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest("John Doe", "12345678901", "john@example.com", "password", UserTypeEnum.COMMON,new BigDecimal("0.00"));
        userResponse = new UserResponse(1L, "John Doe", "1234567890123", "john@example.com", UserTypeEnum.COMMON.getName(), new BigDecimal("0.00"));
    }

    @Test
    void shouldReturnCreatedStatusAndUserResponse_WhenRequestIsValid() throws Exception {
        // Arrange
        when(userService.create(any(UserRequest.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userResponse.id()))
                .andExpect(jsonPath("$.name").value(userResponse.name()))
                .andExpect(jsonPath("$.document").value(userResponse.document()))
                .andExpect(jsonPath("$.email").value(userResponse.email()));
    }

    @Test
    void shouldReturnListOfUsers() throws Exception {
        // Arrange
        List<UserResponse> users = List.of(userResponse);
        when(userService.listAll()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(userResponse.id()))
                .andExpect(jsonPath("$[0].name").value(userResponse.name()))
                .andExpect(jsonPath("$[0].document").value(userResponse.document()))
                .andExpect(jsonPath("$[0].email").value(userResponse.email()));
    }
}
