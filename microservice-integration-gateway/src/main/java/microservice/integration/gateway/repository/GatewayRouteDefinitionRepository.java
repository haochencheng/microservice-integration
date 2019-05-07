package microservice.integration.gateway.repository;

import microservice.integration.gateway.common.AvailableRouteDefinitionListVo;
import microservice.integration.gateway.config.RedisConfig;
import microservice.integration.gateway.model.GatewayRouteDefinition;
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
@CacheConfig(cacheNames = RedisConfig.GATEWAY_SERVICE)
public interface GatewayRouteDefinitionRepository {

    @Select(" select * from `gateway_route_definition` where enabled = 1;")
    @Cacheable(key = "'GatewayRoute:'+'getAvailableGatewayRouteDefinitionList'")
    List<GatewayRouteDefinition> getAvailableGatewayRouteDefinitionList();

    @Select(" select * from `gateway_route_definition`")
    List<GatewayRouteDefinition> getAllGatewayRouteDefinitionList();

    @Insert(" INSERT INTO `gateway_route_definition` " +
            " (`id`,`name`, `route_definition_uri`,`enabled`, `order`,`desc`,`create_time`)" +
            "VALUES (#{routeDefinition.id},#{routeDefinition.name}, #{routeDefinition.routeDefinitionUri},#{routeDefinition.enabled},#{routeDefinition.order},#{routeDefinition.desc},#{routeDefinition.createTime});")
    @Caching(evict = {
            @CacheEvict(key = "'GatewayRoute:'+'getAvailableGatewayRouteDefinitionList'"),
            @CacheEvict(key = "'GatewayRoute:'+'getAvailableRouteDefinitionList'")})
    int saveRouteDefinition(@Param("routeDefinition") GatewayRouteDefinition routeDefinition);

    @Caching(evict = {
                    @CacheEvict(key = "'GatewayRoute:'+#p0['id']"),
                    @CacheEvict(key = "'GatewayRoute:'+'getAvailableGatewayRouteDefinitionList'"),
                    @CacheEvict(key = "'GatewayRoute:'+'getAvailableRouteDefinitionList'")})
    @Update("update `gateway_route_definition` set name=#{routeDefinition.name},route_definition_uri=#{routeDefinition.routeDefinitionUri},`desc`=#{routeDefinition.desc},`enabled`=#{routeDefinition.enabled},`order`=#{routeDefinition.order}  where id = #{routeDefinition.id}")
    int updateRouteDefinition(@Param("routeDefinition") GatewayRouteDefinition routeDefinition);

    @Update("update gateway_route_definition set enabled = #{enabled} where id = #{id}")
    @Caching(
            evict = {@CacheEvict(key = "'GatewayRoute:'+#p0"),
                    @CacheEvict(key = "'GatewayRoute:'+'getAvailableGatewayRouteDefinitionList'"),
                    @CacheEvict(key = "'GatewayRoute:'+'getAvailableRouteDefinitionList'")})
    void enabledRouteDefinition(@Param("id") String id, @Param("enabled") boolean enabled);

    @Cacheable(key = "'GatewayRoute:'+#p0")
    @Select("select * from gateway_route_definition where id = #{id}")
    GatewayRouteDefinition getRouteDefinitionById(@Param("id") String id);

    @Select("select id,name from gateway_route_definition where enabled = 1 order by create_time desc ")
    @Cacheable(key = "'GatewayRoute:'+'getAvailableRouteDefinitionList'")
    List<AvailableRouteDefinitionListVo> getAvailableRouteDefinitionList();


}
