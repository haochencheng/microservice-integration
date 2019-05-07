package microservice.integration.common.bean;

import lombok.Data;

/**
 * @description: 分页查询条件
 * @author: haochencheng
 * @create: 2018-12-07 16:11
 **/
@Data
public class PageCondition {

    private int pageNum=1;
    private int pageSize=10;

}
