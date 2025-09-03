package com.ecommerce_pineaster.pi_eco.security.jwt;
import com.ecommerce_pineaster.pi_eco.security.serivces.UserDetailImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.jwt.expiration}")
    private Long  jwtExpiration;

    @Value("${spring.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.ecomm.app.jwtCookieName}")
    private String jwtCookie;


    public static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    //Getting Jwt from headers
//    public String getJwtFromHeader(HttpServletRequest request){
//
//        String bearerToken = request.getHeader("Authorization");
//        logger.debug("Authorization Header :{} ",bearerToken);
//        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
//            return  bearerToken.substring(7);
//
//        }
//
//        return null;
//
//    }

    public  String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);
        if(cookie != null) {
            return cookie.getValue();
        }else {
            return null;
        }
    }
    public ResponseCookie generateJwtCookie(UserDetailImpl userDetail ){
        String jwt = generateTokenFromUserName(userDetail.getUsername());
         ResponseCookie  cookie = ResponseCookie.from(jwtCookie,jwt)
                 .path("/api")
                 .maxAge(24*60*60)
                 .httpOnly(false)
                 .build();
         return cookie;

    }

    public ResponseCookie getCleanJwtCookie( ) {
        ResponseCookie  cookie = ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
        return cookie;

    }


    //Getting token form username
    public  String generateTokenFromUserName(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(key())
                .compact();

    }

    //Getting userName from token
    public String getUserNameFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //generating key

    public Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

   // Validate JWT token
   public boolean validateJwtToken(String authToken) {
       try {
           System.out.println("Validate");

           // This will throw an exception if the token is invalid
           Jwts.parser()
                   .verifyWith((SecretKey) key())
                   .build()
                   .parseSignedClaims(authToken);

           return true; // valid

       } catch (MalformedJwtException e) {
           System.err.println("Invalid JWT token: " + e.getMessage());
       } catch (ExpiredJwtException e) {
           System.err.println("JWT token is expired: " + e.getMessage());
       } catch (UnsupportedJwtException e) {
           System.err.println("JWT token is unsupported: " + e.getMessage());
       } catch (IllegalArgumentException e) {
           System.err.println("JWT claims string is empty: " + e.getMessage());
       }

       return false; // invalid
   }



}
