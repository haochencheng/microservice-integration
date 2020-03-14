##### 创建 Provisioner

要使用 StorageClass，我们就得安装对应的自动配置程序。比如：我们这里存储后端使用的是 NFS，那么我们就需要使用到一个对应的自动配置程序。支持 NFS 的自动配置程序就是 [nfs-client](https://github.com/kubernetes-incubator/external-storage/tree/master/nfs-client)，我们把它称作 Provisioner。这个程序可以使用我们已经配置好的 NFS 服务器，来自动创建持久卷，也就是自动帮我们创建 PV。

- 以 Deployment 方式部署一个 Provisioner

根据实际情况将下面的环境变量 `NFS_SERVER`、`NFS_PATH` 和 NFS 相关配置替换成你的对应的值。

####	nfs-client.yaml

```yaml
kind: Deployment
apiVersion: extensions/v1beta1
metadata:
  name: nfs-client-provisioner
spec:
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: nfs-client-provisioner
    spec:
      serviceAccountName: nfs-client-provisioner
      containers:
        - name: nfs-client-provisioner
          image: quay.io/external_storage/nfs-client-provisioner:latest
          volumeMounts:
            - name: nfs-client-root
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME
              value: fuseim.pri/ifs
            - name: NFS_SERVER
              value: 192.168.1.5
            - name: NFS_PATH
              value: /opt/nfs-data
      volumes:
        - name: nfs-client-root
          nfs:
            server: 192.168.1.5
            path: /opt/nfs-data
```

- 给 nfs-client-provisioner 创建 ServiceAccount

从 Kubernetes 1.6 版本开始，API Server 启用了 RBAC 授权。Provisioner 要想在 Kubernetes 中创建对应的 PV 资源，就得有对应的权限。

这里我们新建一个名为 nfs-client-provisioner 的 ServiceAccount 并绑定在一个名为 nfs-client-provisioner-runner 的 ClusterRole 上。该 ClusterRole 包含对 PersistentVolumes 的增、删、改、查等权限。

####	nfs-client-sa.yaml

```
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nfs-client-provisioner

---
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-client-provisioner-runner
rules:
  - apiGroups: [""]
    resources: ["persistentvolumes"]
    verbs: ["get", "list", "watch", "create", "delete"]
  - apiGroups: [""]
    resources: ["persistentvolumeclaims"]
    verbs: ["get", "list", "watch", "update"]
  - apiGroups: ["storage.k8s.io"]
    resources: ["storageclasses"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["events"]
    verbs: ["list", "watch", "create", "update", "patch"]

---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: run-nfs-client-provisioner
subjects:
  - kind: ServiceAccount
    name: nfs-client-provisioner
    namespace: default
roleRef:
  kind: ClusterRole
  name: nfs-client-provisioner-runner
  apiGroup: rbac.authorization.k8s.io
```

- 创建 StorageClass 对象

这里我们创建了一个名为 course-nfs-storage 的 StorageClass 对象，注意下面的 Provisioner 对应的值一定要和上面的 Deployment下面 PROVISIONER_NAME 这个环境变量的值一样。

```
$ vim nfs-client-class.yaml

apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: course-nfs-storage
provisioner: fuseim.pri/ifs # or choose another name, must match deployment's env PROVISIONER_NAME'
```

使用 Kubectl 命令建立这个 StorageClass。

##### 手动创建的一个 PVC 对象

- 新建一个 PVC 对象

我们这里就来建立一个能使用 StorageClass 资源对象来动态建立 PV 的 PVC，要创建使用 StorageClass 资源对象的 PVC 有以下两种方法。

方法一：在这个 PVC 对象中添加一个 Annotations 属性来声明 StorageClass 对象的标识。

```
# 这里我们声明了一个 PVC 对象，采用 ReadWriteMany 的访问模式并向 PV 请求 100Mi 的空间。
$ vim test-pvc.yaml

kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: test-pvc
  annotations:
    volume.beta.kubernetes.io/storage-class: "course-nfs-storage"
spec:
  accessModes:
  - ReadWriteMany
  resources:
    requests:
      storage: 100Mi
```

方法二：把名为 course-nfs-storage 的 StorageClass 设置为 Kubernetes 的默认后端存储。

```sh
kubectl patch storageclass course-nfs-storage -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
```

上面这两种方法都是可以的，为了不影响系统的默认行为，这里我们采用第一种方法，直接使用 YAML 文件创建即可。

```
$ kubectl create -f test-pvc.yaml
persistentvolumeclaim "test-pvc" created
```

创建完成后，我们来看看对应的资源是否创建成功。

```
$ kubectl get pvc
NAME       STATUS    VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS         AGE
pvc1-nfs   Bound     pv1-nfs                                    1Gi        RWO                                 4h
test-pvc   Bound     pvc-3d8d6ecf-9a13-11e8-9a96-001c42c61a79   100Mi      RWX            course-nfs-storage   41s

$ kubectl get pv
NAME                                       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM              STORAGECLASS         REASON    AGE
pv1-nfs                                    1Gi        RWO            Recycle          Bound       default/pvc1-nfs                                  5h
pv2-nfs                                    2Gi        RWO            Recycle          Available                                                     1h
pvc-3d8d6ecf-9a13-11e8-9a96-001c42c61a79   100Mi      RWX            Delete           Bound       default/test-pvc   course-nfs-storage             2m
```

从上面的结果我们可以看到一个名为 test-pvc 的 PVC 对象创建成功并且状态已经是 Bound 了。对应也自动创建了一个名为 pvc-3d8d6ecf-9a13-11e8-9a96-001c42c61a79 的 PV 对象，其访问模式是 RWX，回收策略是 Delete。STORAGECLASS 栏中的值也正是我们创建的 StorageClass 对象 course-nfs-storage。