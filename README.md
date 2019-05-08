# Getting Started
## 快速开始

![image-20190508155005132](/Users/haochencheng/Library/Application Support/typora-user-images/image-20190508155005132.png)

创建gateway数据库，执行doc/gateway.sql 。

执行doc/nacos.sql 插入nacos配置数据(如不想使用nacos配置中心，则使用/microservice-integration-gateway/resource中 dev1的配置 即可)

##### 启动 microservice-integration-eureka 

访问地址：

```
localhost:3000
```

#### 启动 microservice-integration-gateway

网关地址:

```
localhost:4000
```

#### 启动 microservice-integration-auth

#### 启动 microservice-integration-app

通过网关访问：

```
localhost:4000/app/no
```

二、网关后台 microservice-integration-admin

```
进入microservice-integration-admin目录
1.安装node yarn npm
2.执行 yarn 安装依赖
3.执行 npm run dev 启动测试环境
```

验证码使用 谷歌 recaptcha-v3  的vue插件

```
vue-recaptcha-v3 
```

访问地址：

```
localhost:9528
```

登录用户名密码随意填写



## 技术栈

#### nacos

```
https://nacos.io/zh-cn/docs/what-is-nacos.html
```

#### 网关后台 vue-admin-template

```
https://github.com/PanJiaChen/vue-admin-template/blob/master/README-zh.md
```

### 说明

#### microservice-integration-eureka

eureka注册中心

#### microservice-integration-app

测试应用app

#### Microservice-integration-auth

应用鉴权

#### microservice-integration-gateway

springcloud-gateway 网关 

#### Microservice-integration-gateway-admin

网关后台管理

- 服务通过网关访问，首先走GatewayServiceManagerAuthorizationFilter鉴权过滤器，先查询服务是否注册，未注册返回未授权应用。

- 根据不同鉴权配置，访问auth应用

- 路由到配置的应用系统

  ```
  
  ```

  

1.服务管理

例如app应用，有多个服务 。

```
microservice-integration-app=microservice-integration-app

有三个服务
@GetMapping("/info")
    @GatewayInfo(name="获取app信息",authorization = AuthorizationEnum.SERVER)
    public ResponseResult getAppInfo(){
        return ResponseResult.success("appInfo");
    }

    @PostMapping("/list")
    @GatewayInfo(name="获取app list",authorization = AuthorizationEnum.LOGIN)
    public ResponseResult getList(){
        return ResponseResult.success(Lists.newArrayList(1,2,3));
    }

    @GetMapping("/no")
    @GatewayInfo(name="不需要鉴权",authorization = AuthorizationEnum.NO)
    public ResponseResult no(){
        return ResponseResult.success(Lists.newArrayList(4,5,6));
    }

```

服务配置注解，依赖microservice-integration-gateway-report 服务启动会上报服务到网关，没有声明鉴权注解默认不鉴权，直接可以访问

```
@GatewayInfo(name="不需要鉴权",authorization = AuthorizationEnum.NO)

鉴权方式枚举
public enum AuthorizationEnum {

    NO(0,"不鉴权"),
    LOGIN(1,"用户登录"),
    SERVER(2,"服务端鉴权");
}

```



2.路由 
	2.1 配置路由管理

​	例如服务app应用名称为        

```
microservice-integration-app=microservice-integration-app
```

​	如图：

![image-20190508150207832](/Users/haochencheng/Library/Application Support/typora-user-images/image-20190508150207832.png)

如使用注册中心，则目标uri为lb://${应用名称}

​	2.2 配置断言列表

![image-20190508150331356](/Users/haochencheng/Library/Application Support/typora-user-images/image-20190508150331356.png)

配置拦截路径 选择所属路由，如图 /app/**    则拦截 /app/** 所有请求  —>  所属路由的 目标uri

如上如配置完成后即可通过网关访问app 应用服务

如网关地址 http://127.0.0.1:4000/

app应用访问地址为 http://127.0.0.1:4000/app/**

如图：

![image-20190508150659212](/Users/haochencheng/Library/Application Support/typora-user-images/image-20190508150659212.png)

2.自定义服务管理

### microservice-integration-gateway-report

服务上报 ，依赖microservice-integration-gateway-report后 

通过 @GatewayInfo(name="获取app信息",authorization = AuthorizationEnum.SERVER)

启动时向gateway 上报 应用服务信息。

