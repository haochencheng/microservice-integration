package microservice.integration.order.service.impl;

import microservice.integration.common.bean.ResponseResult;
import order.dto.OrderRequest;
import order.service.OrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: haochencheng
 * @create: 2020-07-26 14:03
 **/
@DubboService(interfaceClass = OrderService.class)
@Component
public class OrderServiceImpl implements OrderService {

    @Override
    public ResponseResult orderWithVersion(OrderRequest orderRequest) {
        System.out.println(orderRequest.toString());
        return ResponseResult.success();
    }
}
