package microservice.integration.common.dto.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-06 17:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationInfo {

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 应用path
     */
    private String applicationPath;


    /**
     * 服务信息
     */
    private List<ServiceInfo> serviceInfoList;


}
