package com.system.controleDeRegistrosFinanceiros.authentication.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authority -> authority
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/refresh-token").permitAll()
            .requestMatchers(HttpMethod.POST,"/usuarios/registro").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/usuarios/registro").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH,"/usuarios/registro").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST,"/cidades").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/cidades").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH,"/cidades").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/funcionarios").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/funcionarios").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/funcionarios").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST,"/veiculos").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/veiculos").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH,"/veiculos").hasRole("ADMIN")
            .anyRequest().authenticated()
            )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }


@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:4200")); 
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}
