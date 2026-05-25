package com.kuaid.security;

import com.kuaid.enums.RoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.kuaid.util.RedisUtil;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String SECRET = "kuaid-express-jwt-secret-key-must-be-at-least-256-bits-long!";
    private static final long ACCESS_TOKEN_EXPIRATION = 2 * 60 * 60 * 1000L; // 2 hours
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7 days

    private static final String REDIS_REFRESH_PREFIX = "jwt:refresh:";
    private static final String REDIS_BLACKLIST_PREFIX = "jwt:blacklist:";

    private SecretKey key;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, RoleEnum role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisUtil.set(REDIS_REFRESH_PREFIX + userId, token, 7, TimeUnit.DAYS);
        return token;
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            if (isBlacklisted(token)) {
                return false;
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String stored = redisUtil.get(REDIS_REFRESH_PREFIX + userId);
        return refreshToken.equals(stored);
    }

    public void blacklistToken(String token) {
        try {
            Claims claims = parseToken(token);
            long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (ttl > 0) {
                redisUtil.set(REDIS_BLACKLIST_PREFIX + token.hashCode(), "1", ttl, TimeUnit.MILLISECONDS);
            }
        } catch (JwtException e) {
            log.warn("Cannot blacklist invalid token");
        }
    }

    public void removeRefreshToken(Long userId) {
        redisUtil.delete(REDIS_REFRESH_PREFIX + userId);
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    public String getRoleFromToken(String token) {
        return parseToken(token).get("role", String.class);
    }

    private boolean isBlacklisted(String token) {
        return redisUtil.hasKey(REDIS_BLACKLIST_PREFIX + token.hashCode());
    }
}
