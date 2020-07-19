### 版本Jenkins 2.245

根据现有镜像构建，更换jenkins源，下载了一些插件。下载即可用
用户名admin admin
源镜像参考![jenkins-deployment](../jenkins/jenkins-deployment.yaml)

docker commit 9940729d1de2 jenkins2.245

docker run -it jenkins2.245 /bin/bash
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

