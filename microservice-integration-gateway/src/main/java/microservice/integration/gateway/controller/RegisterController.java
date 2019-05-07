package microservice.integration.gateway.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.dto.gateway.ApplicationInfo;
import microservice.integration.common.dto.gateway.ServiceInfo;
import microservice.integration.common.lock.RedisDistributedLock;
import microservice.integration.gateway.model.ServiceDefinition;
import microservice.integration.gateway.service.ApplicationDefinitionService;
import microservice.integration.gateway.service.ServiceDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-06 17:51
 **/
@RestController
@RequestMapping("/register")
public class RegisterController {


    private final static Logger logger= LoggerFactory.getLogger(RegisterController.class);

    public static final String REGISTER_SERVICE_KEY = "registerService";
    @Autowired
    private ApplicationDefinitionService applicationDefinitionService;

    @Autowired
    private ServiceDefinitionService serviceDefinitionService;

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @PostMapping("/service")
    public Mono<ResponseResult> registerService(@RequestBody ApplicationInfo applicationInfo){
        //本地有不覆盖,如果鉴权方式不同 覆盖
        String applicationName = applicationInfo.getApplicationName();
        String applicationPath = applicationInfo.getApplicationPath();
        if (Strings.isNullOrEmpty(applicationName) || Strings.isNullOrEmpty(applicationPath)){
            return Mono.empty();
        }
        Integer applicationDefinitionId = applicationDefinitionService.register(applicationName, applicationPath);
        //上报的services
        List<ServiceInfo> serviceInfoList = applicationInfo.getServiceInfoList();
        List<ServiceDefinition> serviceDefinitions= Lists.newLinkedList();
        serviceInfoList.forEach(serviceInfo -> {
            ServiceDefinition serviceDefinition=new ServiceDefinition();
            serviceDefinition.setApplicationDefinitionId(applicationDefinitionId);
            serviceDefinition.setServiceName(serviceInfo.getMethodName());
            serviceDefinition.setServicePath(serviceInfo.getRequestUrl());
            serviceDefinition.setServiceMethod(serviceInfo.getRequestType());
            serviceDefinition.setNeedAuthorization(serviceInfo.getAuthorization().getCode());
            serviceDefinition.setDesc(serviceInfo.getMethodName());
            serviceDefinitions.add(serviceDefinition);
        });
        String key= REGISTER_SERVICE_KEY +applicationPath;
        try {
            if (redisDistributedLock.tryLock(key)){
                serviceDefinitionService.register(serviceDefinitions);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("{} report service Interrupted,cause:{}",applicationPath,e);
            return Mono.just(ResponseResult.error(e.getMessage()));
        }finally {
            redisDistributedLock.releaseLock(key);
        }
        return Mono.just(ResponseResult.success("创建成功"));
    }

}
