package pizzapp.pizzappbackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.models.User;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    @Value("${secret}")
    private String secret;
    @Value("${tokenRefresh}")
    private int tokenRefresh;

    public String getJWTToken(User u){
        return JWT.create()
                .withSubject(u.getEmail())
                .withIssuer("PizzApp")
                .withClaim("id", u.getId())
                .withClaim("role", u.getRole())
                .withClaim("banned", u.isBanned())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenRefresh))
                .sign(Algorithm.HMAC256(secret));
    }

    public  Optional<Token> verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("PizzApp")
                .build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return Optional.of(new Token(decodedJWT));
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
}
