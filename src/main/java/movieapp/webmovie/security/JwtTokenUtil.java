package movieapp.webmovie.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecretRaw;

    private Key key;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        byte[] secretBytes = Base64.getDecoder().decode(jwtSecretRaw);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", "ROLE_" + user.getRole().name())
                .claim("authorities", List.of("ROLE_" + user.getRole().name())) // ✅ thêm authorities
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public User getUserFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (validateToken(token)) {
                String email = getEmailFromToken(token);
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            } else {
                throw new RuntimeException("Invalid JWT token");
            }
        }

        throw new RuntimeException("Missing or invalid Authorization header");
    }
}