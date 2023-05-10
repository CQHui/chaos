# 吞吐量比较 Spring MVC vs Spring WebFlux vs Springmvc(虚拟线程)


## 测试环境

### 系统版本

OpenJDK 19

Spring Boot: v3.0.5

Apache Tomcat/10.1.7

### API

GET localhost:8080/slowAPI?timeSlowness=150
GET localhost:8080/webflux/slowAPI?timeSlowness=150

api响应时间：> 150ms

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

spring mvc是非常经典的阻塞IO，遇到阻塞后throughput就会明显受到影响。Spring WebFlux是基于非阻塞的响应式编程（Reactive Programming），有相对好看的吞吐量。但是很难兼容传统的阻塞IO的组件，如JDBC。响应式编程似乎没有在Java领域引起很大的活跃度（可能为了解决非阻塞有很多工作要做）。
而虚拟线程，基本不需要去修改原有阻塞相关的组件或者代码，却能够用阻塞的代码风格去实现非阻塞逻辑。对开发者或者各种组件的兼容性更好。Java 架构师都这么说了。

`Brian Goetz: "I think Project Loom is going to kill Reactive Programming"`

### spring MVC vs Spring WebFlux
异步非阻塞式带来的吞吐量提升还是很明显的。还有一个细节，WebFlux下的最大请求时间Max是比MVC下的要更大的。

### 普通线程 vs 虚拟线程
从Average和Throughput来看，使用虚拟线程和普通线程差距还是很明显的。虚拟线程对于每个API（Average 176ms）的延迟和理论上（150ms）差距不大。


### Golang framework Gin
再附一张golang实现的相同api的Throughput。对比看来，协程的吞吐接近于JAVA的虚拟线程吞吐量(运行环境和IDE皆不同, 并不能直接说明golang的性能比Java低, 不过总体还是接近)

``` go

package main

import (
"net/http"
"strconv"
"time"

	"github.com/gin-gonic/gin"
)

// handle方法
func Pong(c *gin.Context) {
timeStr := c.DefaultQuery("timeSlowness", "1000")
seconds, _ := strconv.Atoi(timeStr)
time.Sleep(time.Duration(time.Duration(seconds) * time.Millisecond))
c.String(http.StatusOK, "success")
}

func main() {

	gin.DisableConsoleColor()

	r := gin.Default()

	r.GET("/slowAPI", Pong)

	r.Run(":8080")
}

```

| Label        | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min  | Max  | Error % | Throughput | Received KB/sec | Sent KB/sec |
| ------------ | --------- | ------- | ------ | -------- | -------- | -------- | ---- | ---- | ------- | ---------- | --------------- | ----------- |
| HTTP Request | 1000000   | 212     | 204    | 269      | 297      | 359      | 150  | 1737 | 0.00%   | 3821.84106 | 459.07          | 507.59      |
| TOTAL        | 1000000   | 212     | 204    | 269      | 297      | 359      | 150  | 1737 | 0.00%   | 3821.84106 | 459.07          | 507.59      |
