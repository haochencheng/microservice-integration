package microservice.integration.common.enums.auth;

import java.util.Arrays;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-12-04 18:32
 **/
public enum AuthorizationEnum {

    NO(0,"不鉴权"),
    APP(1,"APP登录"),
    SERVER(2,"服务端鉴权"),
    VISITORS(3,"访客模式");

    private int code;
    private String desc;

    AuthorizationEnum(int code, String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static AuthorizationEnum getEnumByCode(int code){
        return Arrays.stream(AuthorizationEnum.values()).filter(authorizationEnum -> authorizationEnum.getCode()==code).findAny().orElse(null);
    }
}

