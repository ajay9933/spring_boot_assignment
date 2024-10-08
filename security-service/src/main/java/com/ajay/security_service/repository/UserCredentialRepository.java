package com.ajay.security_service.repository;

import com.ajay.security_service.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential,Integer> {
    Optional<UserCredential> findByName(String username);

    boolean existsByEmail(String email);

}
