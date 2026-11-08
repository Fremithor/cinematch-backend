package co.edu.cinematch.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import java.nio.charset.StandardCharsets; import java.time.*; import java.util.*; import javax.crypto.SecretKey; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
@Service public class JwtService {
 private final SecretKey key; private final long minutes;
 public JwtService(@Value("${cinematch.jwt.secret}") String secret,@Value("${cinematch.jwt.expiration-minutes}") long minutes){this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.minutes=minutes;}
 public String crear(String correo,String rol){return Jwts.builder().subject(correo).claim("rol",rol).issuedAt(new Date()).expiration(Date.from(Instant.now().plus(Duration.ofMinutes(minutes)))).signWith(key).compact();}
 public Claims leer(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}
