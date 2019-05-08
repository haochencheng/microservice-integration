package microservice.integration.gateway.filter;

import com.google.common.base.Strings;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.dto.user.UserDto;
import microservice.integration.common.enums.auth.AuthorizationEnum;
import microservice.integration.common.util.MonoUtil;
import microservice.integration.gateway.client.AuthorizationFeignClient;
import microservice.integration.gateway.service.ApplicationDefinitionService;
import microservice.integration.gateway.service.ServiceDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 鉴权过滤器
 * @author: haochencheng
 * @create: 2018-11-01 17:52
 **/
public class GatewayServiceManagerAuthorizationFilter implements GlobalFilter, Ordered {

    private static final List<String> FileWhiteList= Arrays.asList(".js");
    public static final String FAVICON_ICO = "favicon.ico";
    public static final String TOKEN = "token";

    private Logger logger= LoggerFactory.getLogger(GatewayServiceManagerAuthorizationFilter.class);

    @Autowired
    private ApplicationDefinitionService applicationDefinitionService;

    @Autowired
    private ServiceDefinitionService serviceDefinitionService;

    @Resource
    private AuthorizationFeignClient authorizationFeignClient;

    /**
     * 服务器管理过滤器，通过应用path，服务path 对服务进行管理
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //服务管理
        URI uri = request.getURI();
        String path = uri.getPath();
        if (path.endsWith(FAVICON_ICO)){
            return chain.filter(exchange);
        }
        String appPath= getApplicationPathByRequestPath(path);
        String servicePath= getServicePathByRequestPathAndAppPath(path,appPath);
        //白名单服务，可拓展 ip 路径 等待 ...
        if (isWhiteService(servicePath)){
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        if (Strings.isNullOrEmpty(appPath)||Strings.isNullOrEmpty(servicePath)){
            return MonoUtil.buildServerResponse(response, HttpStatus.NOT_FOUND, "未找到服务");
        }
        if (!applicationDefinitionService.isApplicationAvailable(appPath)){
            return MonoUtil.buildServerResponse(response, HttpStatus.NOT_ACCEPTABLE, "未授权服务");
        }
        if (!serviceDefinitionService.isServiceAvailable(servicePath,request.getMethod())){
            return MonoUtil.buildServerResponse(response, HttpStatus.NOT_ACCEPTABLE, "未授权服务");
        }
        //鉴权
        AuthorizationEnum authorizationEnum = serviceDefinitionService.isNeedAuthorization(servicePath);
        if (authorizationEnum==null){
            return MonoUtil.buildServerResponse(response, HttpStatus.NOT_ACCEPTABLE, "服务授权配置不正确");
        }
        HttpHeaders headers = request.getHeaders();
        switch (authorizationEnum) {
            case NO:
                return chain.filter(exchange);
            case LOGIN:
                if (!headers.containsKey(TOKEN)){
                    return MonoUtil.buildServerResponse(response, HttpStatus.UNAUTHORIZED, "token无效");
                }
                ResponseResult<UserDto> userLoginResponse = authorizationFeignClient.userInfo(headers.get(TOKEN).get(0));
                if (!userLoginResponse.isSuccessful()){
                    return MonoUtil.getNeedLoginResult(response);
                }
                return chain.filter(exchange);
            case SERVER:
                if (!headers.containsKey(TOKEN)){
                    return MonoUtil.buildServerResponse(response, HttpStatus.UNAUTHORIZED, "token无效");
                }
                ResponseResult responseResult = authorizationFeignClient.authorizationServer(headers.get(TOKEN).get(0));
                if (!responseResult.isSuccessful()){
                    return MonoUtil.buildServerResponse(response, HttpStatus.UNAUTHORIZED, responseResult.getRetMsg());
                }
                return chain.filter(exchange);
            default:
                return MonoUtil.buildServerResponse(response, HttpStatus.NOT_ACCEPTABLE, "服务授权配置不正确");
        }
    }

    private String getApplicationPathByRequestPath(String requestPath){
        String[] split = requestPath.split("/");
        List<String> list = Arrays.asList(split);
        Optional<String> pathOptional = list.stream().filter(path -> !Strings.isNullOrEmpty(path) && !(path.length()==1 && path.equalsIgnoreCase("/")) ).findFirst();
//      版本一 /member-rights/aa 有问题 只取到member这个单词  应该取到/member-rights
//        Matcher matcher = pattern.matcher(requestPath);
//        if (matcher.find()){
//            return matcher.group(0);
//        }
        if (pathOptional.isPresent()){
            return "/" + pathOptional.get();
        }
        return null;
    }

    private String getServicePathByRequestPathAndAppPath(String requestPath, String appPath){
        if (Strings.isNullOrEmpty(requestPath)||Strings.isNullOrEmpty(appPath)){
            return null;
        }
        return requestPath.substring(appPath.length());
    }

    private boolean isWhiteService(String path){
        for (String s:FileWhiteList){
            Pattern filePattern=Pattern.compile( s);
            Matcher matcher = filePattern.matcher(path);
            if (matcher.find()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+50;
    }

}
