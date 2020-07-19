
### 执行名称

```bash
kubectl create -f admin-role.yml
```

# 获取admin-token的secret名字
```bash
kubectl -n kube-system get secret|grep admin-token
```

# 获取token的值
```
kubectl -n kube-system describe secret admin-token-nwphb
```
或者
```
kubectl describe secret admin-token-nwphb
```