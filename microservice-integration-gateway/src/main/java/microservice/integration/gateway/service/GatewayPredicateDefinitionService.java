package microservice.integration.gateway.service;


import microservice.integration.gateway.common.PredicateDefinitionListVo;
import microservice.integration.gateway.model.GatewayPredicateDefinition;

import java.util.List;

public interface GatewayPredicateDefinitionService {

    GatewayPredicateDefinition getPredicateDefinitionById(String id);

    void savePredicateDefinition(GatewayPredicateDefinition gatewayPredicateDefinition);

    int updatePredicateDefinition(GatewayPredicateDefinition gatewayPredicateDefinition);

    List<PredicateDefinitionListVo> getAllPredicateDefinitionList(int pageNo, int pageSize);

    void enabledPredicateDefinition(Integer id, boolean enabled);

}
