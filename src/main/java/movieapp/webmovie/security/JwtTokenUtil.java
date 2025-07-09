package movieapp.webmovie.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import movieapp.webmovie.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecretRaw;

    private Key key;

    // ✅ Khởi tạo key từ chuỗi base64
    @PostConstruct
    public void init() {
        byte[] secretBytes = Base64.getDecoder().decode(jwtSecretRaw);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    // ✅ Tạo JWT Token với role đính kèm
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // email là subject
                .claim("role", "ROLE_" + user.getRole().name()) // thêm role vào payload
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Trích xuất email (subject) từ token
    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // ✅ Trích xuất role từ token
    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    // ✅ Lấy toàn bộ claims (payload) trong token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Kiểm tra token có hợp lệ không (chữ ký, định dạng, hạn)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ (Tuỳ chọn) Kiểm tra token đã hết hạn chưa
    public boolean isTokenExpired(String token) {
        Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
