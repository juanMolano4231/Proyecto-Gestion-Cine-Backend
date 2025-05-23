/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.services;

import api.models.Usuario;
import api.repositories.UsuarioRepository;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author johan
 */
@Service
public class JWTService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuarioRepository.class);

    @Value("${JWT_SECRET}")
    private String secretCode;

    private final long EXPIRATION_TIME_MS = 600000;
    private SecretKey jwtSecret;

    @PostConstruct
    public void init() {
        if (secretCode == null || secretCode.trim().isEmpty()) {
            throw new IllegalStateException("JWT_SECRET must be configured in application.properties");
        }
        
        logger.info("jwtSecret: {}", jwtSecret);

        this.jwtSecret = Keys.hmacShaKeyFor(secretCode.getBytes(StandardCharsets.UTF_8));
        
        logger.info("jwtSecret: {}", jwtSecret);

    }

    public String generarToken(Usuario usuario, String tipo) {
        return Jwts.builder()
                .setSubject(usuario.getUsuario())
                .claim("tipo", tipo)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String obtenerUsuario(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public String obtenerTipo(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("tipo", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
