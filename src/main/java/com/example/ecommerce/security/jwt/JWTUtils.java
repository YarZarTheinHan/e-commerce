package com.example.ecommerce.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.example.ecommerce.security.services.CustomUserDetails;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret; 

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs; 

    @Value("${app.jwt.cookie-name}")
    private String cookieName; 

    // public String getJwtFromHeader(HttpServletRequest request) {
    //     String bearerToken = request.getHeader("Authorization");
    //     if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
    //         return bearerToken.substring(7); 
    //     }
    //     return null;
    // }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if(cookie != null){
            return cookie.getValue();
        } else{
        return null;
        }
    }

    public ResponseCookie generateJwtCookie(CustomUserDetails customUserDetails){
        String jwt = generateTokenFromUsername(customUserDetails.getUsername());
        ResponseCookie cookie = ResponseCookie.from(cookieName, jwt)
            .path("/api")
            .maxAge(24 * 60* 60)
            .httpOnly(false)
            .build();
        return cookie;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}