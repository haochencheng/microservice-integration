package microservice.integration.gateway.service;


import microservice.integration.gateway.common.AvailableApplicationDefinitionListVo;
import microservice.integration.gateway.model.ApplicationDefinition;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 20:59
 **/
public interface ApplicationDefinitionService {

    void saveApplicationDefinition(ApplicationDefinition applicationDefinition);

    int updateApplicationDefinition(ApplicationDefinition applicationDefinition);

    boolean isApplicationAvailable(String applicationPath);

    List<ApplicationDefinition> getApplicationDefinitionList(int pageNo, int pageSize);

    List<AvailableApplicationDefinitionListVo> getAvailableApplicationDefinitionList();

    void enabledApplicationDefinition(Integer id, boolean enabled);

    ApplicationDefinition getApplicationDefinitionById(Integer id);

    Integer register(String applicationName, String applicationPath);


}
