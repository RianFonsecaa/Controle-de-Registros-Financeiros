package com.system.controleDeRegistrosFinanceiros.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.authentication.model.UserRole;
import com.system.controleDeRegistrosFinanceiros.authentication.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        
        if (userRepository.count() == 0) {
            String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
            User admin = new User(
                "admin@gmail.com",
                encryptedPassword,
                "admin", 
                UserRole.ADMIN,
                true
            );

            userRepository.save(admin);
            System.out.println("✅ Usuário ADMIN padrão criado com sucesso: admin / admin");
        }
    }
}