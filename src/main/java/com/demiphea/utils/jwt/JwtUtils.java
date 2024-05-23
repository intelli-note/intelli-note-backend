package com.demiphea.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JwtUtils
 *
 * @author demiphea
 * @since 17.0.9
 */
public class JwtUtils {
    /**
     * 密钥
     */
//    private static final String SK = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER + "~!@#$%^&*()_+{}|<>?`[]\\;',./！￥……*（）——：“”《》？", 256);
    private static final String SK = "$4xSQkd.9G,+—e）d”t1@Lm”t…Wlv…l7：2>？9*<wJbTc`*$|'%*z9%F+_？$n（^)——O…）Y7》f[h？~<se—bC#av[Eof—>'FMe？5RI**<m`《dW（{'zG\\?rv：8m$—&0;*WbexTw8n：G（N^[？3>《3…i]”)^xH《1?zO5'{t*)AZ.x_Qi[sZ”&$qP`]Is+.9ehy`+$?WVV^@,￥…hy《1QxxP'/ar…N8ru90FpT*(V5NajmWxRqziH5d…{yp？￥!RI|—a6：r<]v";
    /**
     * 默认算法
     */
    private static final Algorithm DEFAULT_ALGORITHM = Algorithm.HMAC256(SK);

    /**
     * 构造Jwt Token
     *
     * @param claims    负载
     * @param survival  存活时间
     * @param unit      单位
     * @param algorithm 算法
     * @return {@link String} token
     * @author demiphea
     */
    public static String create(Map<String, Object> claims, long survival, TemporalUnit unit, Algorithm algorithm) {
        Instant expiration = Instant.now().plus(survival, unit);
        JWTCreator.Builder builder = JWT.create();
        if (claims != null) {
            claims.forEach((k, v) -> {
                if (v instanceof Boolean x) {
                    builder.withClaim(k, x);
                } else if (v instanceof Integer x) {
                    builder.withClaim(k, x);
                } else if (v instanceof Long x) {
                    builder.withClaim(k, x);
                } else if (v instanceof Double x) {
                    builder.withClaim(k, x);
                } else if (v instanceof String x) {
                    builder.withClaim(k, x);
                } else if (v instanceof Date x) {
                    builder.withClaim(k, x);
                } else if (v instanceof Instant x) {
                    builder.withClaim(k, x);
                } else if (v instanceof Map x) {
                    builder.withClaim(k, x);
                } else if (v instanceof List<?> x) {
                    builder.withClaim(k, x);
                } else if (v instanceof String[] x) {
                    builder.withArrayClaim(k, x);
                } else if (v instanceof Integer[] x) {
                    builder.withArrayClaim(k, x);
                } else if (v instanceof Long[] x) {
                    builder.withArrayClaim(k, x);
                }
            });
        }
        return builder
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    /**
     * 默认方法构造Jwt Token
     *
     * @param claims 负载
     * @return {@link String} token
     * @author demiphea
     */
    public static String createDefault(Map<String, Object> claims) {
        return create(claims, 7, ChronoUnit.DAYS, DEFAULT_ALGORITHM);
    }

    /**
     * 校验解码 Jwt Token
     *
     * @param token     token
     * @param algorithm 算法
     * @return {@link DecodedJWT} 解码器
     * @author demiphea
     */
    public static DecodedJWT verify(String token, Algorithm algorithm) {
        return getDecoder(token, algorithm);
    }

    /**
     * 校验解码默认 Jwt Token
     *
     * @param token token
     * @return {@link DecodedJWT} 解码器
     * @author demiphea
     */
    public static DecodedJWT verifyDefault(String token) {
        return verify(token, DEFAULT_ALGORITHM);
    }


    private static JWTVerifier getVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }

    private static DecodedJWT getDecoder(String token, Algorithm algorithm) {
        return getVerifier(algorithm).verify(token);
    }


}
