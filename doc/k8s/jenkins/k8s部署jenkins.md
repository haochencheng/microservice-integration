###	jenkins镜像

```sh
docker pull jenkins
```

#### Jenkins 权限配置

此处直接将`jenkins-admin`集成了`cluster-admin`权限，可根据自己具体需要进行权限的设置。

jenkins-rbac.yaml

```yml
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: jenkins
  name: jenkins-admin
  namespace: jenkins

---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: jenkins-admin
  labels:
    k8s-app: jenkins
subjects:
  - kind: ServiceAccount
    name: jenkins-admin
    namespace: jenkins
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: rbac.authorization.k8s.io

---
```

####	Jenkins Deployment配置

jenkins-deployment.yaml

```yml
apiVersion: apps/v1beta2
kind: Deployment
metadata:
  name: jenkins
  namespace: jenkins
  labels:
    k8s-app: jenkins
spec:
  replicas: 1
  selector:
    matchLabels:
      k8s-app: jenkins
  template:
    metadata:
      labels:
        k8s-app: jenkins
    spec:
      containers:
      - name: jenkins
        image: jenkins
        imagePullPolicy: IfNotPresent
        volumeMounts:
        - name: jenkins-home
          mountPath: /var/jenkins_home
        - name: maven-repository
          mountPath: /Users/haochencheng/Workspace/maven-repo
        - name: docker
          mountPath: /usr/bin/docker
        - name: docker-sock
          mountPath: /var/run/docker.sock
        ports:
        - containerPort: 8080
        - containerPort: 50000
      volumes:
        - name: jenkins-home
          hostPath:
            path: /opt/jenkins_home
        - name: maven-repository
          hostPath:
            path: /opt/maven/repository
        - name: docker
          hostPath:
            path: /usr/bin/docker
        - name: docker-sock
          hostPath:
            path: /var/run/docker.sock
      serviceAccountName: jenkins-admin
---
```

#### Jenkins Service配置

jenkins-service.yaml

```yml
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: jenkins
  name: jenkins
  namespace: jenkins
  annotations:
    prometheus.io/scrape: 'true'
spec:
  ports:
    - name: jenkins
      port: 8080
      nodePort: 31888
      targetPort: 8080
    - name: jenkins-agent
      port: 50000
      nodePort: 50000
      targetPort: 50000
  type: NodePort
  selector:
    k8s-app: jenkins
```

