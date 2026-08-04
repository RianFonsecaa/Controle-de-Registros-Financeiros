package com.system.controleDeRegistrosFinanceiros.authentication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<UserDetails> findByLogin(String login);

    Boolean existsByLogin(String login);

    Page<User> findByAtivo(boolean ativo, Pageable pageable);

    List<User> findByAtivoTrueOrderByNameAsc();
    
}
