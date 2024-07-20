package br.com.pedrosa.desafio.picpay.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AuthorizationConfigTest {

    @InjectMocks
    private AuthorizationConfig authorizationConfig;

    @BeforeEach
    public void setUp() {
        String urlApi = "http://mockurl.com";
        ReflectionTestUtils.setField(authorizationConfig, "urlApi", urlApi);
    }

    @Test
    public void should_AuthorizationClient() {
        AuthorizationClient client = authorizationConfig.authorizationClient();
        assertNotNull(client);
    }
}
