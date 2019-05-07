package microservice.integration.common.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 15:19
 **/
@Data
public class UserDto implements Serializable {

    /**
     * realName :
     * mobile :
     * userId :
     */

    private String realName;
    private String mobile;
    private int userId;


}
