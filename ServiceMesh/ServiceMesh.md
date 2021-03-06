# ServiceMesh

## 微服务
介绍ServiceMesh之前，首先要说的就是微服务

* 用一张经典的图来看一下微服务和传统的单体架构区别
<br>
图中左边是单体架构的集群，右边是微服务集群。

![微服务](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice.jpg)

#### 微服务有点

独立部署，灵活扩展
,资源的有效隔离
,单个服务的复杂度可控
, 独立按需扩展，技术选型灵活，容错，可用性高

* 企业常用的微服务架构

![架构图](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-1.png)

* **服务接入层**：企业暴露到外部访问的入口，一般通过防火墙等。
* **网关层**：服务网关是介于客户端和服务端的中间层，所有的外部请求会先经过服务网关，为企业应用提供统一的访问控制入口。服务网关是微服务架构下的服务拆分，聚合，路由，认证以及流控综合体现。
* **支撑服务层**：为企业应用提供运行所需的支撑环境，包括注册发现，集中配置，容错限流，认证授权，日志聚合，监测告警，消息服务等
* **业务服务层**：业务服务是企业应用的核心所在，为企业领域应用的具体实现，一般进一步拆分为基础服务（基础功能）和聚合服务（综合场景）。
* **平台服务层**：为企业应用提供运行所需的软件资源，包括应用服务器，应用发布管理，应用镜像包管理，服务治理。
* **基础设施层**：为企业应用提供运行所需的硬件资源，包括计算资源，网络资源，存储资源，基本的安全策略控制等。


### 微服务组件使用

![图2](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-2.png)

上面的图中可以得到一个结论：在微服务架构下，核心都在网关服务、支撑服务两个层面。

目前的Spring Cloud以及Dubbo都有完整套件来实现，如果需要直接拿来用就可以了，很爽也很方便，使用方式就是在每一个服务器加上相应的组件就ok了

![数据收集](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-3.png)

![服务注册与发现](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-4.png)


##### 微服务的痛点：
1. 业务技术团队需要花时间精力去学习这些组件，
2. 只要增加一个服务就必须要引入所有的组件
3. 组件升级周期长
4. 多版本语言需要开发多个版本的Client

### 如何解决？

问题是有了，下面就要想办法解决问题了

既然问题是因为业务和支撑模块耦合，那就想办法解耦喽

* **将服务拆成两个进程**

![chai](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-6.png)

> * 一个进程实现业务逻辑，即上面的白色方块
> * 一个进程实现底层技术体系，上面的蓝色方块

这样就实现了<font color=red >**业务归业务，技术归技术** </font>，如果所有的服务都实现解耦，最终整个架构就会变成：
![tu10](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-10.png)

这种模式也被形象的成为SideCar模式：
![tu11](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-11.png)

## 落地

ServiceMesh本质上只是一概念，目前页已经有开源框架真正做到了落地，如Linkerd和Istio。
下面主要说一说Istio,，它是由Google和IBM合作开发,算是目前把ServiceMesh这个概念落实的最好的一个开源框架了

## Istio

### Istio是什么？
Istio是谷歌、IBM、Lyft等公司贡献的开源Service Mesh组件。它实现的目标就是让业务开发不再关注微服务之间如何调用、管理、监控等非功能性需求，而是让Istio来处理这些问题。Istio和Kubernetes有天然的支持。

### 为什么要用Istio

Istio 提供一种简单的方式来为已部署的服务建立网络，该网络具有负载均衡、服务间认证、监控等功能，只需要对服务的代码进行一点或不需要做任何改动。想要让服务支持 Istio，只需要在您的环境中部署一个特殊的 sidecar 代理，使用 Istio 控制平面功能配置和管理代理，拦截微服务之间的所有网络通信：

* HTTP、gRPC、WebSocket 和 TCP 流量的自动负载均衡。
* 通过丰富的路由规则、重试、故障转移和故障注入，可以对流量行为进行细粒度控制。
* 可插入的策略层和配置 API，支持访问控制、速率限制和配额。
* 对出入集群入口和出口中所有流量的自动度量指标、日志记录和追踪。
* 通过强大的基于身份的验证和授权，在集群中实现安全的服务间通信。

### Istio做了什么？
先来看一下一个常规的微服务需要用到的组件
![tu11](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-12.png)

前面也说到过，ServiceMesh的理念就是把技术层与业务分开，让业务技术人员专注于业务开发，Istio就要替代一些组件。如下：
![tu12](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-13.png)

使用Istio之后，再来看我们的微服务组件架构图，你会发现，之前的API网关、服务注册中心、负载均衡、熔断等组件都不需要了，这些都由Istio来处理。

最后微服务会只剩下服务本身和一个代理（SideCar）。Istio使用Envoy作为代理实现服务的动态发现、负载均衡、熔断等等。Envoy是基于C++开发的代理。

