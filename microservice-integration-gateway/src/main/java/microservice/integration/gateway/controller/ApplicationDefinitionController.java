package microservice.integration.gateway.controller;

import com.github.pagehelper.PageInfo;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.gateway.common.AvailableApplicationDefinitionListVo;
import microservice.integration.gateway.common.EnabledDefinitionVo;
import microservice.integration.gateway.model.ApplicationDefinition;
import microservice.integration.gateway.service.ApplicationDefinitionService;
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
@RequestMapping("/admin/app")
public class ApplicationDefinitionController {

    @Autowired
    private ApplicationDefinitionService applicationDefinitionService;

    @GetMapping("/list")
    public Mono<ResponseResult<PageInfo<ApplicationDefinition>>> getApplicationDefinitionList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        List<ApplicationDefinition> applicationDefinitionList = applicationDefinitionService.getApplicationDefinitionList(pageNo, pageSize);
        return Mono.just(ResponseResult.success(PageInfo.of(applicationDefinitionList)));
    }

    @GetMapping("/list/available")
    public Mono<ResponseResult<List<AvailableApplicationDefinitionListVo>>> getAvailableApplicationDefinitionList(){
        List<AvailableApplicationDefinitionListVo> applicationDefinitionList = applicationDefinitionService.getAvailableApplicationDefinitionList();
        return Mono.just(ResponseResult.success(applicationDefinitionList));
    }

    @PostMapping("/enabled")
    public Mono<ResponseResult> enabledApplicationDefinition(@RequestBody EnabledDefinitionVo enabledApplicationDefinitionVo){
        applicationDefinitionService.enabledApplicationDefinition(enabledApplicationDefinitionVo.getId(),enabledApplicationDefinitionVo.isEnabled());
        return Mono.just(ResponseResult.success(""));
    }

    @PostMapping("/create")
    public Mono<ResponseResult> createApplicationDefinition(@RequestBody ApplicationDefinition applicationDefinition){
        applicationDefinitionService.saveApplicationDefinition(applicationDefinition);
        return Mono.just(ResponseResult.success("创建成功"));
    }

    @PostMapping("/update")
    public Mono<ResponseResult> updateApplicationDefinition(@RequestBody ApplicationDefinition applicationDefinition){
        if (applicationDefinition.getId()==null){
            return Mono.just(ResponseResult.error("应用id不可为空"));
        }
        applicationDefinitionService.updateApplicationDefinition(applicationDefinition);
        return Mono.just(ResponseResult.success("创建成功"));
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseResult<ApplicationDefinition>> getApplicationDefinitionById(@PathVariable("id") Integer id){
        if (Objects.isNull(id)||id==0){
            return Mono.just(ResponseResult.error("应用主键错误"));
        }
        ApplicationDefinition applicationDefinition = applicationDefinitionService.getApplicationDefinitionById(id);
        if (Objects.isNull(applicationDefinition)){
            return Mono.just(ResponseResult.error("应用不存在"));
        }
        return Mono.just(ResponseResult.success(applicationDefinition));
    }
}
