package microservice.integration.common.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 环境判断 工具类 读取 spring.profiles.active  使用需在启动 扫描到 此类  eg 。 @ComponentScan("com.ucommune")
 * @description:
 * @author: haochencheng
 * @create: 2019-04-16 13:28
 **/
public class ProfileUtil implements EnvironmentAware {

    private final static String PROFILE_KEY="activeProfiles";
    public static final String DEV = "dev";
    public static final String PROD = "prod";

    private static Environment environment= null;

    public static boolean isProd(){
        String profile = environment.getProperty(PROFILE_KEY, DEV);
        return PROD.equals(profile);
    }

    public static boolean nonProd(){
        String profile = environment.getProperty(PROFILE_KEY, DEV);
        return !PROD.equals(profile);
    }

    @Override
    public void setEnvironment(Environment environment) {
        ProfileUtil.environment=environment;
    }
}