![tu14](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-14.png)

在Kubernetes集群里，你可以为每个微服务通过一个Policy文件描述代理服务，Istio Pilot会统一收集所有的代理注册信息，用来进行服务之间的请求调度。Istio的调度机制如下：

 



- Service Mesh解析所有的请求，并把这些请求发送到本地的代理；

- 负载均衡会将请求分发到某个代理实例；

- 代理实例会进行检查，例如ACL、Quota等等；

- 如果成功，目标服务返回结果给请求调用者。

![tu14](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/microservice-15.png)

所有的服务追踪信息可以统一通过Istio Mixer进行收集，并发送到Prometheus，用Grafana进行数据可视化展示。

### Istio架构

Istio基本架构图如下图所示，网格东西向及南北向的流量控制，核心思路是由Pilot维护管理策略，并通过标准接口下发到Envoy Proxy中，由Envoy最终实现流量的转发。

![架构图](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/istio-1.png)

Istio 服务网格逻辑上分为数据平面和控制平面。

> 数据平面由一组以 sidecar 方式部署的智能代理（Envoy）组成。这些代理可以调节和控制微服务及 Mixer 之间所有的网络通信。

> 控制平面负责管理和配置代理来路由流量。此外控制平面配置 Mixer 以实施策略和收集遥测数据。




### 数据面

Istio 使用 Envoy 代理的扩展版本，Envoy 是以 C++ 开发的高性能代理，其实就是一个proxy转发器，用于调解服务网格中所有服务的所有入站和出站流量。
服务间的所有请求都会经过Envoy然后由Envoy转发，那Envoy如何转发请求呢？当然是需要一些规则的，然后按照这些规则转发。
<br>
Envoy规则配置主要下面四个东西。

![架构图](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/envoy_1.png)

* listener

	  envoy既然是proxy，专门做转发，那肯定就得监听一个端口，接入请求，然后才能够根据策略转发，这个监听的端口称为listener

* endpoint

	这个是目标的ip地址和端口，这个是proxy最终将请求转发到的地方。

* cluster

	当我们集群部署时，会有多个相同的服务，这样就会有三个Ip和端口，他们三个组在一起就是cluster，从这里就可推测出来，Envoy肯定是可以支持负载均衡

* route

	这个大家应该明白什么作用了，其实就是和RabbitMQ的routing功能类似，有时候多个cluster具有类似的功能，但是是不同的版本号，可以通过route规则，选择将请求路由到某一个版本号，也即某一个cluster。


下面是一个配置的简单例子


![架构图](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/envoy-2.jpg)

> 如图所示，LISTENER被配置为监听本地127.0.0.1的10000接口，ROUTE配置为某个URL的前缀转发到哪个CLUSTER，CLUSTER里面配置负载均衡策略，HOSTS里面是所有的ENDPOINT。

当然这些配置也可以是动态的，把所有配置放在统一的地方维护，也就是放在Discovery Service中，过一段时间去这里拿一下配置，就修改了转发策略。对应控制面的Envoy API。

Envoy 的许多内置功能被 Istio 发扬光大，例如：

1. 动态服务发现
1. 负载均衡
1. TLS 终止
1. HTTP/2 & gRPC 代理
1. 熔断器
1. 健康检查、基于百分比流量拆分的灰度发布
1. 故障注入
1. 丰富的度量指标

#### Envoy 在Istio中的应用

![envoy3](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/envoy-3.jpg)

前面我们有说过在ServerMesh中非常重要的一点的就是SideCar的不熟模式，在Istio中的SideCar就是基于Envory不熟到整个环境中的，Sidecars负责拦截服务之间的网络通信。

![envoy4](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/envory-4.jpg)

从上面图展示了不带sidecar和带了sidecar的区别<br>
Envoy是以“进程外”的方式部署，这意味着它和服务进程相互独立，由此带来了好处前面也有说过，无代码侵入，可以跨语言跨平台，开发人员可以专注于业务开发


Envoy 被部署为 sidecar，和对应服务在同一个 Kubernetes pod 中。这允许 Istio 将大量关于流量行为的信号作为属性提取出来，而这些属性又可以在 Mixer 中用于执行策略决策，并发送给监控系统，以提供整个网格行为的信息。



### 控制面Pilot


Pilot是Istio中的一个核心组件，它为整个mesh提供了标准的服务模型, 该标准服务模型独立于各种底层平台, Pilot以插件方式对接不同的服务发现平台, 解析用户输入的流控配置, 转换为统一的服务发现和流量控制模型, 并以xDS方式下发到数据面。

![pilot1](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/pilot-1.jpg)

上面图片就是pilot的架构，

* Platform Adapter

   这个可以理解为适配器，可以对接多种不同的平台获取服务注册信息，并转换成Istio通用的抽象模型。

