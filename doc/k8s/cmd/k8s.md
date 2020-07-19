### 查看日志

```bash
 kubectl logs -f jenkins-67d666c998-8kmcr --namespace jenkins
```

### 查看pv

 ```bash
 kubectl get pvc -n jenkins
```

### 重启 delployment

```bash
kubectl patch deployment <deployment-name> \
  -p '{"spec":{"template":{"spec":{"containers":[{"name":"<container-name>","env":[{"name":"RESTART_","value":"'$(date +%s)'"}]}]}}}}'
```

### 查看 delployment

```bash
kubectl get deployment
```

### 查看发布过程

```bash
 kubectl rollout status deploy deployment
```

# 查看集群信息

```bash
kubectl cluster-info

kubectl get deployment --all-namespaces
kubectl get svc  --all-namespaces
kubectl get pod  -o wide  --all-namespaces


```

### 导出yaml
```bash
# 注意: --export 是为了去除当前正在运行的这个deployment生成的一些状态，我们用不到就过滤掉
kubectl get deployment/nginx -o=yaml --export  > new.yaml
```

### 设置容易默认时区

通过 Pod Preset 设置时区
![tz-pod](allow-tz-env.yaml)

