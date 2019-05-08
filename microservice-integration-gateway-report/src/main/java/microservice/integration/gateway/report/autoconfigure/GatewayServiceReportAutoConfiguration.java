package microservice.integration.gateway.report.autoconfigure;

import microservice.integration.gateway.report.GatewayServiceReportHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-05 19:11
 **/
@Configuration
@ConditionalOnProperty(name = "spring.application.name")
@AutoConfigureAfter(DiscoveryClient.class)
public class GatewayServiceReportAutoConfiguration {

    @Bean
    public GatewayServiceReportHandler gatewayServiceReportHandler(){
        return new GatewayServiceReportHandler();
    }

}
