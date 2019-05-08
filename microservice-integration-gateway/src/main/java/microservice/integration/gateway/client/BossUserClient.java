package microservice.integration.gateway.client;


import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.config.FeignConfiguration;
import microservice.integration.gateway.common.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "BossUserClient",fallback = BossUserClient.BossUserClientFallBack.class,configuration = FeignConfiguration.class)
public interface BossUserClient {

    @PostMapping(value = "/user/threadlogin")
    ResponseResult<UserResponse> userLogin(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("token") String token);

    class BossUserClientFallBack implements BossUserClient {

        @Override
        public ResponseResult<UserResponse> userLogin(String username, String password, String token) {
            return ResponseResult.error("BossUserClientFallBack：调用boss出错");
        }
    }

}
