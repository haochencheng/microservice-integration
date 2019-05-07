package microservice.integration.gateway.service.impl;

import com.github.pagehelper.PageHelper;
import microservice.integration.gateway.common.FilterDefinitionListVo;
import microservice.integration.gateway.model.GatewayFilterDefinition;
import microservice.integration.gateway.repository.GatewayFilterDefinitionRepository;
import microservice.integration.gateway.service.GatewayFilterDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-03 12:08
 **/
@Service
public class GatewayFilterDefinitionServiceImpl  implements GatewayFilterDefinitionService {

    @Autowired
    private GatewayFilterDefinitionRepository gatewayFilterDefinitionRepository;


    @Override
    public GatewayFilterDefinition getFilterDefinitionById(String id) {
        return gatewayFilterDefinitionRepository.getFilterDefinitionById(id);
    }

    @Override
    public void saveFilterDefinition(GatewayFilterDefinition gatewayFilterDefinition) {
        gatewayFilterDefinitionRepository.saveFilterDefinition(gatewayFilterDefinition);
    }

    @Override
    public int updateFilterDefinition(GatewayFilterDefinition gatewayFilterDefinition) {
        return gatewayFilterDefinitionRepository.updateFilterDefinition(gatewayFilterDefinition);
    }

    @Override
    public List<FilterDefinitionListVo> getAllFilterDefinitionList(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize );
        return gatewayFilterDefinitionRepository.getAllGatewayFilterDefinitionList();
    }

    @Override
    public void enabledFilterDefinition(Integer id, boolean enabled) {
        gatewayFilterDefinitionRepository.enabledFilterDefinition(id, enabled);
    }
}
