package microservice.integration.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JwtUtil {

    public static final String MICROSERVICE_INTEGRATION = "microservice-integration";
    public static final String SECRET = "61311bcb-cb1a-4b57-ad02-a792b3bf6adc";

    public static void main(String[] args) throws InterruptedException {
        // 生成token
        String token = getServerToken();

        // 打印token
        System.out.println("token: " + token);

        // 解密token
        DecodedJWT jwt = deToken(token, SECRET);

        System.out.println("issuer: " + jwt.getIssuer());
        System.out.println("过期时间：      " + jwt.getExpiresAt());

        Thread.sleep(5000);
        jwt = deToken(token, SECRET);

        System.out.println("issuer: " + jwt.getIssuer());

    }


    /**
     * 生成加密后的token
     *
     * @param paramMap 参数集合
     * @param expiresAt 过期时间 如果传了过期时间，过期了将解密失败。永久为null
     * @param secret 密钥
     * @return 加密后的token
     */
    public static String getToken(Map<String, String> paramMap, LocalDateTime expiresAt, String secret) {
        JWTCreator.Builder JWTBuilder = JWT.create()
                .withIssuer(MICROSERVICE_INTEGRATION);
        Set<Map.Entry<String, String>> entries = paramMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            JWTBuilder.withClaim(entry.getKey(), entry.getValue());
        }
        if (Objects.isNull(expiresAt)){
            JWTBuilder.withExpiresAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).plusMinutes(5).toInstant()));
        }else {
            JWTBuilder.withExpiresAt(Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant()));
        }
        return JWTBuilder.sign(Algorithm.HMAC256(secret));
    }

    public static String getServerToken(){
        return getToken(new HashMap<>(),null, SECRET);
    }

    /**
     * 先验证token是否被伪造，然后解码token。
     *
     * @param token 字符串token
     * @param secret 密钥
     * @return 解密后的DecodedJWT对象，可以读取token中的数据。
     */
    public static DecodedJWT deToken(final String token, String secret) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(MICROSERVICE_INTEGRATION)
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        }catch (Exception e){
            return null;
        }

    }


}
