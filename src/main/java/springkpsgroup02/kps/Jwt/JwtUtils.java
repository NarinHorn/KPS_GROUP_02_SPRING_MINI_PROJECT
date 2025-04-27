package springkpsgroup02.kps.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import springkpsgroup02.kps.Model.Entity.Profile;
import springkpsgroup02.kps.Repository.ProfileRepository;
import springkpsgroup02.kps.Service.AuthService;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final ProfileRepository profileRepository;
    private final String SECRET = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";
    private final long EXPIRATION = TimeUnit.MINUTES.toMillis(1);

    // generate token for user
    public String generateToken(String email) {
        UUID uuid = profileRepository.getUserByEmailOrUserName(email).getId();
        Map<String,UUID > claim = new HashMap<>();
        claim.put("Id",uuid);
        return Jwts.builder()
                .setClaims(claim)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, generateSignKey())
                .compact();
    }

    // generate secret key for signing token
    private SecretKey generateSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // retrieving any information from token (Need the secret key)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // extract a specific claim from the JWT token’s claims (user's info)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // retrieve email from jwt token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {
        return extractClaim(token, (claims -> claims.get("Id", String.class)));
    }

    // retrieve expiration date from jwt token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // check expired token
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // check if token is valid
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
