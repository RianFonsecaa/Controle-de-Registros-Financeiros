package com.system.controleDeRegistrosFinanceiros.authentication.utils;

import java.io.IOException;
import java.util.UUID;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.exceptions.ErrorResponse;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.system.controleDeRegistrosFinanceiros.authentication.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = this.recoverToken(request);

        try {
            if (token != null) {
                String sub = tokenService.validateToken(token);
                UUID userId = UUID.fromString(sub);

                if (!ObjectUtils.isEmpty(userId)) {
                    User user = (User) userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", sub));

                    if (!user.getAtivo()) {
                        sendErrorResponse(response, HttpStatus.FORBIDDEN, "Sua conta foi desativada pelo administrador.");
                        return;
                    }

                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);

        } catch (TokenExpiredException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expirado. Por favor, faça login novamente.");
        } catch (JWTVerificationException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token inválido.");
        } catch (Exception ex) {
            System.err.println("Erro no Filtro: " + ex.getMessage());
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno de segurança.");
        }
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(status, message);

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}