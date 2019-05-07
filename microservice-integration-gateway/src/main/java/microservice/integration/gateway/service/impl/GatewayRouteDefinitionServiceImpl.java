package microservice.integration.gateway.service.impl;

import com.github.pagehelper.PageHelper;
import microservice.integration.gateway.common.AvailableRouteDefinitionListVo;
import microservice.integration.gateway.model.GatewayRouteDefinition;
import microservice.integration.gateway.repository.GatewayRouteDefinitionRepository;
import microservice.integration.gateway.service.GatewayRouteDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 18:19
 **/
@Service
public class GatewayRouteDefinitionServiceImpl implements GatewayRouteDefinitionService {

    @Autowired
    private GatewayRouteDefinitionRepository gatewayRouteDefinitionRepository;

    @Override
    public GatewayRouteDefinition getRouteDefinitionById(String id) {
        return gatewayRouteDefinitionRepository.getRouteDefinitionById(id);
    }

    @Override
    public void saveRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition) {
        gatewayRouteDefinitionRepository.saveRouteDefinition(gatewayRouteDefinition);
    }

    @Override
    public int updateRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition) {
        return gatewayRouteDefinitionRepository.updateRouteDefinition(gatewayRouteDefinition);
    }

    @Override
    public List<GatewayRouteDefinition> getAllRouteDefinitionList(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize );
        return gatewayRouteDefinitionRepository.getAllGatewayRouteDefinitionList();
    }

    @Override
    public List<AvailableRouteDefinitionListVo> getAvailableRouteDefinitionList() {
        return gatewayRouteDefinitionRepository.getAvailableRouteDefinitionList();
    }

    @Override
    public void enabledRouteDefinition(String id, boolean enabled) {
        gatewayRouteDefinitionRepository.enabledRouteDefinition(id,enabled );
    }
}
