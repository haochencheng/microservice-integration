持久化k8s存储

# 创建配置文件

```
sudo vi /etc/exports
/Volumes/data -alldirs -mapall=501 -network 192.168.1.5 -mask 255.255.254.0
```

其中：
`/Volumes/data`: 要共享的本机目录
`-alldirs` 挂载该目录下的所有子目录
`-mapall=501`: 使用用户名ID为501的权限。该501的获取应该通过在`id`命令获取
`-network 192.168.0.0 -mask 255.255.254.0`: 允许连接的网络范围

# 启动服务

```
 sudo nfsd enable
 sudo nfsd disable
 sudo nfsd start
 sudo nfsd stop
 sudo nfsd restart
 sudo nfsd status
```

## 查看共享状态

```
  showmount -e 

  showmount -e 192.168.99.1
```

k8s 挂载出现

```
access denied by server while mounting
```

修改/etc/nfs.conf

```
 nfs.server.mount.require_resv_port = 0 
```

检查是否正确
```bash
 sudo nfsd checkexports
```
重启nfs

修改配置

```bash
sudo vim /etc/exports
##  改为：
/opt/nfs-data -alldirs  -maproot=root:wheel -rw -network=192.168.0.0 -mask=255.255.0.0
```

本地挂载  
```bash
sudo mount -t nfs -o nolock,nfsvers=3,vers=3 192.168.43.85:/opt/nfs-data /tmp/test
```
