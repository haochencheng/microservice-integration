package microservice.integration.gateway.controller;

import microservice.integration.common.bean.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-12-04 13:55
 **/
@RestController
public class HomeController {


    @GetMapping("/favicon.ico")
    public Mono<ResponseResult> favicon(){
        return Mono.just(ResponseResult.success(""));
    }

}
