package microservice.integration.gateway.service.impl;

import com.github.pagehelper.PageHelper;
import microservice.integration.common.enums.auth.AuthorizationEnum;
import microservice.integration.gateway.common.ServiceDefinitionListCondition;
import microservice.integration.gateway.common.ServiceDefinitionListVo;
import microservice.integration.gateway.model.ServiceDefinition;
import microservice.integration.gateway.repository.ServiceDefinitionRepository;
import microservice.integration.gateway.service.ServiceDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 21:00
 **/
@Service
public class ServiceDefinitionServiceImpl implements ServiceDefinitionService {

    @Autowired
    private ServiceDefinitionRepository serviceDefinitionRepository;


    @Override
    public void saveServiceDefinition(ServiceDefinition serviceDefinition) {
        serviceDefinitionRepository.saveServiceDefinition(serviceDefinition);
    }

    @Override
    public int updateServiceDefinition(ServiceDefinition serviceDefinition) {
        return serviceDefinitionRepository.updateServiceDefinition(serviceDefinition);
    }

    @Override
    public boolean isServiceAvailable(String servicePath, HttpMethod httpMethod) {
        if (!serviceDefinitionRepository.isServiceExist(servicePath)) {
            return false;
        }
        int id = serviceDefinitionRepository.getServiceIdByServicePath(servicePath);
        if (id == 0) {
            return false;
        }
        ServiceDefinition serviceDefinition = serviceDefinitionRepository.getServiceDefinitionById(id);
        if (Objects.isNull(serviceDefinition)) {
            return false;
        }
//方法校验
//        if (serviceDefinition.isEnabled()&&httpMethod.matches(serviceDefinition.getServiceMethod())){
//            return true;
//        }
        if (serviceDefinition.isEnabled()) {
            return true;
        }
        return false;
    }

    @Override
    public List<ServiceDefinitionListVo> getServiceDefinitionList(ServiceDefinitionListCondition serviceDefinitionListCondition) {
        PageHelper.startPage(serviceDefinitionListCondition.getPageNo(), serviceDefinitionListCondition.getPageSize());
        return serviceDefinitionRepository.getAllServiceDefinitionList(serviceDefinitionListCondition);
    }

    @Override
    public void enabledServiceDefinition(Integer id, boolean enabled) {
        serviceDefinitionRepository.enabledServiceDefinition(id, enabled);
    }

    @Override
    public AuthorizationEnum isNeedAuthorization(String servicePath) {
        int id = serviceDefinitionRepository.getServiceIdByServicePath(servicePath);
        if (id == 0) {
            return null;
        }
        ServiceDefinition serviceDefinition = serviceDefinitionRepository.getServiceDefinitionById(id);
        return AuthorizationEnum.getEnumByCode(serviceDefinition.getNeedAuthorization());
    }

    @Override
    public synchronized void register(List<ServiceDefinition> serviceDefinitionList) {
        //上报的servicePathList 去重
        serviceDefinitionList = serviceDefinitionList.stream().filter(distinctByKey(ServiceDefinition::getServicePath)).collect(Collectors.toList());
        Set<String> servicePathSet = serviceDefinitionList.stream().map(ServiceDefinition::getServicePath).collect(Collectors.toSet());
        //已有的services
        List<ServiceDefinition> sourceServiceList = serviceDefinitionRepository.getServiceDefinitionListByServicePathList(servicePathSet);
        Set<String> sourceServicePathSet = sourceServiceList.stream().map(ServiceDefinition::getServicePath).collect(Collectors.toSet());
        Map<String, List<ServiceDefinition>> stringListMap = sourceServiceList.stream().collect(Collectors.groupingBy(ServiceDefinition::getServicePath));

        //获取插入serviceList
        List<ServiceDefinition> serviceDefinitions = serviceDefinitionList.stream().filter(serviceDefinition -> {
            String servicePath = serviceDefinition.getServicePath();
            if (!sourceServicePathSet.contains(servicePath)) {
                return true;
            }
            //如果鉴权方式不同 覆盖 默认是0 不覆盖
            int needAuthorization = serviceDefinition.getNeedAuthorization();
            if (needAuthorization != 0 && serviceDefinition.getNeedAuthorization() != stringListMap.get(servicePath).get(0).getNeedAuthorization()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(serviceDefinitions)){
            return;
        }
        serviceDefinitionRepository.saveServiceDefinitionList(serviceDefinitions);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }


}
