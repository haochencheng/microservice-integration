package microservice.integration.gateway.common;


import microservice.integration.gateway.model.GatewayPredicateDefinition;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-06 16:21
 **/
public class PredicateDefinitionListVo extends GatewayPredicateDefinition {

    private String routeDefinitionName;

    public String getRouteDefinitionName() {
        return routeDefinitionName;
    }

    public void setRouteDefinitionName(String routeDefinitionName) {
        this.routeDefinitionName = routeDefinitionName;
    }
}
