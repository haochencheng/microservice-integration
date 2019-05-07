package microservice.integration.gateway.service;

import microservice.integration.common.enums.auth.AuthorizationEnum;
import microservice.integration.gateway.common.ServiceDefinitionListCondition;
import microservice.integration.gateway.common.ServiceDefinitionListVo;
import microservice.integration.gateway.model.ServiceDefinition;
import org.springframework.http.HttpMethod;

import java.util.List;

public interface ServiceDefinitionService {

    void saveServiceDefinition(ServiceDefinition serviceDefinition);

    int updateServiceDefinition(ServiceDefinition serviceDefinition);

    boolean isServiceAvailable(String servicePath, HttpMethod httpMethod);

    List<ServiceDefinitionListVo> getServiceDefinitionList(ServiceDefinitionListCondition serviceDefinitionListCondition);

    void enabledServiceDefinition(Integer id, boolean enabled);

    AuthorizationEnum isNeedAuthorization(String servicePath);

    void register(List<ServiceDefinition> serviceDefinitionList);

}
