package microservice.integration.gateway.service;


import microservice.integration.gateway.common.AvailableRouteDefinitionListVo;
import microservice.integration.gateway.model.GatewayRouteDefinition;

import java.util.List;

public interface GatewayRouteDefinitionService {

    GatewayRouteDefinition getRouteDefinitionById(String id);

    void saveRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition);

    int updateRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition);

    List<GatewayRouteDefinition> getAllRouteDefinitionList(int pageNo, int pageSize);

    List<AvailableRouteDefinitionListVo> getAvailableRouteDefinitionList();

    void enabledRouteDefinition(String id, boolean enabled);


}
