package shop.nongdam.nongdambackend.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.nongdam.nongdambackend.auth.api.dto.request.TokenRequestDTO;
import shop.nongdam.nongdambackend.global.jwt.api.dto.TokenDTO;
import shop.nongdam.nongdambackend.member.domain.Role;

@Slf4j
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TokenProvider {

    @Value("${token.expire.time.access}")
    private String accessTokenExpireTime;

    @Value("${token.expire.time.refresh}")
    private String refreshTokenExpireTime;

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = hexStringToByteArray(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String getMemberEmailFromToken(TokenRequestDTO tokenRequestDTO) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(tokenRequestDTO.authCode())
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException exception) {
            log.error("JWT expired");
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }

        return false;
    }

    public TokenDTO generateToken(String email, Role role) {
        String accessToken = generateAccessToken(email, role);
        String refreshToken = generateRefreshToken();

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDTO generateAccessTokenByRefreshToken(String email, String refreshToken) {
        String accessToken = generateAccessToken(email);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(String email) {
        Date date = new Date();
        Date accessExpiryDate = new Date(date.getTime() + Long.parseLong(accessTokenExpireTime));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(date)
                .setExpiration(accessExpiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken() {
        Date date = new Date();
        Date refreshExpiryDate = new Date(date.getTime() + Long.parseLong(refreshTokenExpireTime));

        return Jwts.builder()
                .setExpiration(refreshExpiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private byte[] hexStringToByteArray(String secret) {
        int len = secret.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(secret.charAt(i), 16) << 4)
                    + Character.digit(secret.charAt(i + 1), 16));
        }
        return data;
    }

    public Role getMemberRoleFromToken(TokenRequestDTO tokenRequestDTO) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(tokenRequestDTO.authCode())
                    .getBody();

            String roleStr = claims.get("role", String.class);
            return roleStr != null ? Role.valueOf(roleStr) : null;
        } catch (Exception e) {
            log.error("Failed to get role from token", e);
            return null;
        }
    }

    public String generateAccessToken(String email, Role role) {
        Date date = new Date();
        Date accessExpiryDate = new Date(date.getTime() + Long.parseLong(accessTokenExpireTime));

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(date)
                .setExpiration(accessExpiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
