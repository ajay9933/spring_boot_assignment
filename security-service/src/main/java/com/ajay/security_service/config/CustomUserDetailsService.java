package com.ajay.security_service.config;

import com.ajay.security_service.entity.UserCredential;
import com.ajay.security_service.exception.InvalidUserException;
import com.ajay.security_service.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<UserCredential> credentials = repository.findByName(username);
        return credentials.map(CustomUserDetails::new).orElseThrow(() -> new InvalidUserException("User not found having username : " + username));
    }
}