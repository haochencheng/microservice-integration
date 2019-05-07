package microservice.integration.common.bean;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ResponseResult<T> implements Serializable {

    private final static String SUCCESS="success";
    private final static Integer SUCCESS_CODE=0;
    private final static Integer NEED_LOGIN_CODE=-2;


    /**
     * 默认错误代码
     */
    private final static Integer DEFAULT =-1;

    /**
     * 业务错误代码
     */
    private final static Integer BUSINESS=-20;

    /**
     * 参数不正确错误代码
     */
    private final static Integer INVALID_PARAM=-30;

    /**
     * 网关错误错误代码
     */
    private final static Integer GATEWAY_ERROR=-200;

    /**
     *  0成功
     */
    @ApiModelProperty(value = "错误码", name = "错误码")
    private Integer retCode = SUCCESS_CODE;
    /**
     * 返回消息，成功为“success”，失败为具体失败信息
     */
    @ApiModelProperty(value = "错误码描述", name = "错误码描述")
    private String retMsg;
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "数据对象", name = "数据对象")
    private T data;

    private ResponseResult() {

    }

    private ResponseResult(T data) {
        this.setData(data);
    }

    private ResponseResult(Integer errorCode, String message) {
        this.setRetCode(errorCode);
        this.setRetMsg(message);
    }

    private ResponseResult(String message) {
        this.setRetCode(DEFAULT);
        this.setRetMsg(message);
    }


    private ResponseResult(String message, T data) {
        this.setRetCode(SUCCESS_CODE);
        this.setRetMsg(message);
        this.setData(data);
    }

    public boolean isSuccessful(){
        return retCode.equals(SUCCESS_CODE);
    }

    public boolean isError(){
        return !retCode.equals(SUCCESS_CODE);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(SUCCESS,data);
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult(SUCCESS, StringUtils.EMPTY);
    }

    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(DEFAULT,message);
    }

    public static <T> ResponseResult<T> error(Integer errorCode, String message) {
        return new ResponseResult<>(errorCode, message);
    }

    public static <T> ResponseResult<T> needLogin() {
        return new ResponseResult<>(NEED_LOGIN_CODE, "用户未登录");
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
