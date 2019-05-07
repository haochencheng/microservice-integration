package microservice.integration.gateway.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.gateway.common.AvailableRouteDefinitionListVo;
import microservice.integration.gateway.common.EnabledRouteDefinitionVo;
import microservice.integration.gateway.model.GatewayRouteDefinition;
import microservice.integration.gateway.service.GatewayRouteDefinitionService;
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
@RequestMapping("/admin/route")
public class RouteDefinitionController {

    @Autowired
    private GatewayRouteDefinitionService routeDefinitionService;

    @GetMapping("/list")
    public ResponseResult<PageInfo<GatewayRouteDefinition>> getRouteDefinitionList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        List<GatewayRouteDefinition> routeDefinitionList = routeDefinitionService.getAllRouteDefinitionList(pageNo, pageSize);
        return ResponseResult.success(PageInfo.of(routeDefinitionList));
    }

    @GetMapping("/list/available")
    public ResponseResult<List<AvailableRouteDefinitionListVo>> getAvailableRouteDefinitionList(){
        List<AvailableRouteDefinitionListVo> routeDefinitionList = routeDefinitionService.getAvailableRouteDefinitionList();
        return ResponseResult.success(routeDefinitionList);
    }

    @PostMapping("/enabled")
    public ResponseResult enabledRouteDefinition(@RequestBody EnabledRouteDefinitionVo enabledRouteDefinitionVo){
        routeDefinitionService.enabledRouteDefinition(enabledRouteDefinitionVo.getId(),enabledRouteDefinitionVo.isEnabled());
        return ResponseResult.success("");
    }

    @PostMapping("/create")
    public ResponseResult createRouteDefinition(@RequestBody GatewayRouteDefinition routeDefinition){
        routeDefinitionService.saveRouteDefinition(routeDefinition);
        return ResponseResult.success("创建成功");
    }

    @PostMapping("/update")
    public ResponseResult updateRouteDefinition(@RequestBody GatewayRouteDefinition routeDefinition){
        if (routeDefinition.getId()==null){
            return ResponseResult.error("路由id不可为空");
        }
        routeDefinitionService.updateRouteDefinition(routeDefinition);
        return ResponseResult.success("创建成功");
    }

    @GetMapping("/get/{id}")
    public ResponseResult<GatewayRouteDefinition> getRouteDefinitionById(@PathVariable("id") String id){
        if (Strings.isNullOrEmpty(id)){
            return ResponseResult.error("路由主键错误");
        }
        GatewayRouteDefinition routeDefinition = routeDefinitionService.getRouteDefinitionById(id);
        if (Objects.isNull(routeDefinition)){
            return ResponseResult.error("路由不存在");
        }
        return ResponseResult.success(routeDefinition);
    }
}
