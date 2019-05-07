package microservice.integration.gateway.repository;

import microservice.integration.gateway.common.AvailableApplicationDefinitionListVo;
import microservice.integration.gateway.config.RedisConfig;
import microservice.integration.gateway.model.ApplicationDefinition;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 20:53
 **/
@Repository
@CacheConfig(cacheNames = RedisConfig.GATEWAY_SERVICE)
public interface ApplicationDefinitionRepository {

    @Insert(" INSERT INTO `application_definition` " +
            " (`application_name`, `application_path`,`enabled`, `desc`,`create_time`)" +
            "VALUES (#{app.applicationName}, #{app.applicationPath},#{app.enabled},#{app.desc},#{app.createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Caching(evict = {
                    @CacheEvict(key = "'ApplicationDefinition:'+'getAvailableApplicationDefinitionList'")})
    int saveApplicationDefinition(@Param("app") ApplicationDefinition applicationDefinition);

    @Caching(evict = {@CacheEvict(key = "'ApplicationDefinition:'+#p0['id']"),
                    @CacheEvict(key = "'ApplicationDefinition:'+'getAvailableApplicationDefinitionList'")})
    @Update("update `application_definition` set application_name=#{app.applicationName},application_path=#{app.applicationPath},`desc`=#{app.desc},enabled=#{app.enabled}  where id = #{app.id}")
    int updateApplicationDefinition(@Param("app") ApplicationDefinition applicationDefinition);

    @Update("update application_definition set enabled = #{enabled} where id = #{id}")
    @Caching(evict = {
                    @CacheEvict(key = "'ApplicationDefinition:'+#p0"),
                    @CacheEvict(key = "'ApplicationDefinition:'+'getAvailableApplicationDefinitionList'")})
    void enabledApplicationDefinition(@Param("id") Integer id, @Param("enabled") boolean enabled);

    @Cacheable( key = "'ApplicationDefinition:'+#p0")
    @Select("select * from application_definition where id = #{id}")
    ApplicationDefinition getApplicationById(@Param("id") Integer id);

    @Cacheable(key = "'ApplicationDefinition:'+#p0",unless = "#result == 0")
    @Select("select IFNULL((select id from application_definition where application_path = #{applicationPath}),0) as id;")
    int getAppIdByApplicationPath(@Param("applicationPath") String applicationPath);

    @Select("select count(*) from application_definition where application_path = #{applicationPath}")
    boolean isAppExist(@Param("applicationPath") String applicationPath);

    @Select("select * from application_definition order by create_time desc")
    List<ApplicationDefinition> getAllApplicationDefinitionList();

    @Select("select id,application_name from application_definition where enabled = 1 order by create_time desc ")
    @Cacheable(key = "'ApplicationDefinition:'+'getAvailableApplicationDefinitionList'")
    List<AvailableApplicationDefinitionListVo> getAvailableApplicationDefinitionList();

}
