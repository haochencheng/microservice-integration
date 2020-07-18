###	mac安装

https://helm.sh/docs/intro/quickstart/

```sh
brew install kubernetes-helm
```

###	Kubernetes helm配置国内镜像源

删除默认的源

```
helm repo remove stable
```

添加新源

```sh
helm repo add stable https://burdenbear.github.io/kube-charts-mirror/
或者
helm repo add stable https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
```

查看helm源添加情况

```sh
helm repo list
```



### 添加helm service account 并添加到clusteradmin 这个clusterrole上

```shell
kubectl create serviceaccount --namespace=kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
```

### 安装helm

#### 使用阿里镜像安装tiller，同时将repo 改为微软提供的helm repo， 阿里也有一个repo但是停止更新了。

```shell
helm init --upgrade -i registry.cn-hangzhou.aliyuncs.com/google_containers/tiller:v2.14.3 --stable-repo-url http://mirror.azure.cn/kubernetes/charts/ --service-account=tiller
```