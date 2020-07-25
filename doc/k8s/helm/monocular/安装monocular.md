### 安装chart 管理UI monocular


```bash
helm repo add monocular https://kubernetes-helm.github.io/monocular
```

通过中科大镜像加速 镜像加速
```bash
quay.mirrors.ustc.edu.cn
```

 
 ```bash
helm install monocular/monocular --namespace monocular
```

```bash
 kubectl --namespace monocular get ingress wanton-robin-monocular
```