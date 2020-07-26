package order.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderRequest implements Serializable {

    private Integer userId;
    private Integer skuId;
    private String orderNo;

}
