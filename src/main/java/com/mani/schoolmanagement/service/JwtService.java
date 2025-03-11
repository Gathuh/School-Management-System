package com.mani.schoolmanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.mani.schoolmanagement.model.User;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@Data
public class JwtService {


    public boolean isValid(String token, User user){
        String username =extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims,T> resolver){
        Claims claims=extraAllClaims(token);
        return resolver.apply(claims);
    }
    private final String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.ew0KICAic3ViIjogIjEyMzQ1Njc4OTAiLA0KICAibmFtZSI6ICJBbmlzaCBOYXRoIiwNCiAgImlhdCI6IDE1MTYyMzkwMjINCn0.hjug4RAC1E_A0SH9IPMxtkqFOe__g_AZth6NVME1k-c";
    private Claims extraAllClaims (String token){
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String generateToken(User user){
        String token =Jwts
                .builder()
                .subject(user.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+24*60*1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }
    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
