###	helm安装

https://www.cnblogs.com/hongdada/p/11270704.html

为了便于将集群中的服务暴露到集群外部，需要使用Ingress。接下来使用Helm将Nginx Ingress部署到Kubernetes上。 Nginx Ingress Controller被部署在Kubernetes的边缘节点上。

这里将`master`作为边缘节点，打上`label`

```
Copy[root@master /]# kubectl label node master node-role.kubernetes.io/edge=
node/master labeled
[root@master /]# kubectl get nodes
NAME      STATUS   ROLES         AGE    VERSION
master    Ready    edge,master   4d3h   v1.15.1
slaver1   Ready    <none>        4d2h   v1.15.1
slaver2   Ready    <none>        4d2h   v1.15.1
```

## 安装[#](https://www.cnblogs.com/hongdada/p/11270704.html#3701619660)

### 使用`yaml`配置文件安装[#](https://www.cnblogs.com/hongdada/p/11270704.html#728995703)

stable/nginx-ingress chart的值文件ingress-nginx.yaml如下：

```
Copycontroller:
  replicaCount: 1
  hostNetwork: true
  nodeSelector:
    node-role.kubernetes.io/edge: ''
  affinity:
    podAntiAffinity:
        requiredDuringSchedulingIgnoredDuringExecution:
        - labelSelector:
            matchExpressions:
            - key: app
              operator: In
              values:
              - nginx-ingress
            - key: component
              operator: In
              values:
              - controller
          topologyKey: kubernetes.io/hostname
  tolerations:
      - key: node-role.kubernetes.io/master
        operator: Exists
        effect: NoSchedule
      - key: node-role.kubernetes.io/master
        operator: Exists
        effect: PreferNoSchedule
defaultBackend:
  nodeSelector:
    node-role.kubernetes.io/edge: ''
  tolerations:
      - key: node-role.kubernetes.io/master
        operator: Exists
        effect: NoSchedule
      - key: node-role.kubernetes.io/master
        operator: Exists
        effect: PreferNoSchedule
```

