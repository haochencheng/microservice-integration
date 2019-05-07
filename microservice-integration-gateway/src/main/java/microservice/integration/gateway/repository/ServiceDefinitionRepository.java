package microservice.integration.gateway.repository;

import microservice.integration.gateway.common.ServiceDefinitionListCondition;
import microservice.integration.gateway.common.ServiceDefinitionListVo;
import microservice.integration.gateway.config.RedisConfig;
import microservice.integration.gateway.model.ServiceDefinition;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 20:54
 **/
@Repository
@CacheConfig(cacheNames = RedisConfig.GATEWAY_SERVICE)
public interface ServiceDefinitionRepository {

    @Insert(" INSERT INTO `service_definition` (`service_name`,`service_path`, `service_method`,`need_authorization`,`application_definition_id`, `desc`, `create_time`)" +
            "VALUES (#{serviceDefinition.serviceName},#{serviceDefinition.servicePath},#{serviceDefinition.serviceMethod},#{serviceDefinition.needAuthorization},#{serviceDefinition.applicationDefinitionId},#{serviceDefinition.desc},#{serviceDefinition.createTime});")
    int saveServiceDefinition(@Param("serviceDefinition") ServiceDefinition serviceDefinition);

    @CacheEvict( key = "'ServiceDefinition:'+#p0['id']")
    @Update("update `service_definition` set service_name=#{service.serviceName},service_path=#{service.servicePath},service_method=#{service.serviceMethod},need_authorization=#{service.needAuthorization},application_definition_id=#{service.applicationDefinitionId},`desc`=#{service.desc},enabled=#{service.enabled}  where id = #{service.id}")
    int updateServiceDefinition(@Param("service") ServiceDefinition serviceDefinition);

    @Update("update service_definition set enabled = #{enabled} where id = #{id}")
    @CacheEvict( key = "'ServiceDefinition:'+#p0")
    void enabledServiceDefinition(@Param("id") Integer id, @Param("enabled") boolean enabled);

    @Cacheable( key = "'ServiceDefinition:'+#p0")
    @Select("select * from service_definition where id = #{id}")
    ServiceDefinition getServiceDefinitionById(@Param("id") Integer id);

    @Cacheable(key = "'ServiceDefinition:'+#p0",unless = "#result == 0")
    @Select("select IFNULL((select id from service_definition where service_path =  #{servicePath}),0) as id;")
    int getServiceIdByServicePath(@Param("servicePath") String servicePath);

    @Select("select count(*) from service_definition where service_path =  #{servicePath}")
    boolean isServiceExist(@Param("servicePath") String servicePath);

    @Select(
            "<script>"+
                "select s.*,a.application_name  from service_definition s,application_definition a where s.application_definition_id = a.id " +
                "<if test=\"condition.serviceName != null and condition.serviceName != '' \"> " +
                "and s.service_name like  \"%\"#{condition.serviceName}\"%\"  " +
                "</if>"+
                "<if test=\"condition.servicePath != null and condition.servicePath != '' \"> " +
                "and s.service_path like \"%\"#{condition.servicePath}\"%\"  " +
                "</if>"+
                "<if test=\"condition.applicationDefinitionId != null and condition.applicationDefinitionId != 0 \"> " +
                "and s.application_definition_id = #{condition.applicationDefinitionId}" +
                "</if>"+
            "</script>"
            )
    List<ServiceDefinitionListVo> getAllServiceDefinitionList(@Param("condition") ServiceDefinitionListCondition serviceDefinitionListCondition);

    @Select("<script>"+
            "select * from service_definition where service_path in " +
                "<foreach item='item' index='index' collection='servicePathList' open='(' separator=',' close=')'>"+
                    "#{item}"+
                "</foreach>"+
            "</script>")
    List<ServiceDefinition> getServiceDefinitionListByServicePathList(@Param("servicePathList") Collection<String> servicePathList);

    @Insert("<script>"+
            "INSERT INTO `service_definition` (`service_name`,`service_path`, `service_method`,`need_authorization`,`application_definition_id`, `desc`, `create_time`)" +
            "VALUES" +
                "<foreach item='serviceDefinition' index='index' collection='list' separator=','  >"+
                    "( #{serviceDefinition.serviceName},#{serviceDefinition.servicePath},#{serviceDefinition.serviceMethod},#{serviceDefinition.needAuthorization},#{serviceDefinition.applicationDefinitionId},#{serviceDefinition.desc},#{serviceDefinition.createTime} )"+
                "</foreach>"+
            "</script>")
    int saveServiceDefinitionList(@Param("list") List<ServiceDefinition> serviceDefinition);

}
