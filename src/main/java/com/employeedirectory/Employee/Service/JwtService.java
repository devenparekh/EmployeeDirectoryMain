package com.employeedirectory.Employee.Service;

import com.employeedirectory.UserDetails.UserDetailsEntity.UserInfo;
import com.employeedirectory.UserDetails.UserDetailsRepository.UserInfoRepository;
import com.employeedirectory.UserDetails.UserDetailsService.UserInfoUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Function;
import io.jsonwebtoken.Claims;

@Component
public class JwtService {

    UserInfoUserDetails userDetails;
    private String secret;
    private int jwtExpirationInMs;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Value("${jwt.secret}")
    public void setSecret(String secret){
        this.secret = secret;
    }

    @Value("${jwt.jwtExpirationInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs){
        this.jwtExpirationInMs=jwtExpirationInMs;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(UserInfoUserDetails userDetails) {

        Optional<UserInfo> userInfo = userInfoRepository.findByName(userDetails.getUsername());
        String userEmail = userInfo.get().getEmail();
        Map<String,Object> claims=new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            claims.put("Email",userEmail);
            if (roles.contains(new SimpleGrantedAuthority("ADMIN")) || roles.contains(new SimpleGrantedAuthority("admin"))){
                claims.put("isAdmin",true);
            }
            if (roles.contains(new SimpleGrantedAuthority("USER")) || roles.contains(new SimpleGrantedAuthority("user"))){
                claims.put("isUser",true);
            }
        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String userName){
        Map<String,String> header = new HashMap<>();
        return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userName)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationInMs))
                    .signWith(SignatureAlgorithm.HS256,Base64.getEncoder().encodeToString(secret.getBytes())).compact();
    }
}