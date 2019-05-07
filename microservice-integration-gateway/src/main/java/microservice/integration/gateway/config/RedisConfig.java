package microservice.integration.gateway.config;


import microservice.integration.common.config.BaseRedisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig extends BaseRedisConfig {

    public static final String GATEWAY_SERVICE = "microservice-integration-gateway-service";
    public static final String SHORT_DATA_CACHE = "gateway-service-shortDataCache";

    @Override
    public Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //BasicDataCache进行过期时间配置
        redisCacheConfigurationMap.put(GATEWAY_SERVICE, this.getRedisCacheConfigurationWithTtl(30*60));
        redisCacheConfigurationMap.put(SHORT_DATA_CACHE, this.getRedisCacheConfigurationWithTtl(5*60));
        return redisCacheConfigurationMap;
    }



}
