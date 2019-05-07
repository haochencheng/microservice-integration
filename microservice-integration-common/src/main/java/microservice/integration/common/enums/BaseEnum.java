package microservice.integration.common.enums;

/**
 * 枚举基类
 * @description:
 * @author: haochencheng
 * @create: 2018-12-12 15:08
 **/
@FunctionalInterface
public interface BaseEnum<T> {

    T getValue();

}
