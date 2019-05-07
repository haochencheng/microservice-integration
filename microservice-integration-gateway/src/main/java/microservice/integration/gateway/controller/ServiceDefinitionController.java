package microservice.integration.gateway.controller;

import com.github.pagehelper.PageInfo;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.gateway.common.EnabledDefinitionVo;
import microservice.integration.gateway.common.ServiceDefinitionListCondition;
import microservice.integration.gateway.common.ServiceDefinitionListVo;
import microservice.integration.gateway.model.ServiceDefinition;
import microservice.integration.gateway.service.ServiceDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-05 14:23
 **/
@RestController
@RequestMapping("/admin/service")
public class ServiceDefinitionController {

    @Autowired
    private ServiceDefinitionService serviceDefinitionService;

    @PostMapping("/list")
    public ResponseResult<PageInfo<ServiceDefinitionListVo>> getServiceDefinitionList(@RequestBody ServiceDefinitionListCondition serviceDefinitionListCondition){
        List<ServiceDefinitionListVo> serviceDefinitionList = serviceDefinitionService.getServiceDefinitionList(serviceDefinitionListCondition);
        return ResponseResult.success(PageInfo.of(serviceDefinitionList));
    }

    @PostMapping("/enabled")
    public ResponseResult enabledServiceDefinition(@RequestBody EnabledDefinitionVo enabledDefinitionVo){
        serviceDefinitionService.enabledServiceDefinition(enabledDefinitionVo.getId(),enabledDefinitionVo.isEnabled());
        return ResponseResult.success("");
    }

    @PostMapping("/create")
    public ResponseResult createServiceDefinition(@RequestBody ServiceDefinition serviceDefinition){
        serviceDefinitionService.saveServiceDefinition(serviceDefinition);
        return ResponseResult.success("创建成功");
    }

    @PostMapping("/update")
    public ResponseResult updateServiceDefinition(@RequestBody ServiceDefinition serviceDefinition){
        if (serviceDefinition.getId()==null){
            return ResponseResult.error("服务id不可为空");
        }
        ;
        if (serviceDefinitionService.updateServiceDefinition(serviceDefinition)!=1){
            return ResponseResult.error("更新失败");
        }
        return ResponseResult.success("更新成功");
    }
}
