package com.SBI.security.services;

//import com.SBI.security.security.CustomLoginFailureHandler;
//import com.SBI.security.security.CustomLoginSuccessHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

/*
    private final CustomLoginSuccessHandler authenticationSuccessHandler;
    private final CustomLoginFailureHandler authenticationFailureHandler;
*/
    //constant secret key that must change
    private static final String PRIVATE_KEY_PATH = "C:\\Users\\ihebt\\Documents\\security\\security\\src\\main\\resources/private.key";

    //private static final String PUBLIC_KEY_PATH = "C:\\Users\\ihebt\\Documents\\security\\security\\src\\main\\resources/public.key";

    /*    public JwtService(CustomLoginSuccessHandler authenticationSuccessHandler, CustomLoginFailureHandler authenticationFailureHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }*/

    public String extractUserEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }


    /*public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }*/

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply((claims));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String useremail = extractUserEmail(token);
        return (useremail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwt_token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt_token)
                .getBody();
    }

    private Key getSignInKey() {
        try {
            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_PATH));
            return Keys.hmacShaKeyFor(privateKeyBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read private key file", e);
        }
    }
}
