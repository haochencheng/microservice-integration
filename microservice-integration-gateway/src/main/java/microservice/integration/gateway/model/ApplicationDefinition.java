package microservice.integration.gateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @description: 应用系统定义
 * @author: haochencheng
 * @create: 2018-11-01 20:34
 **/
@Data
public class ApplicationDefinition {

    private Integer id;
    /**
     * 应用名称
     */
    @NotNull(message = "应用名称不可为空")
    private String applicationName;
    /**
     * 应用path
     */
    @NotNull(message = "应用路径不可为空")
    private String applicationPath;

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
