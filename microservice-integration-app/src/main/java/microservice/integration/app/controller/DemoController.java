package microservice.integration.app.controller;

import com.google.common.collect.Lists;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.enums.auth.AuthorizationEnum;
import microservice.integration.gateway.report.annotation.GatewayInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-08 13:23
 **/
@RestController
public class DemoController {

    @GetMapping("/info")
    @GatewayInfo(name="获取app信息",authorization = AuthorizationEnum.SERVER)
    public ResponseResult getAppInfo(){
        return ResponseResult.success("appInfo");
    }

    @PostMapping("/list")
    @GatewayInfo(name="获取app list",authorization = AuthorizationEnum.LOGIN)
    public ResponseResult getList(){
        return ResponseResult.success(Lists.newArrayList(1,2,3));
    }

    @GetMapping("/no")
    @GatewayInfo(name="不需要鉴权",authorization = AuthorizationEnum.NO)
    public ResponseResult no(){
        return ResponseResult.success(Lists.newArrayList(4,5,6));
    }


}
