package microservice.integration.common.util;

import com.alibaba.fastjson.JSONObject;
import microservice.integration.common.bean.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-08 17:46
 **/
public class MonoUtil {


    public static Mono<Void> buildServerResponse(ServerHttpResponse response, HttpStatus httpStatus, String msg)  {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        try {
            return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONObject.toJSONString(ResponseResult.error(httpStatus.value(), msg)).getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONObject.toJSONString(ResponseResult.error(httpStatus.value(), "UnsupportedEncodingException")).getBytes())));
        }
    }

    public static Mono<Void> getNeedLoginResult(ServerHttpResponse response) {
        return buildServerResponse(response, -2, "认证失败，请重新登录");
    }

    public static Mono<Void> buildServerResponse(ServerHttpResponse response, int status, String msg)  {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        try {
            return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONObject.toJSONString(ResponseResult.error(status, msg)).getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONObject.toJSONString(ResponseResult.error(status, "UnsupportedEncodingException")).getBytes())));
        }
    }

}