* Abstract Model(统一的服务模型)

   Pilot 定义了网格中服务的标准模型，这个标准模型独立于各种底层平台。由于有了该标准模型，各个不同的平台可以通过适配器和 Pilot 对接，将自己特有的服务数据格式转换为标准格式，填充到 Pilot 的标准模型中。例如：可以通过Kubernetes API 服务器得到 kubernetes 中 service 和 pod 的相关信息，然后翻译为标准模型提供给 Pilot 使用。当然也可以通过适配器从Mesos, Cloud Foundry, Consul 等平台中获取服务信息。

* 标准的数据面API

	Pilot 使用了一套起源于 Envoy 项目的标准数据平面 API 来将服务信息和流量规则下发到数据平面的 sidecar 中。可以想象，有了这个标准API后，数据平面和控制平面实现解耦。为多种数据平面 sidecar 实现提供了可能性

* 业务 DSL 语言<br>
	Pilot 还定义了一套 DSL（Domain Specific Language）语言，DSL 语言提供了面向业务的高层抽象，可以被运维人员理解和使用。运维人员使用该 DSL 定义流量规则并下发到 Pilot，这些规则被 Pilot 翻译成数据平面的配置，再通过标准 API 分发到 Envoy 实例，可以在运行期对微服务的流量进行控制和调整。

	Pilot 的规则 DSL 是采用 K8S API Server 中的 Custom Resource (CRD) 实现的。

	通过运用不同的流量规则，可以对网格中微服务进行精细化的流量控制，如按版本分流，断路器，故障注入，灰度发布等。

### Isti 的流量管理

(demo实例演示Istio如何实现控制流量)

我们可以通过下图了解Istio 流量管理的基本流程涉及到的相关组件。图中红色的线表示控制流，黑色的线表示数据流。蓝色部分为相关的组件。

![liuliang](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/pilot-2.jpg)

* Discovery Services

	 从 Service provider（如kubernetes或者consul）中获取服务信息<br>
将服务信息和流量规则转化为数据平面可以理解的格式，通过标准的数据平面 API 下发到网格中的各个 sidecar 中

* Pilot-agent

	该进程根据 K8S API Server 中的配置信息生成 Envoy 的配置文件，并负责启动 Envoy 进程。不过Envoy 的大部分配置信息都是通过 xDS 接口从 Pilot 中动态获取的，因此 Agent 生成的只是用于初始化 Envoy 的少量静态配置。

	热更新envoy：<br>
	因为有些静态资源，如果TLS的证书，envoy还不支持动态下发，因而需要重新静态配置，然后pilot-agent负责将envoy进行热重启加载。

从下面的图可以看出所谓热重启的主要步骤
<br>

> 启动另外一个envoy2进程（Secondary process）
<br> envoy2通知envoy1（Primary process）关闭其管理的端口，由envoy2接管
<br> 通过UDS把envoy1可用的listen sockets拿过来
<br>envoy2初始化成功，通知envoy1在一段时间内（drain-time-s）优雅关闭正在工作的请求
<br> 到了时间（parent-shutdown-time-s），envoy2通知envoy1自行关闭
<br> envoy2升级为envoy1
<br>

![envory4](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/envoy_4.png)
<br>
重启的时候，会先启动一个备用进程，将转发的统计数据通过shared memory在两个进程间共享。

# Istio中的网关

在一个典型的网格中，通常有一个或多个用于接受外部请求，将流量引入网格的负载均衡器（我们称之为 gateway）。 然后流量通过边车网关（sidecar gateway）流经内部服务。 
<br>
下图描绘了网关在网格中的使用情况。

