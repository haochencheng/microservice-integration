package microservice.integration.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-08 13:20
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class);
    }

}
