package com.system.controleDeRegistrosFinanceiros.authentication.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.system.controleDeRegistrosFinanceiros.authentication.model.User;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration.access-minutes}")
    private long accessExpirationMinutes;

    @Value("${api.security.token.expiration.refresh-hours}")
    private long refreshExpirationHours;

    public String generateAccessToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withClaim("role", user.getRole().name())
                    .withClaim("type", "acess")
                    .withExpiresAt(genAccessTokenExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro na geração do access token!", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withClaim("type", "refresh")
                    .withExpiresAt(genRefreshTokenExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro na geração do refresh token!", exception);
        }
    }

    public String validateToken(String token){
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
    }

    public String validateRefreshToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .withClaim("type", "refresh")
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant genAccessTokenExpirationDate(){
        return LocalDateTime.now().plusMinutes(accessExpirationMinutes).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant genRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusHours(refreshExpirationHours).toInstant(ZoneOffset.of("-03:00"));
    }
}