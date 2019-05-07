package microservice.integration.gateway.service.impl;

import com.github.pagehelper.PageHelper;
import microservice.integration.gateway.common.AvailableApplicationDefinitionListVo;
import microservice.integration.gateway.model.ApplicationDefinition;
import microservice.integration.gateway.repository.ApplicationDefinitionRepository;
import microservice.integration.gateway.service.ApplicationDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 20:59
 **/
@Service
public class ApplicationDefinitionServiceImpl implements ApplicationDefinitionService {

    @Autowired
    private ApplicationDefinitionRepository applicationDefinitionRepository;

    @Override
    public void saveApplicationDefinition(ApplicationDefinition applicationDefinition){
        applicationDefinitionRepository.saveApplicationDefinition(applicationDefinition);
    }

    @Override
    public int updateApplicationDefinition(ApplicationDefinition applicationDefinition) {
        return applicationDefinitionRepository.updateApplicationDefinition(applicationDefinition);
    }

    @Override
    public boolean isApplicationAvailable(String applicationPath) {
        if (!applicationDefinitionRepository.isAppExist(applicationPath)){
            return false;
        }
        int id = applicationDefinitionRepository.getAppIdByApplicationPath(applicationPath);
        return applicationDefinitionRepository.getApplicationById(id).isEnabled();
    }

    @Override
    public List<ApplicationDefinition> getApplicationDefinitionList(int pageNo,int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return applicationDefinitionRepository.getAllApplicationDefinitionList();
    }

    @Override
    public List<AvailableApplicationDefinitionListVo> getAvailableApplicationDefinitionList() {
        return applicationDefinitionRepository.getAvailableApplicationDefinitionList();
    }

    @Override
    public void enabledApplicationDefinition(Integer id, boolean enabled) {
        applicationDefinitionRepository.enabledApplicationDefinition(id,enabled);
    }

    @Override
    public ApplicationDefinition getApplicationDefinitionById(Integer id) {
        return applicationDefinitionRepository.getApplicationById(id);
    }

    @Override
    public Integer register(String applicationName, String applicationPath) {
        if (!applicationPath.startsWith("/")){
            applicationPath+="/";
        }
        int id = applicationDefinitionRepository.getAppIdByApplicationPath(applicationPath);
        if(id==0){
            ApplicationDefinition applicationDefinition = new ApplicationDefinition();
            applicationDefinition.setApplicationName(applicationName);
            applicationDefinition.setApplicationPath(applicationPath);
            id = applicationDefinitionRepository.saveApplicationDefinition(applicationDefinition);
        }
        return id;
    }


}
