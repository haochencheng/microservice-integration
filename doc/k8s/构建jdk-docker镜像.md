###	下载jdk

```url
https://repo.huaweicloud.com/java/jdk/8u151-b12/
```

###	写dockerfile

```dockerfile
FROM centos:7
MAINTAINER haochencheng

ENV JAVA_HOME /usr/local/jdk1.8.0_151 
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
ENV PATH $PATH:$JAVA_HOME/bin
# 设置字符集，防止乱码
ENV LANG en_US.utf8 
ENV LC_ALL en_US.utf8 

# 本地下载jdk1.8 或者网络下载
# 本地使用COPY 命令 （注意后面是当前路径的相对路径）
# COPY dk1.8.0_151 /usr/local/
# 网络下载 使用ADD 解压缩
RUN curl -O https://repo.huaweicloud.com/java/jdk/8u151-b12/jdk-8u151-linux-x64.tar.gz && tar -xzf jdk-8u151-linux-x64.tar.gz -C /usr/local/ && rm jdk-8u151-linux-x64.tar.gz  \
# 设置时区
&& rm -rf /etc/localtime && ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
# 安装telnet/ifconfig（用于调测网络使用）
&& yum install telnet net-tools -y

```

FROM 指定基础镜像

MAINTAINER 指定维护者信息

RUN 在命令前面加上RUN（通常用于执行 linux 命令）

ADD 从宿主机上复制文件到镜像中

ENV 设置环境变量

WORKDIR 设置当前工作目录，类似于cd

VOLUME 设置卷，挂载主机目录

EXPOSE 指定对外的端口

CMD 指定容器启动后要干的事情



###	构建镜像

```sh
docker build -t haochencheng/centos7-java8:1.0 https://raw.githubusercontent.com/haochencheng/microservice-integration/master/doc/k8s/common-image-DockerFile
```



###	启动镜像

```sh
docker run -idt  --name java8 haochencheng/centos7-java8:1.0 /bin/bash
```

###	进入控制台

```sh
docker exec -it java8 /bin/bash
```

###	查看java环境

```sh
java
```



###	上传镜像到阿里云私服

1. 登录阿里云Docker Registry

```sh

$ sudo docker login --username=tb8316616_2011 registry.cn-hangzhou.aliyuncs.com
用于登录的用户名为阿里云账号全名，密码为开通服务时设置的密码。
您可以在访问凭证页面修改凭证密码。
```

2. 从Registry中拉取镜像

```sh
$ sudo docker pull registry.cn-hangzhou.aliyuncs.com/haochencheng/k8s:[镜像版本号]
```

3. 将镜像推送到Registry

```
$ sudo docker login --username=tb8316616_2011 registry.cn-hangzhou.aliyuncs.com
$ sudo docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/haochencheng/k8s:[镜像版本号]
$ sudo docker push registry.cn-hangzhou.aliyuncs.com/haochencheng/k8s:[镜像版本号]
```

