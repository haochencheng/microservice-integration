package microservice.integration.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import microservice.integration.common.constant.EnvConstant;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-12-03 11:18
 **/
@Configuration
public class FeignConfiguration {

    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * 指定feign的日志级别
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel(){
        if (EnvConstant.PROD.equalsIgnoreCase(profile)){
            return Logger.Level.NONE;
        }
        return Logger.Level.FULL;
    }

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * new一个form编码器，实现支持form表单提交
     */
    @Bean
    public Encoder springFeignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Decoder feignDecoder() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        //Customize as much as you want
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

}
