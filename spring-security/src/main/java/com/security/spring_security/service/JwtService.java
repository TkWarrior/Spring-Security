package com.security.spring_security.service;


import com.security.spring_security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey = null;

    // this will generate the jwt token based on the generateKEY
    public String generateToken(User user) {
        Map<String,Object>claims = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .issuer("talib")
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*15*1000))
                .and()
                .signWith(generateKey())
                .compact();
    }

    // This will genrate key based on the generate secretKey
    private SecretKey generateKey(){
        byte[] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }

    public String getSecretKey(){
        secretKey = "784d8bd6c3a7fa9bd7ea4a4bc99ab7686d8e17b9721f080cdca3340c6a28e8f19c93ff4566721482225fb2a651ab7f1dd0551560022a733a3c37260bc177f4d1";
        return secretKey;
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token , Function<Claims,T> claimResolver)
    {
       Claims claims = extractClaims(token);
       return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token){
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName =  extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // if the time is before the cuurent time than the token is expired otherwise not expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        // extracting expiration time from the token payload
        return extractClaims(token , Claims::getExpiration);
    }
}
