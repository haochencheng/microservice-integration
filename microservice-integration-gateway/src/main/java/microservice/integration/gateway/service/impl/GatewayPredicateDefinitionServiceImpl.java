package microservice.integration.gateway.service.impl;

import com.github.pagehelper.PageHelper;
import microservice.integration.gateway.common.PredicateDefinitionListVo;
import microservice.integration.gateway.model.GatewayPredicateDefinition;
import microservice.integration.gateway.repository.GatewayPredicateDefinitionRepository;
import microservice.integration.gateway.service.GatewayPredicateDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-06 16:11
 **/
@Service
public class GatewayPredicateDefinitionServiceImpl implements GatewayPredicateDefinitionService {

    @Autowired
    private GatewayPredicateDefinitionRepository gatewayPredicateDefinitionRepository;

    @Override
    public GatewayPredicateDefinition getPredicateDefinitionById(String id) {
        return gatewayPredicateDefinitionRepository.getPredicateDefinitionById(id);
    }

    @Override
    public void savePredicateDefinition(GatewayPredicateDefinition gatewayPredicateDefinition) {
        gatewayPredicateDefinitionRepository.savePredicateDefinition(gatewayPredicateDefinition);
    }

    @Override
    public int updatePredicateDefinition(GatewayPredicateDefinition gatewayPredicateDefinition) {
        return gatewayPredicateDefinitionRepository.updatePredicateDefinition(gatewayPredicateDefinition);
    }

    @Override
    public List<PredicateDefinitionListVo> getAllPredicateDefinitionList(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize );
        return gatewayPredicateDefinitionRepository.getAllGatewayPredicateDefinitionList();
    }

    @Override
    public void enabledPredicateDefinition(Integer id, boolean enabled) {
        gatewayPredicateDefinitionRepository.enabledPredicateDefinition(id,enabled );
    }
}
