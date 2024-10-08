package com.ajay.security_service.controller;

import com.ajay.security_service.dto.AuthenticationRequest;
import com.ajay.security_service.entity.UserCredential;
import com.ajay.security_service.exception.InvalidTokenException;
import com.ajay.security_service.exception.InvalidUserDetails;
import com.ajay.security_service.exception.InvalidUserException;
import com.ajay.security_service.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthenticationController authController;

    @Mock
    private AuthenticationService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewUser_Success() {
        UserCredential userCredential = new UserCredential();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.saveUser(userCredential)).thenReturn("User registered successfully");

        String response = authController.addNewUser(userCredential, bindingResult);
        assertEquals("User registered successfully", response);
    }


    @Test
    public void testAddNewUser_ValidationError() {
        UserCredential userCredential = new UserCredential();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userCredential", "name", "Name is required"),
                new FieldError("userCredential", "email", "Email format is invalid")
        ));


        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            authController.addNewUser(userCredential, bindingResult);
        });


        String expectedMessage = "Validation errors here are: nameName is required;   emailEmail format is invalid;   ";
        assertTrue(exception.getMessage().contains("Validation errors here are:"));
        assertTrue(exception.getMessage().equals(expectedMessage));
    }



    @Test
    void testGenerateUser_InvalidCredentials() {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "wrongPassword");

        when(authenticationManager.authenticate(any())).thenThrow(new InvalidUserException("Invalid User Credentials"));

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("Invalid User Credentials", exception.getMessage());
    }

    @Test
    void testValidateToken_Valid() {
        String token = "validToken";

        doNothing().when(authService).validateToken(token);

        String response = authController.validateToken(token);
        assertEquals("You are valid user", response);
    }

    @Test
    void testValidateToken_Invalid() {
        String token = "invalidToken";

        doThrow(new InvalidTokenException("Invalid Token")).when(authService).validateToken(token);

        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> {
            authController.validateToken(token);
        });

        assertEquals("Your Token is invalid", exception.getMessage());
    }

    @Test
    public void testAddNewUser_MissingField() {
        UserCredential userCredential = new UserCredential();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("userCredential", "password", "Password is required")
        ));

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            authController.addNewUser(userCredential, bindingResult);
        });

        assertTrue(exception.getMessage().contains("Validation errors here are:"));
        assertTrue(exception.getMessage().contains("passwordPassword is required;   "));
    }


    @Test
    void testGenerateUser_InvalidAuthentication() {

        AuthenticationRequest authRequest = new AuthenticationRequest("user", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("INVALID_CREDENTIALS", exception.getMessage());
    }
    @Test
    void testGenerateUser_UnexpectedException() {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("Unexpected error", exception.getMessage());
    }


    @Test
    public void testGenerateUser_Success() {
   
        String username = "testUser";
        String password = "testPassword";
        String token = "dummyToken";

        AuthenticationRequest authRequest = new AuthenticationRequest(username, password);


        when(authService.generateToken(username)).thenReturn(token);


        ResponseEntity<Map<String, String>> response = authController.generateUser(authRequest);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().get("token"));
        verify(authService, times(1)).generateToken(username);
    }



}
