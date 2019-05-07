package microservice.integration.gateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 17:09
 **/
public class GatewayPredicateDefinition extends PredicateDefinition{

    private int id;
    private String routeDefinitionId;
    private String predicateDefinitionArgs;
    private String desc;
    private boolean enabled;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime=LocalDateTime.now();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRouteDefinitionId() {
        return routeDefinitionId;
    }

    public void setRouteDefinitionId(String routeDefinitionId) {
        this.routeDefinitionId = routeDefinitionId;
    }

    public String getPredicateDefinitionArgs() {
        return predicateDefinitionArgs;
    }

    public void setPredicateDefinitionArgs(String predicateDefinitionArgs) {
        this.predicateDefinitionArgs = predicateDefinitionArgs;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
