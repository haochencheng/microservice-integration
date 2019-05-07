package microservice.integration.gateway.common;


import microservice.integration.gateway.model.GatewayFilterDefinition;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-06 17:47
 **/
public class FilterDefinitionListVo extends GatewayFilterDefinition {

    private String routeDefinitionName;

    public String getRouteDefinitionName() {
        return routeDefinitionName;
    }

    public void setRouteDefinitionName(String routeDefinitionName) {
        this.routeDefinitionName = routeDefinitionName;
    }
}
