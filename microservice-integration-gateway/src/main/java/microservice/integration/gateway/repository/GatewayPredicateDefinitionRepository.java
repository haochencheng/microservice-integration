package microservice.integration.gateway.repository;

import microservice.integration.gateway.aop.CacheRemove;
import microservice.integration.gateway.common.PredicateDefinitionListVo;
import microservice.integration.gateway.model.GatewayPredicateDefinition;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "ucommune-gateway-service")
public interface GatewayPredicateDefinitionRepository {

    @Select("select p.*,r.name as route_definition_name  from gateway_predicate_definition p,gateway_route_definition r where p.route_definition_id = r.id ")
    List<PredicateDefinitionListVo> getAllGatewayPredicateDefinitionList();

    @Insert(" INSERT INTO `gateway_predicate_definition` " +
            " (`id`,`name`, `predicate_definition_args`,`route_definition_id`,`enabled`,`desc`,`create_time`)" +
            "VALUES (#{predicateDefinition.id},#{predicateDefinition.name},#{predicateDefinition.predicateDefinitionArgs},#{predicateDefinition.routeDefinitionId},#{predicateDefinition.enabled},#{predicateDefinition.desc},#{predicateDefinition.createTime});")
    @CacheRemove(key = "GatewayPredicate:getAvailablePredicateListByRouteDefinitionId")
    int savePredicateDefinition(@Param("predicateDefinition") GatewayPredicateDefinition predicateDefinition);

    @Caching(evict = {
                    @CacheEvict(key = "'GatewayPredicate:'+#p0['id']")})
    @CacheRemove(key = "GatewayPredicate:getAvailablePredicateListByRouteDefinitionId")
    @Update("update `gateway_predicate_definition` set name=#{predicateDefinition.name},predicate_definition_args=#{predicateDefinition.predicateDefinitionArgs},`desc`=#{predicateDefinition.desc},`enabled`=#{predicateDefinition.enabled} where id = #{predicateDefinition.id}")
    int updatePredicateDefinition(@Param("predicateDefinition") GatewayPredicateDefinition predicateDefinition);

    @Update("update gateway_predicate_definition set enabled = #{enabled} where id = #{id}")
    @Caching(evict = {
            @CacheEvict(key = "'GatewayPredicate:'+#p0")})
    @CacheRemove(key = "GatewayPredicate:getAvailablePredicateListByRouteDefinitionId")
    void enabledPredicateDefinition(@Param("id") Integer id, @Param("enabled") boolean enabled);

    @Cacheable(key = "'GatewayPredicate:'+#p0")
    @Select(" select * from `gateway_predicate_definition` where id = #{id};")
    GatewayPredicateDefinition getPredicateDefinitionById(@Param("id") String id);

    @Cacheable(key = "'GatewayPredicate:getAvailablePredicateListByRouteDefinitionId'+#p0")
    @Select(" select * from `gateway_predicate_definition` where route_definition_id = #{routeDefinitionId} and enabled = 1;")
    List<GatewayPredicateDefinition> getAvailablePredicateListByRouteDefinitionId(@Param("routeDefinitionId") String routeDefinitionId);

}
