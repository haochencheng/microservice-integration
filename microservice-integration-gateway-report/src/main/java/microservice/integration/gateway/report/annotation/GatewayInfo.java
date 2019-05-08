package microservice.integration.gateway.report.annotation;

import microservice.integration.common.enums.auth.AuthorizationEnum;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface GatewayInfo {

    String name() default "";

    AuthorizationEnum authorization() default AuthorizationEnum.NO;

}
