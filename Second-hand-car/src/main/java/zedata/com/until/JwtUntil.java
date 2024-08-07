package zedata.com.until;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Map;

/**
 * @author Mr-Glacier
 */
public class JwtUntil {

    public static String  getToken(Map<String,Object> mapHeaders,String playload,String secretString)
    {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretString);
        SecretKey key = Keys.hmacShaKeyFor(apiKeySecretBytes);

        // 生成JWT
        String jwt = Jwts.builder()
                .setHeader(mapHeaders)
                .setPayload(playload)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }
}
