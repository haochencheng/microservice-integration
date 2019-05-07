package microservice.integration.common.dto.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.integration.common.enums.auth.AuthorizationEnum;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-06 17:54
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceInfo {

    public String controllerName;
    /**
     * 服务名称
     */
    public String methodName;
    public String requestType;
    /**
     * 请求地址
     */
    public String requestUrl;

    /**
     * 授权类型
     */
    private AuthorizationEnum authorization;

}
