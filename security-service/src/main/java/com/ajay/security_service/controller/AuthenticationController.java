package com.ajay.security_service.controller;


import com.ajay.security_service.dto.AuthenticationRequest;
import com.ajay.security_service.entity.UserCredential;
import com.ajay.security_service.exception.InvalidTokenException;
import com.ajay.security_service.exception.InvalidUserDetails;
import com.ajay.security_service.exception.InvalidUserException;
import com.ajay.security_service.service.AuthenticationService;
import com.ajay.security_service.utils.Constant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(Constant.AUTHENTICATION)
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(Constant.REGISTER_URL)
    public String addNewUser(@Valid @RequestBody UserCredential userCredential, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors here are: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(error.getDefaultMessage()).append(";   ")
            );
            throw new InvalidUserDetails(errorMessage.toString());
        }
        return authService.saveUser(userCredential);
    }



    @PostMapping(Constant.TOKEN)
    public ResponseEntity<Map<String, String>> generateUser(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword())
            );
            String token = authService.generateToken(authRequest.getName());


            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new InvalidUserException(Constant.INVALID_CREDENTIALS);
        }
    }


    @GetMapping(Constant.VALIDATE)
    public String validateToken(@RequestParam("token") String token){
        try{
            authService.validateToken(token);
            return "You are valid user";
        }
        catch (Exception e){
            throw new InvalidTokenException("Your Token is invalid");
        }
    }
}