[完整版的nginx-ingress](https://github.com/helm/charts/blob/master/stable/nginx-ingress/values.yaml)

nginx ingress controller的副本数replicaCount为1，将被调度到node1这个边缘节点上。这里并没有指定nginx ingress controller service的externalIPs，而是通过`hostNetwork: true`设置nginx ingress controller使用宿主机网络。

```
Copyhelm repo update

helm install stable/nginx-ingress \
-n nginx-ingress \
--namespace kube-system  \
-f ingress-nginx.yaml
```

具体信息：

```
Copy[root@master /]# helm install stable/nginx-ingress -n nginx-ingress --namespace kube-system -f ingress-nginx.yaml
NAME:   nginx-ingress
LAST DEPLOYED: Tue Jul 30 15:29:31 2019
NAMESPACE: kube-system
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                      DATA  AGE
nginx-ingress-controller  1     <invalid>

==> v1/Pod(related)
NAME                                           READY  STATUS             RESTARTS  AGE
nginx-ingress-controller-657658b9b-xxq8r       0/1    ContainerCreating  0         <invalid>
nginx-ingress-default-backend-f8b68765c-fgjfs  0/1    ContainerCreating  0         <invalid>

==> v1/Service
NAME                           TYPE          CLUSTER-IP      EXTERNAL-IP  PORT(S)                     AGE
nginx-ingress-controller       LoadBalancer  10.108.248.196  <pending>    80:30839/TCP,443:32156/TCP  <invalid>
nginx-ingress-default-backend  ClusterIP     10.97.98.91     <none>       80/TCP                      <invalid>

==> v1beta1/Deployment
NAME                           READY  UP-TO-DATE  AVAILABLE  AGE
nginx-ingress-controller       0/1    1           0          <invalid>
nginx-ingress-default-backend  0/1    1           0          <invalid>

==> v1beta1/PodDisruptionBudget
NAME                           MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
nginx-ingress-controller       1              N/A              0                    <invalid>
nginx-ingress-default-backend  1              N/A              0                    <invalid>


NOTES:
The nginx-ingress controller has been installed.
It may take a few minutes for the LoadBalancer IP to be available.
You can watch the status by running 'kubectl --namespace kube-system get services -o wide -w nginx-ingress-controller'

An example Ingress that makes use of the controller:

  apiVersion: extensions/v1beta1
  kind: Ingress
  metadata:
    annotations:
      kubernetes.io/ingress.class: nginx
    name: example
    namespace: foo
  spec:
    rules:
      - host: www.example.com
        http:
          paths:
            - backend:
                serviceName: exampleService
                servicePort: 80
              path: /
    # This section is only required if TLS is to be enabled for the Ingress
    tls:
        - hosts:
            - www.example.com
          secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

  apiVersion: v1
  kind: Secret
  metadata:
    name: example-tls
    namespace: foo
  data:
    tls.crt: <base64 encoded cert>
    tls.key: <base64 encoded key>
  type: kubernetes.io/tls
```

### 直接使用命令安装[#](https://www.cnblogs.com/hongdada/p/11270704.html#2281461728)

```
Copyhelm install stable/nginx-ingress \
-n nginx-ingress \
--namespace kube-system  \
--set controller.hostNetwork=true，rbac.create=true \
--set controller.replicaCount=1
```

输出：

```
Copy[root@master /]# helm install stable/nginx-ingress \
> -n nginx-ingress \
> --namespace kube-system  \
> --set controller.hostNetwork=true，rbac.create=true \
> --set controller.replicaCount=1
Error: release nginx-ingress failed: Deployment in version "v1beta1" cannot be handled as a Deployment: v1beta1.Deployment.Spec: v1beta1.DeploymentSpec.Template: v1.PodTemplateSpec.Spec: v1.PodSpec.HostNetwork: ReadBool: expect t or f, but found ", error found in #10 byte of ...|Network":"true，rba|..., bigger context ...|s":{}}],"dnsPolicy":"ClusterFirst","hostNetwork":"true，rbac.create=true","serviceAccountName":"def|...
```

安装异常，我这里的helm安装的时候没有设置rbac，去掉重新安装即可

```
Copy[root@master /]# helm install stable/nginx-ingress \
> -n nginx-ingress \
> --namespace kube-system \
> --set controller.hostNetwork=true,controller.replicaCount=1
NAME:   nginx-ingress
LAST DEPLOYED: Tue Jul 30 15:19:59 2019
NAMESPACE: kube-system
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                      DATA  AGE
nginx-ingress-controller  1     <invalid>

==> v1/Pod(related)
NAME                                           READY  STATUS             RESTARTS  AGE
nginx-ingress-controller-67db56c89f-2hkxq      0/1    ContainerCreating  0         <invalid>
nginx-ingress-default-backend-878d64884-q4fmt  0/1    ContainerCreating  0         <invalid>

==> v1/Service
NAME                           TYPE          CLUSTER-IP      EXTERNAL-IP  PORT(S)                     AGE
nginx-ingress-controller       LoadBalancer  10.108.107.199  <pending>    80:31412/TCP,443:31392/TCP  <invalid>
nginx-ingress-default-backend  ClusterIP     10.107.244.59   <none>       80/TCP                      <invalid>

==> v1beta1/Deployment
NAME                           READY  UP-TO-DATE  AVAILABLE  AGE
nginx-ingress-controller       0/1    1           0          <invalid>
nginx-ingress-default-backend  0/1    1           0          <invalid>

==> v1beta1/PodDisruptionBudget
NAME                           MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
nginx-ingress-controller       1              N/A              0                    <invalid>
nginx-ingress-default-backend  1              N/A              0                    <invalid>


NOTES:
The nginx-ingress controller has been installed.
It may take a few minutes for the LoadBalancer IP to be available.
You can watch the status by running 'kubectl --namespace inkube-systemet services -o wide -w nginx-ingress-controller'

An example Ingress that makes use of the controller:

  apiVersion: extensions/v1beta1
  kind: Ingress
  metadata:
    annotations:
      kubernetes.io/ingress.class: nginx
    name: example
    namespace: foo
  spec:
    rules:
      - host: www.example.com
        http:
          paths:
            - backend:
                serviceName: exampleService
                servicePort: 80
              path: /
    # This section is only required if TLS is to be enabled for the Ingress
    tls:
        - hosts:
            - www.example.com
          secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

  apiVersion: v1
  kind: Secret
  metadata:
    name: example-tls
    namespace: foo
  data:
    tls.crt: <base64 encoded cert>
    tls.key: <base64 encoded key>
  type: kubernetes.io/tls
```

### 使用externalIP方式对外暴露服务：[#](https://www.cnblogs.com/hongdada/p/11270704.html#2728671991)

```
Copyhelm install stable/nginx-ingress \
-n nginx-ingress \
--namespace kube-system  \
--set controller.service.externalIPs[0]=18.16.202.163
```

查看：

```
Copy[root@master /]# kubectl get po,svc -n kube-system
NAME                                                 READY   STATUS        RESTARTS   AGE
pod/nginx-ingress-controller-7d4f8b4fbc-rv52j        1/1     Running       0          44m
pod/nginx-ingress-default-backend-59944969d4-df8cl   1/1     Running       0          44m

NAME                                    TYPE           CLUSTER-IP       EXTERNAL-IP     PORT(S)                      AGE
service/nginx-ingress-controller        LoadBalancer   10.103.227.228   18.16.202.163   80:30289/TCP,443:30669/TCP   44m
service/nginx-ingress-default-backend   ClusterIP      10.106.189.109   <none>          80/TCP                       44m
```

## 删除nginx-ingress[#](https://www.cnblogs.com/hongdada/p/11270704.html#4144380445)

删除`nginx-ingress`

```
Copy[root@master /]# helm delete nginx-ingress
release "nginx-ingress" deleted
[root@master /]# helm ls --all nginx-ingress
NAME            REVISION    UPDATED                     STATUS  CHART               APP VERSION NAMESPACE    
nginx-ingress   1           Tue Jul 30 14:31:01 2019    DELETED nginx-ingress-0.9.5 0.10.2      kube-system
[root@master /]# helm delete --purge nginx-ingress
release "nginx-ingress" deleted
[root@master /]# helm ls --all nginx-ingress
```

使用`--purge`参数可以彻底删除release不留下记录，否则下一次部署的时候不能使用重名的release。

## 安装成功查看[#](https://www.cnblogs.com/hongdada/p/11270704.html#1266272549)

查看

```
Copy[root@master /]# kubectl get po,svc -n kube-system -o wide
NAME                                                 READY   STATUS    RESTARTS   AGE   IP              NODE     NOMINATED NODE   READINESS GATES
pod/nginx-ingress-controller-b89575c7f-2xtkk         1/1     Running   0          13m   18.16.202.163   master   <none>           <none>
pod/nginx-ingress-default-backend-7b8b45bd49-g4mbz   1/1     Running   0          13m   10.244.0.23     master   <none>           <none>

NAME                                    TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE   SELECTOR
service/nginx-ingress-controller        LoadBalancer   10.111.25.193    <pending>     80:31577/TCP,443:31246/TCP   13m   app=nginx-ingress,component=controller,release=nginx-ingress
service/nginx-ingress-default-backend   ClusterIP      10.106.126.222   <none>        80/TCP                       13m   app=nginx-ingress,component=default-backend,release=nginx-ingress
```

helm查看：

```
Copy[root@master /]# helm ls --all nginx-ingress
NAME            REVISION    UPDATED                     STATUS      CHART               APP VERSION NAMESPACE    
nginx-ingress   1           Tue Jul 30 14:39:58 2019    DEPLOYED    nginx-ingress-0.9.5 0.10.2      kube-system
```

访问验证：

```
Copy[root@master /]# curl http://18.16.202.163
default backend - 404
```

http测试：

```
Copy[root@master /]# curl -I http://18.16.202.163/healthz/
HTTP/1.1 200 OK
Server: openresty/1.15.8.1
Date: Tue, 20 Aug 2019 10:11:11 GMT
Content-Type: text/html
Content-Length: 0
Connection: keep-alive
```

https:

```
Copy[root@master /]# curl -kI  https://k8s.hongda.com/
HTTP/1.1 200 Connection established

HTTP/1.1 200 OK
Server: openresty/1.15.8.1
Date: Tue, 20 Aug 2019 16:22:06 GMT
Content-Type: text/html; charset=utf-8
Content-Length: 990
Connection: keep-alive
Vary: Accept-Encoding
Accept-Ranges: bytes
Cache-Control: no-store
Last-Modified: Mon, 17 Dec 2018 09:04:43 GMT
Strict-Transport-Security: max-age=15724800; includeSubDomains
```