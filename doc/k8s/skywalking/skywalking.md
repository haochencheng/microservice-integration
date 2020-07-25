### 安装skywalking
官网
https://github.com/apache/skywalking-kubernetes

Deploy SkyWalking and Elasticsearch 7 (default)
```bash

git clone https://github.com/apache/skywalking-kubernetes
cd chart
helm repo add elastic https://helm.elastic.co

helm dep up skywalking
helm install skywalking skywalking -n skywalking --values ./skywalking/values-es6.yaml
```


```bash
helm dep up skywalking
```