![getway-1](https://github.com/shanyao19940801/BookeNote/blob/master/ServiceMesh/file/gateway-1.svg)




### Mixer

Mixer 是一个独立于平台的组件，负责在服务网格上执行访问控制和使用策略，并从 Envoy 代理和其他服务收集遥测数据。代理提取请求级属性，发送到 Mixer 进行评估。
[参考](https://www.yunforum.net/group-topic-id-1893.html)
    

### Citadel

Citadel 通过内置身份和凭证管理赋能强大的服务间和最终用户身份验证。可用于升级服务网格中未加密的流量，并为运维人员提供基于服务标识而不是网络控制的强制执行策略的能力。从 0.5 版本开始，Istio 支持基于角色的访问控制，以控制谁可以访问您的服务，而不是基于不稳定的三层或四层网络标识。

### Galley

Galley 将担任 Istio 的配置验证，获取配置，处理和分配组件的任务。它负责将其余的 Istio 组件与从底层平台（例如 Kubernetes）获取用户配置的细节中隔离开来。



### 性能瓶颈

不论什么样的框架都不是完美的，虽然ServiceMesh优势很明显，但不可否认的是ServiceMesh的确实对应用的性能带来了损耗，可以从两个方面看待此问题：


> 
1.数据面板中Sidecar的加入增加了业务请求的链路长度，必然会带来性能的损耗，由此延伸可知请求转发性能的高低必然会成为各个Sidecar能否最终胜出的关键点之一。
>
2.控制面板采用的是集中式管理，统一负责请求的合法性校验、流控、遥测数据的收集与统计，而这要求Sidecar每转发一个请求，都需要与控制面板通讯，例如对应到Istio的架构中，这部分工作是由Mixer组件负责，那么可想而知这里必然会成为性能瓶颈之一，针对这个问题Istio官方给出了解决方案，即将Mixer的大部分工作下放到Sidecar中，对应到Envoy中就是新增一个MixerFilter来承担请求校验、流控、数据收集与统计工作，MixerFilter需要定时与Istio通讯以批量上报数据与拉取最新配置数据。


# Docker

Docker 是一个开源的应用容器引擎，基于 Go 语言 并遵从Apache2.0协议开源。
Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。
容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）,更重要的是容器性能开销极低。

# kubernetes
Kubernetes是一个开源的，用于管理云平台中多个主机上的容器化的应用，Kubernetes的目标是让部署容器化的应用简单并且高效（powerful）,Kubernetes提供了应用部署，规划，更新，维护的一种机制。
一个K8S系统，通常称为一个K8S集群（Cluster）。

这个集群主要包括两个部分：

一个Master节点（主节点）

一群Node节点（计算节点）

### Master节点

Master节点包括API Server、Scheduler、Controller manager、etcd。<br>
API Server是整个系统的对外接口，供客户端和其它组件调用，相当于“营业厅”。<br>
Scheduler负责对集群内部的资源进行调度，相当于“调度室”。<br>
Controller manager负责管理控制器，相当于“大总管”。

### Node节点
Node节点包括Docker、kubelet、kube-proxy、Fluentd、kube-dns（可选），还有就是Pod。

Pod是Kubernetes最基本的操作单元。一个Pod代表着集群中运行的一个进程，它内部封装了一个或多个紧密相关的容器。<br>
Docker.<br>
Kubelet，主要负责监视指派到它所在Node上的Pod，包括创建、修改、监控、删除等。<br>
Kube-proxy，主要负责为Pod对象提供代理。<br>
Fluentd，主要负责日志收集、存储与查询。



# 参考

[企业应用架构演化探讨：从微服务到Service Mesh](https://www.kubernetes.org.cn/5349.html)

[ServiceMesh究竟解决什么问题？](https://mp.weixin.qq.com/s?__biz=MjM5ODYxMDA5OQ==&mid=2651962194&idx=2&sn=7a2d8305181a394e1d01e885286a7dde&chksm=bd2d0e8e8a5a8798c17b6dcbbd0fb87ed519b685856bc480437b9ca03a665e5536d03264d91b&scene=21#wechat_redirect)

[使用 Istio治理微服务入门](https://mp.weixin.qq.com/s?__biz=MzU0MTMyMDg1NQ==&mid=2247483756&idx=1&sn=ee475f20b9900aec7e56ae03313bf149&chksm=fb2af294cc5d7b82509ef23d7268370588b997d1b169970bbbce08810488f098024a5a84cb78&scene=21#wechat_redirect)

[Service Mesh 在华为公有云的实践](https://gitbook.cn/books/5a1e7dca387c5b4ee351790b/index.html)

[微服务运维减负：Istio Service Mesh原理+实战](https://dbaplus.cn/news-134-2030-1.html?ad_check=1)

[四层代理和七层代理](http://www.voidcn.com/article/p-oifzqzvk-bnz.html)

[服务器发布几种方式](https://www.cnblogs.com/apanly/p/8784096.html)

[十分钟带你理解Kubernetes核心概念](http://www.dockone.io/article/932)

[浅谈Service Mesh体系中的Envoy](https://yq.aliyun.com/articles/606655)

[iptables详解:图文并茂理解iptables](http://www.zsythink.net/archives/1199/)

[pilot-agent和pilot-discovery](https://www.kubernetes.org.cn/4308.html) 

[GetWay](https://www.yangcs.net/posts/istio-ingress/)

[docker和k8s](https://www.jianshu.com/p/f1f94c6968f5)

[为什么 kubernetes 天然适合微服务](https://www.cnblogs.com/163yun/p/8855360.html)

# 问题
如何实现代理和服务通信 

##命令
export PATH="$PATH:/home/sqshanyao/istio-1.0.0/bin" 

* 查看网关

kubectl get gateway

kubectl exec -it $(kubectl get pod -l app=ratings -o jsonpath='{.items[0].metadata.name}') -c ratings -- curl productpage:9080/productpage | grep -o "<title>.*</title>"


export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')