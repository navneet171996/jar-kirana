package com.jar.kirana.security;

import com.jar.kirana.entities.UserToken;
import com.jar.kirana.repositories.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpiration}")
    private Long validity;

    @Value("${spring.app.jwtSecret}")
    private String secretKey;


    private final UserTokenRepository userTokenRepository;

    public JwtUtils(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }


    public String extractUsernameFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoleFromToken(String token){
        return extractClaim(token, claims -> claims.get("roles", String.class));
    }

    public boolean isValid(String token, UserDetails userDetails){
        String email = extractUsernameFromToken(token);
        boolean isLoggedOut = this.isTokenLoggedOut(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isLoggedOut;
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationFromToken(token).before(new Date());
    }

    private boolean isTokenLoggedOut(String token) {
        Optional<UserToken> tokenOptional = userTokenRepository.findByToken(token);
        if(tokenOptional.isPresent()){
            UserToken userToken = tokenOptional.get();
            return userToken.getIsLoggedOut();
        }else{
            logger.error("User token {} not found", token);
            throw new RuntimeException(String.format("User token %s not found", token));
        }
    }

    public Date extractExpirationFromToken(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts
                .builder()
                .subject(authentication.getName())
                .claim("roles", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validity*1000))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
