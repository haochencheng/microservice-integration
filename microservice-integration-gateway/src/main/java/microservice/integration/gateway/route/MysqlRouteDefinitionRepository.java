package microservice.integration.gateway.route;

import com.alibaba.fastjson.JSONObject;
import microservice.integration.gateway.model.GatewayFilterDefinition;
import microservice.integration.gateway.model.GatewayPredicateDefinition;
import microservice.integration.gateway.model.GatewayRouteDefinition;
import microservice.integration.gateway.repository.GatewayFilterDefinitionRepository;
import microservice.integration.gateway.repository.GatewayPredicateDefinitionRepository;
import microservice.integration.gateway.repository.GatewayRouteDefinitionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 16:57
 **/
@Component
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {

    @Autowired
    private GatewayRouteDefinitionRepository gatewayRouteDefinitionRepository;

    @Autowired
    private GatewayPredicateDefinitionRepository gatewayPredicateDefinitionRepository;

    @Autowired
    private GatewayFilterDefinitionRepository gatewayFilterDefinitionRepository;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<GatewayRouteDefinition> gatewayRouteDefinitionList = gatewayRouteDefinitionRepository.getAvailableGatewayRouteDefinitionList();
        if (CollectionUtils.isEmpty(gatewayRouteDefinitionList)){
            return Flux.empty();
        }
        List<GatewayRouteDefinition> routeDefinitionList=new LinkedList<>();
        gatewayRouteDefinitionList.forEach(routeDefinition -> {
            String routeDefinitionUri = routeDefinition.getRouteDefinitionUri();
            URI uri;
            try {
                uri=new URI(routeDefinitionUri);
            } catch (URISyntaxException e) {
                //TODO 日志处理
                throw new InvalidPropertyException(MysqlRouteDefinitionRepository.class,"routeDefinitionUri","绑定RouteDefinition参数uri出错！");
            }
            routeDefinition.setUri(uri);
            List<PredicateDefinition> predicates= new ArrayList<>();
            List<FilterDefinition> filters = new ArrayList<>();
            String routeDefinitionId = routeDefinition.getId();
            //构建PredicateDefinition
            List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gatewayPredicateDefinitionRepository.getAvailablePredicateListByRouteDefinitionId(routeDefinitionId);
            if (!CollectionUtils.isEmpty(gatewayPredicateDefinitionList)){
                gatewayPredicateDefinitionList.forEach(gatewayPredicateDefinition -> {
                    String predicateDefinitionArgs = gatewayPredicateDefinition.getPredicateDefinitionArgs();
                    String[] args = predicateDefinitionArgs.split(",");
                    for (int i=0; i < args.length; i++) {
                        gatewayPredicateDefinition.addArg(NameUtils.generateName(i), args[i] );
                    }
                    PredicateDefinition predicateDefinition= new PredicateDefinition();
                    BeanUtils.copyProperties(gatewayPredicateDefinition, predicateDefinition);
                    predicates.add(predicateDefinition);
                });
                routeDefinition.setPredicates(predicates);
            }
            //构建FilterDefinition
            List<GatewayFilterDefinition> gatewayFilterDefinitionList = gatewayFilterDefinitionRepository.getAvailableFilterListByRouteDefinitionId(routeDefinitionId);
            if (!CollectionUtils.isEmpty(gatewayPredicateDefinitionList)){
                gatewayFilterDefinitionList.forEach(gatewayFilterDefinition -> {
                    JSONObject param = gatewayFilterDefinition.getFilterDefinitionArgs();
                    Set<Map.Entry<String, Object>> entries = param.entrySet();
                    for (Map.Entry<String, Object> entry:entries){
                        String key = entry.getKey();
                        gatewayFilterDefinition.setName(key);
                        Object value = entry.getValue();
                        if (value instanceof String){
                            String[] args = String.valueOf(value).split(",");
                            for (int i=0; i < args.length; i++) {
                                gatewayFilterDefinition.addArg(NameUtils.generateName(i), args[i] );
                            }
                        }else if (value instanceof LinkedHashMap){
                            Set<Map.Entry<String,String>> set = ((LinkedHashMap) value).entrySet();
                            for (Map.Entry<String,String> entrySet:set){
                                gatewayFilterDefinition.addArg(entrySet.getKey() ,entrySet.getValue());
                            }
                        }
                    }
                    FilterDefinition filterDefinition=new FilterDefinition();
                    BeanUtils.copyProperties(gatewayFilterDefinition,filterDefinition);
                    filters.add(filterDefinition);
                });
                routeDefinition.setFilters(filters);
            }
            routeDefinitionList.add(routeDefinition);
        });
        return Flux.fromIterable(routeDefinitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
