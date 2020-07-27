package microservice.integration.app.controller;

import microservice.integration.common.bean.ResponseResult;
import order.dto.OrderRequest;
import order.service.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * dubbo 消费者使用示例
 * @description:
 * @author: haochencheng
 * @create: 2020-07-26 17:57
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

//    @DubboReference
    private OrderService orderService;

    @GetMapping("/create")
    public ResponseResult makeOrder(){
        return orderService.orderWithVersion(new OrderRequest());
    }

}
