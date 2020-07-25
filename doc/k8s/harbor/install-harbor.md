### helm安装harbor
添加harbor的chart仓库
```bash
helm repo add harbor https://helm.goharbor.io
helm repo update
```

获取harbor的chart压缩包
```bash
helm fetch harbor/harbor
tar -zxvf harbor-1.1.1.tgz
cd harbor
```

使用![harbor-values.yaml](harbor-values.yaml)进行安装
在解压出来的目录里面执行，注意这里的.表示当前所在目录。
```bash
helm install . --name harbor --namespace harbor -f ../harbor-values.yaml
```

查看ingress资源对象
```bash
kubectl get ing -o wide -n harbor
```
安装完成后在 host中添加 映射
```
127.0.0.1 harbor.k8sinternal.com
```

浏览器中输入 harbor.k8sinternal.com 查看

然后输入用户名,密码：
```
admin
Harbor12345
```

没有使用证书 报401
```
Error response from daemon: Get https://harbor.k8sinternal.com/v2/: unauthorized: authentication required
```

修改nginx配置关闭ssl转发
```
    "ingress.kubernetes.io/ssl-redirect": "false",
      "nginx.ingress.kubernetes.io/ssl-redirect": "false"
```



也可以通过 Helm 安装的时候自己覆盖 harborAdminPassword  
即可登录进入 Portal 首页

docker cli测试login,pull,push
测试使用 docker cli 来进行 pull/push 镜像

```bash
 docker login harbor.k8sinternal.com
```

