package order.service;

import microservice.integration.common.bean.ResponseResult;
import order.dto.OrderRequest;

public interface OrderService {

    ResponseResult orderWithVersion(OrderRequest orderRequest);

}
