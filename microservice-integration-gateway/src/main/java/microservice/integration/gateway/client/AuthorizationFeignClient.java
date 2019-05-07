package microservice.integration.gateway.client;

import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.config.FeignConfiguration;
import microservice.integration.common.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "ucommune-auth-service",path = "/auth",fallback = AuthorizationFeignClient.AuthorizationFeignClientFallBack.class,configuration = FeignConfiguration.class)
public interface AuthorizationFeignClient {

    /**
     * 获取用户信息
     * @param cookie
     * @return
     */
    @RequestMapping(value = "/user/app/info",method = RequestMethod.GET)
    ResponseResult<UserDto> isUserLogin(@RequestHeader("cookie") String cookie);

    /**
     * 获取boss权限
     * @param token
     * @return
     */
    @GetMapping("/user/boss/authorization")
    ResponseResult authorizationBoss(@RequestHeader("token") String token);

    class AuthorizationFeignClientFallBack implements AuthorizationFeignClient {

        @Override
        public ResponseResult<UserDto> isUserLogin(String cookie) {
            return ResponseResult.error("AuthorizationFeignClientFallBack-isUserLogin：调用auth出错");
        }

        @Override
        public ResponseResult authorizationBoss(String token) {
            return ResponseResult.error("AuthorizationFeignClientFallBack-authorizationBoss：调用auth出错");
        }
    }

}