```
{
  "kind": "Pod",
  "apiVersion": "v1",
  "metadata": {
    "name": "kube-apiserver-docker-desktop",
    "namespace": "kube-system",
    "selfLink": "/api/v1/namespaces/kube-system/pods/kube-apiserver-docker-desktop",
    "uid": "ef17e4e8-b030-11e9-974a-025000000001",
    "resourceVersion": "38404",
    "creationTimestamp": "2019-07-27T05:39:42Z",
    "labels": {
      "component": "kube-apiserver",
      "tier": "control-plane"
    },
    "annotations": {
      "kubernetes.io/config.hash": "7c4f3d43558e9fadf2d2b323b2e78235",
      "kubernetes.io/config.mirror": "7c4f3d43558e9fadf2d2b323b2e78235",
      "kubernetes.io/config.seen": "2019-07-27T05:38:36.718969884Z",
      "kubernetes.io/config.source": "file"
    }
  },
  "spec": {
    "volumes": [
      {
        "name": "ca-certs",
        "hostPath": {
          "path": "/etc/ssl/certs",
          "type": "DirectoryOrCreate"
        }
      },
      {
        "name": "etc-ca-certificates",
        "hostPath": {
          "path": "/etc/ca-certificates",
          "type": "DirectoryOrCreate"
        }
      },
      {
        "name": "k8s-certs",
        "hostPath": {
          "path": "/run/config/pki",
          "type": "DirectoryOrCreate"
        }
      },
      {
        "name": "usr-local-share-ca-certificates",
        "hostPath": {
          "path": "/usr/local/share/ca-certificates",
          "type": "DirectoryOrCreate"
        }
      },
      {
        "name": "usr-share-ca-certificates",
        "hostPath": {
          "path": "/usr/share/ca-certificates",
          "type": "DirectoryOrCreate"
        }
      }
    ],
    "containers": [
      {
        "name": "kube-apiserver",
        "image": "k8s.gcr.io/kube-apiserver:v1.14.3",
        "command": [
          "kube-apiserver",
          "--advertise-address=192.168.65.3",
          "--allow-privileged=true",
          "--authorization-mode=Node,RBAC",
          "--client-ca-file=/run/config/pki/ca.crt",
          "--enable-admission-plugins=NodeRestriction",
          "--enable-bootstrap-token-auth=true",
          "--etcd-cafile=/run/config/pki/etcd/ca.crt",
          "--etcd-certfile=/run/config/pki/apiserver-etcd-client.crt",
          "--etcd-keyfile=/run/config/pki/apiserver-etcd-client.key",
          "--etcd-servers=https://127.0.0.1:2379",
          "--insecure-port=0",
          "--kubelet-client-certificate=/run/config/pki/apiserver-kubelet-client.crt",
          "--kubelet-client-key=/run/config/pki/apiserver-kubelet-client.key",
          "--kubelet-preferred-address-types=InternalIP,ExternalIP,Hostname",
          "--proxy-client-cert-file=/run/config/pki/front-proxy-client.crt",
          "--proxy-client-key-file=/run/config/pki/front-proxy-client.key",
          "--requestheader-allowed-names=front-proxy-client",
          "--requestheader-client-ca-file=/run/config/pki/front-proxy-ca.crt",
          "--requestheader-extra-headers-prefix=X-Remote-Extra-",
          "--requestheader-group-headers=X-Remote-Group",
          "--requestheader-username-headers=X-Remote-User",
          "--secure-port=6443",
          "--service-account-key-file=/run/config/pki/sa.pub",
          "--service-cluster-ip-range=10.96.0.0/12",
          "--service-node-port-range=1-65535",
          "--tls-cert-file=/run/config/pki/apiserver.crt",
          "--tls-private-key-file=/run/config/pki/apiserver.key"
        ],
        "resources": {
          "requests": {
            "cpu": "250m"
          }
        },
        "volumeMounts": [
          {
            "name": "ca-certs",
            "readOnly": true,
            "mountPath": "/etc/ssl/certs"
          },
          {
            "name": "etc-ca-certificates",
            "readOnly": true,
            "mountPath": "/etc/ca-certificates"
          },
          {
            "name": "k8s-certs",
            "readOnly": true,
            "mountPath": "/run/config/pki"
          },
          {
            "name": "usr-local-share-ca-certificates",
            "readOnly": true,
            "mountPath": "/usr/local/share/ca-certificates"
          },
          {
            "name": "usr-share-ca-certificates",
            "readOnly": true,
            "mountPath": "/usr/share/ca-certificates"
          }
        ],
        "livenessProbe": {
          "httpGet": {
            "path": "/healthz",
            "port": 6443,
            "host": "192.168.65.3",
            "scheme": "HTTPS"
          },
          "initialDelaySeconds": 15,
          "timeoutSeconds": 15,
          "periodSeconds": 10,
          "successThreshold": 1,
          "failureThreshold": 8
        },
        "terminationMessagePath": "/dev/termination-log",
        "terminationMessagePolicy": "File",
        "imagePullPolicy": "IfNotPresent"
      }
    ],
    "restartPolicy": "Always",
    "terminationGracePeriodSeconds": 30,
    "dnsPolicy": "ClusterFirst",
    "nodeName": "docker-desktop",
    "hostNetwork": true,
    "securityContext": {},
    "schedulerName": "default-scheduler",
    "tolerations": [
      {
        "operator": "Exists",
        "effect": "NoExecute"
      }
    ],
    "priorityClassName": "system-cluster-critical",
    "priority": 2000000000,
    "enableServiceLinks": true
  },
  "status": {
    "phase": "Running",
    "conditions": [
      {
        "type": "Initialized",
        "status": "True",
        "lastProbeTime": null,
        "lastTransitionTime": "2020-03-07T14:36:25Z"
      },
      {
        "type": "Ready",
        "status": "True",
        "lastProbeTime": null,
        "lastTransitionTime": "2020-03-07T14:36:28Z"
      },
      {
        "type": "ContainersReady",
        "status": "True",
        "lastProbeTime": null,
        "lastTransitionTime": "2020-03-07T14:36:28Z"
      },
      {
        "type": "PodScheduled",
        "status": "True",
        "lastProbeTime": null,
        "lastTransitionTime": "2020-03-07T14:36:25Z"
      }
    ],
    "hostIP": "192.168.65.3",
    "podIP": "192.168.65.3",
    "startTime": "2020-03-07T14:36:25Z",
    "containerStatuses": [
      {
        "name": "kube-apiserver",
        "state": {
          "running": {
            "startedAt": "2020-03-07T14:36:26Z"
          }
        },
        "lastState": {
          "terminated": {
            "exitCode": 255,
            "reason": "Error",
            "startedAt": "2020-03-05T13:03:17Z",
            "finishedAt": "2020-03-07T14:36:15Z",
            "containerID": "docker://275c71e72dca794d6168e077587ec9bc45fc3095fdcd8fa55683aa86cb7f3112"
          }
        },
        "ready": true,
        "restartCount": 5,
        "image": "k8s.gcr.io/kube-apiserver:v1.14.3",
        "imageID": "docker-pullable://k8s.gcr.io/kube-apiserver@sha256:e29561119a52adad9edc72bfe0e7fcab308501313b09bf99df4a9638ee634989",
        "containerID": "docker://9dd5bc1a5dc527e8664c4fd25e8dbd04bc9a67dd2afe8dcb682406ac9536f916"
      }
    ],
    "qosClass": "Burstable"
  }
}
```

