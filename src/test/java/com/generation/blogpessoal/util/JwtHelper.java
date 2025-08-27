package com.generation.blogpessoal.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.UsuarioLogin;

public class JwtHelper {
    
    private JwtHelper() {}
    
    public static String obterToken(TestRestTemplate testRestTemplate, String email, String senha) {
        UsuarioLogin login = TestBuilder.criarUsuarioLogin(email, senha);
        HttpEntity<UsuarioLogin> request = new HttpEntity<>(login);
        
        ResponseEntity<UsuarioLogin> response = testRestTemplate
            .exchange("/usuarios/logar", HttpMethod.POST, request, UsuarioLogin.class);
        
        UsuarioLogin body = response.getBody();
        if (body != null && body.getToken() != null) {
            return body.getToken();
        }
        
        throw new RuntimeException("Falha no login: " + email);
    }
    
    public static <T> HttpEntity<T> criarRequisicaoComToken(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        String tokenLimpo = token.startsWith("Bearer ") ? token.substring(7) : token;
        headers.setBearerAuth(tokenLimpo);
        return new HttpEntity<>(body, headers);
    }
    
    public static HttpEntity<Void> criarRequisicaoComToken(String token) {
        return criarRequisicaoComToken(null, token);
    }
}