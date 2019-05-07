package microservice.integration.gateway.controller;

import com.github.pagehelper.PageInfo;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.gateway.common.EnabledDefinitionVo;
import microservice.integration.gateway.common.FilterDefinitionListVo;
import microservice.integration.gateway.model.GatewayFilterDefinition;
import microservice.integration.gateway.service.GatewayFilterDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-05 14:23
 **/
@RestController
@RequestMapping("/admin/filter")
public class FilterDefinitionController {

    @Autowired
    private GatewayFilterDefinitionService filterDefinitionService;

    @GetMapping("/list")
    public Mono<ResponseResult<PageInfo<FilterDefinitionListVo>>> getFilterDefinitionList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        List<FilterDefinitionListVo> serviceDefinitionList = filterDefinitionService.getAllFilterDefinitionList(pageNo, pageSize);
        return Mono.just(ResponseResult.success(PageInfo.of(serviceDefinitionList)));
    }

    @PostMapping("/enabled")
    public Mono<ResponseResult> enabledFilterDefinition(@RequestBody EnabledDefinitionVo enabledDefinitionVo){
        filterDefinitionService.enabledFilterDefinition(enabledDefinitionVo.getId(),enabledDefinitionVo.isEnabled());
        return Mono.just(ResponseResult.success(""));
    }

    @PostMapping("/create")
    public Mono<ResponseResult> createFilterDefinition(@RequestBody GatewayFilterDefinition gatewayFilterDefinition){
        filterDefinitionService.saveFilterDefinition(gatewayFilterDefinition);
        return Mono.just(ResponseResult.success("创建成功"));
    }

    @PostMapping("/update")
    public Mono<ResponseResult> updateFilterDefinition(@RequestBody GatewayFilterDefinition gatewayFilterDefinition){
        if (Objects.isNull(gatewayFilterDefinition.getId())||gatewayFilterDefinition.getId()==0){
            return Mono.just(ResponseResult.error("用户id不可为空"));
        }
        filterDefinitionService.updateFilterDefinition(gatewayFilterDefinition);
        return Mono.just(ResponseResult.success("创建成功"));
    }
}
