package com.ajay.security_service.service;


import com.ajay.security_service.entity.UserCredential;
import com.ajay.security_service.exception.EmailExistsException;
import com.ajay.security_service.exception.UsernameExistsException;
import com.ajay.security_service.repository.UserCredentialRepository;
import com.ajay.security_service.utils.Constant;
import com.ajay.security_service.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {




    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(UserCredentialRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public String saveUser(UserCredential userCredential){
        Optional<UserCredential> userExists= repository.findByName(userCredential.getName());



        if(userExists.isPresent())
        {
            throw new UsernameExistsException(Constant.USER_EXISTS);
        }

        boolean emailExists= repository.existsByEmail(userCredential.getEmail());
      if(emailExists)
        {
            throw new EmailExistsException(Constant.EMAIL_EXISTS);
        }

        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        repository.save(userCredential);
        return Constant.USER_SAVED;
    }

    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
