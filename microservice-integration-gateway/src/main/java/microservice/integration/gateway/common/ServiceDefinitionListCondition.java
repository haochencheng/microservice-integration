package microservice.integration.gateway.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-03-14 14:16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDefinitionListCondition {

    private String serviceName;

    private String servicePath;

    private Integer applicationDefinitionId;

    private Integer pageNo = 1;

    private Integer pageSize = 10;

}
