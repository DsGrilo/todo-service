package com.grilo.todoservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.grilo.todoservice.architecture.entity.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    public String generateJWT(User user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(
                        LocalDateTime.now()
                                .plusMinutes(10)
                                .toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC512("secret_salt"));

    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC512("secret_salt"))
                .build().verify(token).getSubject();
    }
}
