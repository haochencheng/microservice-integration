package microservice.integration.gateway.report;

import com.google.common.base.Strings;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.dto.gateway.ApplicationInfo;
import microservice.integration.common.dto.gateway.ServiceInfo;
import microservice.integration.common.enums.auth.AuthorizationEnum;
import microservice.integration.common.util.JwtUtil;
import microservice.integration.gateway.report.annotation.GatewayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @description: 服务配置上报网关
 * @author: haochencheng
 * @create: 2019-04-30 15:20
 **/
public class GatewayServiceReportHandler implements ApplicationContextAware {

    private static final Logger logger= LoggerFactory.getLogger(GatewayServiceReportHandler.class);

    @Value("${spring.application.name}")
    private String applicationName;

    public GatewayServiceReportHandler() {
        logger.info("enable GatewayServiceReportHandler!");
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate=new RestTemplate();

    private final static String GATEWAY_APPLICATION_NAME="microservice-integration-gateway";

    private String getReportGatewayUrl() {
        if (Objects.isNull(discoveryClient)){
            logger.error("no discoveryClient founded !could not report services to gateway");
            return null;
        }
        List<String> services = discoveryClient.getServices();
        if (services.isEmpty()){
            logger.error("no available gateway service !could not report services to gateway");
            return null;
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(GATEWAY_APPLICATION_NAME);
        if (instances.isEmpty()){
            logger.error("no available gateway instance !could not report services to gateway");
            return null;
        }
        ServiceInstance serviceInstance = instances.get(0);
        String reportUrl;
        if (serviceInstance.isSecure()){
            reportUrl="https://";
        }else {
            reportUrl="http://";
        }
        return reportUrl+serviceInstance.getHost()+":"+serviceInstance.getPort();
    }

    private List<ServiceInfo> getServiceInfoList(ApplicationContext applicationContext) {
        //请求url和处理方法的映射
        List<ServiceInfo> requestMappingInfoList = new LinkedList<>();
        //获取所有的RequestMapping
        Map<String, RequestMappingHandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
                RequestMappingHandlerMapping.class, true, false);
        for (HandlerMapping handlerMapping : allRequestMappings.values()) {
            RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
                RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
                HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

                RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
                String requestType;
                Set<RequestMethod> methods = methodCondition.getMethods();
                requestType = methods.isEmpty() ? "" : methods.stream().findFirst().get().name();
                PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                String requestUrl = patternsCondition.getPatterns().stream().findFirst().get();

                String controllerName = mappingInfoValue.getBeanType().toString();
                String requestMethodName = mappingInfoValue.getMethod().getName();
                Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();
                //反射获取 权限注解 默认不需要鉴权
                GatewayInfo methodAnnotation = mappingInfoValue.getMethodAnnotation(GatewayInfo.class);
                AuthorizationEnum authorization;
                String name = null;
                if (Objects.nonNull(methodAnnotation)){
                    authorization = methodAnnotation.authorization();
                    name = methodAnnotation.name();
                }else {
                    authorization = AuthorizationEnum.NO;
                }
                ServiceInfo serviceInfo = ServiceInfo.builder().methodName(Strings.isNullOrEmpty(name) ? requestMethodName : name)
                        .requestType(requestType).requestUrl(requestUrl)
                        .controllerName(controllerName).authorization(authorization).build();
                requestMappingInfoList.add(serviceInfo);
            }
        }
        return requestMappingInfoList;
    }

    /**
     * 上报 网关
     * @param applicationInfo
     * @return
     */
    private ResponseResult report(String reportGatewayUrl, ApplicationInfo applicationInfo) {
        if (!reportGatewayUrl.endsWith("/")) {
            reportGatewayUrl = reportGatewayUrl+"/";
        }
        reportGatewayUrl+="register/service";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("token", JwtUtil.getServerToken());
        ResponseEntity<Void> response = restTemplate.exchange(reportGatewayUrl, HttpMethod.POST,
                new HttpEntity<>(applicationInfo, headers), Void.class);
        logger.info("response:{}",response.toString());
        return ResponseResult.success();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String reportGatewayUrl=getReportGatewayUrl();
        if (Objects.isNull(reportGatewayUrl)){
            logger.error("get report GatewayUrl error ! could not report services to gateway");
            return;
        }
        List<ServiceInfo> serviceInfoList = getServiceInfoList(applicationContext);
        if (CollectionUtils.isEmpty(serviceInfoList)){
            logger.info("no services found to report !");
            return;
        }
        Environment environment = applicationContext.getEnvironment();
        String path = environment.getProperty("server.servlet.path");
        logger.debug("report application path is : {}",path);
        ApplicationInfo applicationInfo=new ApplicationInfo(this.applicationName,path,serviceInfoList);
        report(reportGatewayUrl,applicationInfo);
    }

}
