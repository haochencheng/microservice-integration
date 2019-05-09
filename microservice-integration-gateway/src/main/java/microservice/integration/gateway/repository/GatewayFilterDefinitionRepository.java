package microservice.integration.gateway.repository;

import com.alibaba.fastjson.JSONObject;
import microservice.integration.gateway.aop.CacheRemove;
import microservice.integration.gateway.common.FilterDefinitionListVo;
import microservice.integration.gateway.config.JsonTypeHandler;
import microservice.integration.gateway.model.GatewayFilterDefinition;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "ucommune-gateway-service")
public interface GatewayFilterDefinitionRepository {

    @Select("select p.*,r.name as route_definition_name  from gateway_filter_definition p,gateway_route_definition r where p.route_definition_id = r.id ")
    @Results(value ={
            @Result(property="filterDefinitionArgs",column="filter_definition_args",javaType=JSONObject.class,jdbcType= JdbcType.VARCHAR,typeHandler= JsonTypeHandler.class),
    })
    List<FilterDefinitionListVo> getAllGatewayFilterDefinitionList();

    @Insert(" INSERT INTO `gateway_filter_definition` " +
            " (`id`,`name`, `filter_definition_args`,`route_definition_id`,`enabled`,`desc`,`create_time`)" +
            "VALUES (#{filterDefinition.id},#{filterDefinition.name},#{filterDefinition.filterDefinitionArgs,typeHandler= com.ucommune.gateway.config.JsonTypeHandler},#{filterDefinition.routeDefinitionId},#{filterDefinition.enabled},#{filterDefinition.desc},#{filterDefinition.createTime});")
    @CacheRemove(key = "GatewayFilterDefinition:getAvailableFilterListByRouteDefinitionId*")
    int saveFilterDefinition(@Param("filterDefinition") GatewayFilterDefinition filterDefinition);

    @Caching(evict = {
                    @CacheEvict(key = "'GatewayFilterDefinition:'+#p0['id']")})
    @Update("update `gateway_filter_definition` set name=#{filterDefinition.name},filter_definition_args=#{filterDefinition.filterDefinitionArgs,typeHandler= com.ucommune.gateway.config.JsonTypeHandler},`desc`=#{filterDefinition.desc},`enabled`=#{filterDefinition.enabled} where id = #{filterDefinition.id}")
    @CacheRemove(key = "GatewayFilterDefinition:getAvailableFilterListByRouteDefinitionId")
    int updateFilterDefinition(@Param("filterDefinition") GatewayFilterDefinition filterDefinition);

    @Update("update gateway_filter_definition set enabled = #{enabled} where id = #{id}")
    @Caching(evict = {
                    @CacheEvict(key = "'GatewayFilterDefinition:'+#p0")})
    @CacheRemove(key = "GatewayFilterDefinition:getAvailableFilterListByRouteDefinitionId")
    void enabledFilterDefinition(@Param("id") Integer id, @Param("enabled") boolean enabled);

    @Cacheable(key = "'GatewayFilterDefinition:'+#p0")
    @Select(" select * from `gateway_filter_definition` where id = #{id};")
    GatewayFilterDefinition getFilterDefinitionById(@Param("id") String id);

    @Cacheable(key = "'GatewayFilterDefinition:getAvailableFilterListByRouteDefinitionId'+#p0")
    @Results(value ={
            @Result(property="filterDefinitionArgs",column="filter_definition_args",javaType=JSONObject.class,jdbcType= JdbcType.VARCHAR,typeHandler= JsonTypeHandler.class),
    })
    @Select(" select * from `gateway_filter_definition` where route_definition_id = #{routeDefinitionId} and enabled = 1;")
    List<GatewayFilterDefinition> getAvailableFilterListByRouteDefinitionId(@Param("routeDefinitionId") String routeDefinitionId);


}
