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
helm install . --name harbor --namespace kube-ops -f ../harbor-values.yaml
```

查看ingress资源对象
```bash
kubectl get ing -o wide -n kube-ops
```

浏览器中输入harbor.k8sinternal.com查看

然后输入用户名：admin，密码：Harbor12345
（也可以通过 Helm 安装的时候自己覆盖 harborAdminPassword）
即可登录进入 Portal 首页 

查看是否安装成功
```bash
 kubectl get pod -n kube-ops
```
查看release：
```bash
helm list
```
打包chart：
```bash
helm package hello-helm
```
然后我们就可以将打包的tgz文件分发到任意的服务器上，通过helm fetch就可以获取到该 Chart 了

删除release：
```bash
 helm delete winning-zebra
```




docker cli测试login,pull,push




    