package microservice.integration.gateway.controller;

import com.google.common.base.Strings;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.util.AESUtil;
import microservice.integration.gateway.client.BossUserClient;
import microservice.integration.gateway.common.LoginVo;
import microservice.integration.gateway.common.UserResponse;
import microservice.integration.gateway.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
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
    private RedisService<UserResponse> redisService;

    @Resource
    private BossUserClient bossUserClient;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody LoginVo loginVo){
        if (Objects.isNull(loginVo)){
            return ResponseResult.error("登录信息错误");
        }
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        String token = loginVo.getToken();
        if (Strings.isNullOrEmpty(username)){
            return ResponseResult.error("用户名不可为空");
        }
        if (Strings.isNullOrEmpty(password)){
            return ResponseResult.error("密码不可为空");
        }
        if (Strings.isNullOrEmpty(token)){
            return ResponseResult.error("token不可为空");
        }
        //调用boss
        String md5WithSalt = AESUtil.decrypt(AESUtil.ASE_KEY,password);
        ResponseResult<UserResponse> userLoginResponse = bossUserClient.userLogin(username, md5WithSalt, token);
        if (!userLoginResponse.isSuccessful()){
            return ResponseResult.error(userLoginResponse.getRetCode(),userLoginResponse.getRetMsg());
        }
        UserResponse userResponse=new UserResponse();
        UserResponse data = userLoginResponse.getData();
        userResponse.setUserId(data.getUserId());
        userResponse.setName(data.getName());
        String sessionId = UUID.randomUUID().toString();
        userResponse.setToken(sessionId);
        redisService.setEx(sessionId,userResponse, TimeUnit.HOURS.toSeconds(1));
        return ResponseResult.success(userResponse);
    }

    @GetMapping("/user/info")
    public ResponseResult userInfo(@RequestHeader(value = "Gateway-Token") String token){
        //通过token获取用户信息
        if (Strings.isNullOrEmpty(token)){
            return ResponseResult.error(-100,"用户信息校验失败，请重新登录");
        }
        UserResponse userResponse = redisService.get(token,UserResponse.class);
        if (Objects.isNull(userResponse)){
            return ResponseResult.error(-100,"登录信息已过期，请重新登录");
        }
        userResponse.setToken("");
        return ResponseResult.success(userResponse);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(@RequestHeader(value = "Gateway-Token") String token){
        redisService.delete(token);
        return ResponseResult.success("登出成功");
    }

}
