package microservice.integration.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import microservice.integration.common.dto.user.UserDto;
import microservice.integration.common.util.JwtUtil;
import microservice.integration.common.util.MonoUtil;
import microservice.integration.gateway.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;



/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 10:50
 **/
public class GatewayAdminAuthorizationFilter implements WebFilter, Ordered {

    public static final String ADMIN = "/admin";
    public static final String REGISTER = "/register";

    @Value("${spring.profiles.active}")
    private String profile;

    private static final List<String> whiteList= Arrays.asList("/admin/user/login","/admin/user/logout");

    @Autowired
    private RedisService<UserDto> redisService;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+40;
    }

    /**
     * 后台管理系统权限验证
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (request.getMethod().matches(HttpMethod.OPTIONS.toString())){
            return chain.filter(exchange);
        }
        URI uri = request.getURI();
        String path = uri.getPath();
        if (path.startsWith(ADMIN)){
            if (whiteList.contains(path)){
                return chain.filter(exchange);
            }
            HttpHeaders headers = request.getHeaders();
            List<String> strings = headers.get("token");
            if (CollectionUtils.isEmpty(strings)){
                return MonoUtil.getNeedLoginResult(response);
            }
            UserDto userResponse = redisService.get(strings.get(0),UserDto.class);
            if (Objects.isNull(userResponse)){
                return MonoUtil.getNeedLoginResult(response);
            }
        }else if (path.startsWith(REGISTER)){
            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (!headers.containsKey(GatewayServiceManagerAuthorizationFilter.TOKEN)){
                return MonoUtil.buildServerResponse(response, HttpStatus.UNAUTHORIZED, "token无效");
            }
            String token = headers.get(GatewayServiceManagerAuthorizationFilter.TOKEN).get(0);
            DecodedJWT decodedJWT = JwtUtil.deToken(token, JwtUtil.SECRET);
            if (Objects.isNull(decodedJWT)){
                return MonoUtil.buildServerResponse(response, HttpStatus.UNAUTHORIZED, "token无效");
            }
        }
        return chain.filter(exchange);
    }



}
