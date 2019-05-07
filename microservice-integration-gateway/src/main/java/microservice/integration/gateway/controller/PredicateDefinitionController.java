package microservice.integration.gateway.controller;

import com.github.pagehelper.PageInfo;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.gateway.common.EnabledDefinitionVo;
import microservice.integration.gateway.common.PredicateDefinitionListVo;
import microservice.integration.gateway.model.GatewayPredicateDefinition;
import microservice.integration.gateway.service.GatewayPredicateDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-05 14:23
 **/
@RestController
@RequestMapping("/admin/predicate")
public class PredicateDefinitionController {

    @Autowired
    private GatewayPredicateDefinitionService predicateDefinitionService;

    @GetMapping("/list")
    public ResponseResult<PageInfo<PredicateDefinitionListVo>> getPredicateDefinitionList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        List<PredicateDefinitionListVo> serviceDefinitionList = predicateDefinitionService.getAllPredicateDefinitionList(pageNo, pageSize);
        return ResponseResult.success(PageInfo.of(serviceDefinitionList));
    }

    @PostMapping("/enabled")
    public ResponseResult enabledPredicateDefinition(@RequestBody EnabledDefinitionVo enabledDefinitionVo){
        predicateDefinitionService.enabledPredicateDefinition(enabledDefinitionVo.getId(),enabledDefinitionVo.isEnabled());
        return ResponseResult.success("");
    }

    @PostMapping("/create")
    public ResponseResult createPredicateDefinition(@RequestBody GatewayPredicateDefinition gatewayPredicateDefinition){
        predicateDefinitionService.savePredicateDefinition(gatewayPredicateDefinition);
        return ResponseResult.success("创建成功");
    }

    @PostMapping("/update")
    public ResponseResult updatePredicateDefinition(@RequestBody GatewayPredicateDefinition gatewayPredicateDefinition){
        if (Objects.isNull(gatewayPredicateDefinition.getId())||gatewayPredicateDefinition.getId()==0){
            return ResponseResult.error("用户id不可为空");
        }
        predicateDefinitionService.updatePredicateDefinition(gatewayPredicateDefinition);
        return ResponseResult.success("创建成功");
    }
}
