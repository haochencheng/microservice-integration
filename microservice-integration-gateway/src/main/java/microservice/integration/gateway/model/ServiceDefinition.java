package microservice.integration.gateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-01 20:16
 **/
@Data
public class ServiceDefinition {

    private Long id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 所属应用
     */
    private Integer applicationDefinitionId;

    /**
     * 服务请求路径
     */
    private String servicePath;
    /**
     * 服务请求方法
     */
    private String serviceMethod;
    /**
     * 是否需要授权
     */
    private int needAuthorization=0;
    /**
     * 是否启用
     */
    private boolean enabled=true;
    /**
     * 描述
     */
    private String desc;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime=LocalDateTime.now();


}
