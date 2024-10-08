package com.ajay.security_service.service;

import com.ajay.security_service.entity.UserCredential;
import com.ajay.security_service.exception.UsernameExistsException;
import com.ajay.security_service.repository.UserCredentialRepository;
import com.ajay.security_service.utils.JwtService;
import com.ajay.security_service.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthenticationService authService;

    @Mock
    private UserCredentialRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSaveUser_Success() {
        UserCredential userCredential = new UserCredential();
        userCredential.setName("testUser");
        userCredential.setPassword("plainPassword");

        when(repository.findByName(userCredential.getName())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userCredential.getPassword())).thenReturn("encodedPassword");

        String response = authService.saveUser(userCredential);

        assertEquals(Constant.USER_SAVED, response);
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(repository, times(1)).save(userCredential);
        assertEquals("encodedPassword", userCredential.getPassword());
    }

    @Test
    public void testSaveUser_UsernameExists() {
        UserCredential userCredential = new UserCredential();
        userCredential.setName("existingUser");
        userCredential.setPassword("plainPassword");


        when(repository.findByName(userCredential.getName())).thenReturn(Optional.of(new UserCredential()));


        UsernameExistsException exception = assertThrows(UsernameExistsException.class, () -> {
            authService.saveUser(userCredential);
        });

        assertEquals("USER ALREADY EXISTS,TRY WITH OTHER USERNAME", exception.getMessage());


        verify(repository, never()).save(userCredential);
    }


    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String expectedToken = "generatedToken";

        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        String token = authService.generateToken(username);

        assertEquals(expectedToken, token);
        verify(jwtService, times(1)).generateToken(username);
    }

    @Test
    public void testValidateToken() {
        String token = "validToken";

        doNothing().when(jwtService).validateToken(token);

        authService.validateToken(token);

        verify(jwtService, times(1)).validateToken(token);
    }
}
