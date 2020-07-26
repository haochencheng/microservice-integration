package microservice.integration.app.controller;

import microservice.integration.common.bean.ResponseResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class OrderControllerTest {

    public static final String APP_URL = "http://localhost:4002/app";
    private RestTemplate restTemplate=new RestTemplate();

    @Test
    void makeOrder() {
        ResponseEntity<ResponseResult> responseEntity = restTemplate.getForEntity(APP_URL + "/order/create", ResponseResult.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseResult responseResult = responseEntity.getBody();
        Assertions.assertTrue(responseResult.isSuccessful());
    }
}