package microservice.integration.gateway.config;

import microservice.integration.common.util.ProfileUtil;
import microservice.integration.gateway.filter.GatewayAdminAuthorizationFilter;
import microservice.integration.gateway.filter.GatewayServiceManagerAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 18:34
 **/
@Configuration
public class GatewayConfiguration {

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,Gateway-Token,Origin , Cookie, Accept, server ";
    private static final String ALLOWED_METHODS = "POST,GET,OPTIONS,PUT,DELETE";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String MAX_AGE = "18000L";

    private final static Logger logger = LoggerFactory.getLogger(GatewayConfiguration.class);

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (!ProfileUtil.nonProd()){
                logger.info("requestPath:{}", request.getPath());
                logger.info("isCorsRequest:{}", CorsUtils.isCorsRequest(request));
                logger.info("RemoteAddress:{}", request.getRemoteAddress());
                logger.info("cookie:{}",request.getCookies().toString());
            }
            if (CorsUtils.isCorsRequest(request)){
                List<String> originStrings = request.getHeaders().get(HttpHeaders.ORIGIN);
                if (!ProfileUtil.nonProd()){
                    logger.info("originStrings:{}", originStrings.toString());
                }
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", originStrings.get(0));
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS + ",userId");
                headers.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    @Bean
    public GlobalFilter gatewayServiceManagerAuthorizationFilter() {
        return new GatewayServiceManagerAuthorizationFilter();
    }

    @Bean
    public WebFilter gatewayAdminAuthorizationFilter() {
        return new GatewayAdminAuthorizationFilter();
    }


}
