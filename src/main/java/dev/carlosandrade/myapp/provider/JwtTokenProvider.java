package dev.carlosandrade.myapp.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final String secretKey = "mySecretKey";

    // Criação do token incluindo username, roles, nome e email
    public String createToken(String username, List<String> roles, Long id, String name, String email) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        claims.put("name", name);  // Adicionando o nome ao token
        claims.put("email", email); // Adicionando o email ao token
        claims.put("id", id);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hora de validade

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Validação do token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token JWT inválido");
        }
    }

    // Resolver token a partir do header "Authorization"
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    // Obter autenticação do token
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = User.withUsername(getUsername(token))
                .password("") // Não armazenamos a senha no JWT
                .authorities(new ArrayList<>()) // Definir authorities se necessário
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Extrair o username (subject) do token
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extrair nome do token
    public String getName(String token) {
        return getClaims(token).get("name", String.class);
    }

    // Extrair email do token
    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    // Método auxiliar para obter as claims (dados) do token
    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
