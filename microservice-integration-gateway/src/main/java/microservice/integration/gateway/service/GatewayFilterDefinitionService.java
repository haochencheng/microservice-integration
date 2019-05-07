package microservice.integration.gateway.service;


import microservice.integration.gateway.common.FilterDefinitionListVo;
import microservice.integration.gateway.model.GatewayFilterDefinition;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-03 12:08
 **/
public interface GatewayFilterDefinitionService {

    GatewayFilterDefinition getFilterDefinitionById(String id);

    void saveFilterDefinition(GatewayFilterDefinition gatewayFilterDefinition);

    int updateFilterDefinition(GatewayFilterDefinition gatewayFilterDefinition);

    List<FilterDefinitionListVo> getAllFilterDefinitionList(int pageNo, int pageSize);

    void enabledFilterDefinition(Integer id, boolean enabled);
}
