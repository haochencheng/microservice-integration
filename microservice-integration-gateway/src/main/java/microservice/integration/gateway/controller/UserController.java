package microservice.integration.gateway.controller;

import com.google.common.base.Strings;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.dto.user.UserDto;
import microservice.integration.gateway.client.AuthorizationFeignClient;
import microservice.integration.gateway.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-02 10:52
 **/
@RestController
@RequestMapping("admin")
public class UserController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RedisService<UserDto> redisService;

    @Resource
    private AuthorizationFeignClient authorizationFeignClient;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserDto userDto){
        if (Objects.isNull(userDto)){
            return ResponseResult.error("登录信息错误");
        }
        String username = userDto.getUserName();
        String password = userDto.getPassword();
        String token = userDto.getToken();
        if (Strings.isNullOrEmpty(username)){
            return ResponseResult.error("用户名不可为空");
        }
        if (Strings.isNullOrEmpty(password)){
            return ResponseResult.error("密码不可为空");
        }
        if (Strings.isNullOrEmpty(token)){
            return ResponseResult.error("token不可为空");
        }
        //调用auth
        ResponseResult<UserDto> userLoginResponse = authorizationFeignClient.userLogin(username, password, token);
        if (!userLoginResponse.isSuccessful()){
            return ResponseResult.error(userLoginResponse.getRetCode(),userLoginResponse.getRetMsg());
        }
        UserDto data = userLoginResponse.getData();
        redisService.setEx(data.getToken(),data, TimeUnit.HOURS.toSeconds(1));
        return ResponseResult.success(data);
    }

    @GetMapping("/user/info")
    public ResponseResult userInfo(@RequestHeader(value = "token") String token){
        //通过token获取用户信息
        if (Strings.isNullOrEmpty(token)){
            return ResponseResult.error(-100,"用户信息校验失败，请重新登录");
        }
        UserDto userResponse = redisService.get(token,UserDto.class);
        if (Objects.isNull(userResponse)){
            return ResponseResult.error(-100,"登录信息已过期，请重新登录");
        }
        userResponse.setToken("");
        return ResponseResult.success(userResponse);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(@RequestHeader(value = "token") String token){
        redisService.delete(token);
        return ResponseResult.success("登出成功");
    }

}
