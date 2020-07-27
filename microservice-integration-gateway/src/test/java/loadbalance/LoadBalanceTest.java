package loadbalance;

import microservice.integration.common.bean.ResponseResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 负载均衡测试
 * @description:
 * @author: haochencheng
 * @create: 2020-07-26 23:35
 **/
public class LoadBalanceTest {

    public static final String APP_URL = "http://localhost:4000/app";
    private RestTemplate restTemplate=new RestTemplate();

    @Test
    void loadBalance() {
        ResponseEntity<ResponseResult> responseEntity;
        ResponseResult responseResult;
        for (int i = 0; i < 1; i++) {
            responseEntity = restTemplate.getForEntity(APP_URL + "/choose/port", ResponseResult.class);
            Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            responseResult = responseEntity.getBody();
            Assertions.assertTrue(responseResult.isSuccessful());
            System.out.println(responseResult.getData());
        }
    }



}
