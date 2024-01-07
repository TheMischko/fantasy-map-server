package com.fantasymap.helpers.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;

public class JwtAuthService implements AuthService{
    private SecretKey secret;
    public JwtAuthService(){
        String secretString = System.getenv("JWT_SECRET");
        byte[] secretBytes = secretString.getBytes();
        this.secret = new SecretKeySpec(secretBytes, 0, secretBytes.length, "HmacSHA256");
    }
    @Override
    public String authenticate(String username, int user_id) {
        String token = Jwts.builder()
                            .claim("username", username)
                            .claim("user_id", user_id)
                            .signWith(this.secret).compact();
        return token;
    }

    @Override
    public boolean isValid(String token){
        try {
            Jwts.parser().verifyWith(this.secret).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }

    public int getTokenUserId(String token){
        try {
            Claims claims = Jwts.parser().verifyWith(this.secret).build().parseSignedClaims(token).getPayload();
            int userId = (int)claims.get("user_id");
            return userId;
        } catch (JwtException e){
            return -1;
        }
    }
}
