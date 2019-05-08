package microservice.integration.auth.controller;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import microservice.integration.common.bean.ResponseResult;
import microservice.integration.common.dto.user.UserDto;
import microservice.integration.common.util.AESUtil;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-05-08 11:52
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private static Cache<String,UserDto> userCache;

    static {
        CacheBuilder cacheBuilder=CacheBuilder.newBuilder();
        userCache = cacheBuilder.expireAfterAccess(1, TimeUnit.HOURS).initialCapacity(10).build();
    }

    /**
     * 用户登录
     * @param userName
     * @param password
     * @param token google验证码
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseResult<UserDto> userLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("token") String token){
        if (Strings.isNullOrEmpty(userName)){
            return ResponseResult.error("用户名不可为空");
        }
        if (Strings.isNullOrEmpty(password)){
            return ResponseResult.error("密码不可为空");
        }
        if (Strings.isNullOrEmpty(token)){
            return ResponseResult.error("token不可为空");
        }
        String md5WithSalt;
        try {
            md5WithSalt = AESUtil.decrypt(AESUtil.ASE_KEY,password);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        UserDto userDto=new UserDto();
        userDto.setMobile("136.........xxx");
        token = UUID.randomUUID().toString();
        userDto.setToken(token);
        userDto.setUserName("admin");
        userDto.setUserId(1);
        userCache.put(token,userDto);
        return ResponseResult.success(userDto);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public ResponseResult<UserDto> userInfo(@RequestHeader("token") String token){
        return ResponseResult.success(userCache.getIfPresent(token));
    }


}
