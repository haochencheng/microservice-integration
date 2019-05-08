package microservice.integration.gateway.client;

import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.config.FeignConfiguration;
import microservice.integration.common.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "microservice-integration-auth",path = "/auth",fallback = AuthorizationFeignClient.AuthorizationFeignClientFallBack.class,configuration = FeignConfiguration.class)
public interface AuthorizationFeignClient {

    /**
     * 用户登录
     * @param userName
     * @param password
     * @param token
     * @return
     */
    @PostMapping(value = "/user/login")
    ResponseResult<UserDto> userLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("token") String token);

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/info",method = RequestMethod.GET)
    ResponseResult<UserDto> userInfo(@RequestHeader("token") String token);

    /**
     * 获取boss权限
     * @param token
     * @return
     */
    @GetMapping("/server/authorization")
    ResponseResult authorizationServer(@RequestHeader("token") String token);

    class AuthorizationFeignClientFallBack implements AuthorizationFeignClient {

        @Override
        public ResponseResult<UserDto> userInfo(String cookie) {
            return ResponseResult.error("AuthorizationFeignClientFallBack-userInfo：调用auth出错");
        }

        @Override
        public ResponseResult authorizationServer(String token) {
            return ResponseResult.error("AuthorizationFeignClientFallBack-authorizationServer：调用auth出错");
        }

        @Override
        public ResponseResult<UserDto> userLogin(String username, String password, String token) {
            return ResponseResult.error("BossUserClientFallBack：调用boss出错");
        }
    }

}


