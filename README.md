# 吞吐量比较 Springmvc vs Spring WebFlux vs Springmvc(虚拟线程)


## 测试环境

### 系统版本

OpenJDK 19

Spring Boot: v3.0.5

Apache Tomcat/10.1.7

### API

GET /localhost/slowAPI?timeSlowness=150

api响应时间：>150ms

### JMeter 参数

> Number of Requests: 1000
> Ramp-up periods: 50
> Loop count: 1000
> Sum of requests: 1000 * 1000 = 1M



## 测试结果



### Spring MVC普通线程



| Label        | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min  | Max  | Error % | Throughput | Received KB/sec | Sent KB/sec |
| ------------ | --------- | ------- | ------ | -------- | -------- | -------- | ---- | ---- | ------- | ---------- | --------------- | ----------- |
| HTTP Request | 1000000   | 731     | 772    | 782      | 788      | 808      | 150  | 933  | 0.00%   | 1282.2765  | 210.35          | 170.3       |
| TOTAL        | 1000000   | 731     | 772    | 782      | 788      | 808      | 150  | 933  | 0.00%   | 1282.2765  | 210.35          | 170.3       |

### Spring WebFlux异步非阻塞式
| Label        | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min  | Max  | Error % | Throughput | Received KB/sec | Sent KB/sec |
| ------------ | --------- | ------- | ------ | -------- | -------- | -------- | ---- | ---- | ------- | ---------- | --------------- | ----------- |
| HTTP Request | 1000000   | 243     | 235    | 319      | 348      | 460      | 150  | 1453 | 0.00%   | 3212.46952 | 526.14          | 451.75      |
| TOTAL        | 1000000   | 243     | 235    | 319      | 348      | 460      | 150  | 1453 | 0.00%   | 3212.46952 | 526.14          | 451.75      |

### 虚拟线程


| Label        | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min  | Max  | Error % | Throughput | Received KB/sec | Sent KB/sec |
| ------------ | --------- | ------- | ------ | -------- | -------- | -------- | ---- | ---- | ------- | ---------- | --------------- | ----------- |
| HTTP Request | 1000000   | 176     | 169    | 206      | 224      | 286      | 150  | 543  | 0.00%   | 4388.6597  | 718.77          | 582.87      |
| TOTAL        | 1000000   | 176     | 169    | 206      | 224      | 286      | 150  | 543  | 0.00%   | 4388.6597  | 718.77          | 582.87      |



## 总结

因为测试时候，服务端和客户端在本地系统测，客户端也无法支持太大规模的并发。如果优化下机子或者客户端，吞吐量应该会更好看。所以在这里只是针对不同模型的吞吐量做个比较。

### spring MVC vs Spring WebFlux
异步非阻塞式带来的吞吐量提升还是很明显的,但是仅仅只是针对于IO非常密集的场景会更明显一点。

### 普通线程 vs 虚拟线程
从Average和Throughput来看，使用虚拟线程和普通线程差距还是很明显的。虚拟线程对于每个API（Average 176ms）的延迟和理论上（150ms）差距不大。