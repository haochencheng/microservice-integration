package microservice.integration.common.dto.user;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 15:19
 **/
@Data
public class UserDto implements Serializable {

    private Integer userId;
    private String userName;
    private String password;
    private String mobile;
    private String token;
    private List<String> roles= Lists.newArrayList("admin");



}
