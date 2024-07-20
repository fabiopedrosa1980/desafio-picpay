package br.com.pedrosa.desafio.picpay.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class UserTypeConfigurationTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeConfiguration userTypeConfiguration;

    @Captor
    private ArgumentCaptor<List<UserType>> userTypeListCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUserTypes() throws Exception {
        // Act
        userTypeConfiguration.run();

        // Assert
        verify(userTypeRepository).saveAll(userTypeListCaptor.capture());
        List<UserType> capturedUserTypes = userTypeListCaptor.getValue();

        assertEquals(2, capturedUserTypes.size());
        assertEquals(new UserType(1, "COMMON"), capturedUserTypes.get(0));
        assertEquals(new UserType(2, "SELLER"), capturedUserTypes.get(1));
    }
}

