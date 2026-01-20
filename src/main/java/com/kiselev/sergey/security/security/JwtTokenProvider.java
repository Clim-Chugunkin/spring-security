package com.kiselev.sergey.security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long validityInSeconds;
    @Value("${jwt.header}")
    private String authHeader;

    private SecretKey jwtAccessSecret;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds + 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(jwtAccessSecret)
                .compact();
    }

    public Authentication getAuth(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authHeader);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }


}
