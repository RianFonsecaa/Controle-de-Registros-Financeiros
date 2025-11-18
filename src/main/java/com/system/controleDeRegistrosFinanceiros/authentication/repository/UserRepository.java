package com.system.controleDeRegistrosFinanceiros.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findByLogin(String login);

    Boolean existsByLogin(String login);
    
}
