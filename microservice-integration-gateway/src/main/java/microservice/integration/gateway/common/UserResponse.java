package microservice.integration.gateway.common;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-08 10:15
 **/
@Data
public class UserResponse {

    private String token;
    private String userId;
    private String name;
    private List<String> roles= Arrays.asList("admin");

}
