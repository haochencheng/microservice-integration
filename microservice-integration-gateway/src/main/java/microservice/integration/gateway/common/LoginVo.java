package microservice.integration.gateway.common;

import lombok.Data;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-07 18:01
 **/
@Data
public class LoginVo {

    private String username;
    private String password;
    private String token;

}
