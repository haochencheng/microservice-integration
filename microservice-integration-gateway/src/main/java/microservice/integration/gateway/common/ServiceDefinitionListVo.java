package microservice.integration.gateway.common;

import lombok.Data;
import microservice.integration.gateway.model.ServiceDefinition;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-06 11:59
 **/
@Data
public class ServiceDefinitionListVo extends ServiceDefinition {

    private String applicationName;

}
