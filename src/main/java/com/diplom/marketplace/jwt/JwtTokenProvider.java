package com.diplom.marketplace.jwt;

import com.diplom.marketplace.entity.enums.Role;
import com.diplom.marketplace.exception.TokenException;
import com.diplom.marketplace.serviceImpl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;


@Component
public class JwtTokenProvider {

    Environment env;
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtTokenProvider(Environment env, UserDetailsServiceImpl userDetailsService) {
        this.env = env;
        this.userDetailsService = userDetailsService;
    }

    private String secretKey;
    private final BigInteger validityInMilliseconds = new BigInteger("31536000000"); // 1 day
    private final BigInteger refreshValidityInMilliseconds = new BigInteger("31536000000"); // 1 year
    private final long limitedValidityInMilliseconds = 1800000; // 30 min

    @PostConstruct
    protected void init() {
        secretKey = env.getProperty("jwt.key");
    }

    public String createToken(String uid, Role role) {

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("auth", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds.intValue());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

//    public String createTempToken(String value) {
//        Claims claims = Jwts.claims().setSubject(value);
//        claims.put("auth", Role.ROLE_TEMP);
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + limitedValidityInMilliseconds);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getSubject(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("auth").toString();
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email").toString();
    }

    public String exchangeToken(String refreshToken) {
        return createToken(
                getSubject(refreshToken),
                Role.valueOf(getRole(refreshToken)));
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) throws TokenException {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

}
