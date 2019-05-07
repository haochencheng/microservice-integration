package microservice.integration.gateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 17:02
 **/
public class GatewayRouteDefinition extends RouteDefinition{

    private String name;
    /**
     * 描述
     */
    private String desc;

    private String routeDefinitionUri;

    private boolean enabled;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime=LocalDateTime.now();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRouteDefinitionUri() {
        return routeDefinitionUri;
    }

    public void setRouteDefinitionUri(String routeDefinitionUri) {
        this.routeDefinitionUri = routeDefinitionUri;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
