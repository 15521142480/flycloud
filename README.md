
### 1、项目说明:
- flycloud-server 是基于Spring Cloud Alibaba + Nacos、Spring Security + oauth2、Mybatis-Plus + Velocity、SpringDoc、Redis + ElasticSearch、 Rabbitmq 等全栈主流技术栈构建的开源项目; 主要以商城项目为主,扩展项目为辅的一个飞翔云系统。
- 所属个人开发；为了方便学习开源公共服务，代码checkout下来无需修改配置即可直接运 -> 但要前提检查 1.在nacos上public命名空间下无服务时 2.公共服务如nacos、mysql等没有down掉(因为是1G服务器); 以方便有问题可以随时讨论。
- 项目目前在收尾的整合阶段，具体情况看下面的 3、 4、 5、 点。


### 2、交流和反馈
- 作者邮箱：        2570078967@qq.com
- 作者vx (phone)： 15521142480
- Github仓库：     https://github.com/15521142480/flycloud
- 演示地址：        http://106.15.42.115:7075 （flycloud-extend扩展模块下的文件后台系统,非主业务系统）


### 3、系统集成了:

| 技能             | 框架                            |
|----------------|-------------------------------|
| 核心框架           | Spring Boot 2.6               |
| 分布式/注册中心框架     | Spring-cloud-alibaba 2021.0.1 + Nacos 2.1.1 |
| 安全/授权框架        | Spring-security + oauth2.0    |
| 数据库/持久层/自动生成框架 | Mysql + Mybatis-plus、 Velocity |
| 缓存/搜索引擎框架      | Ehcache、 Redis、ElasticSearch (待续) |
| 分布式事务/消息队列框架   | Seata (待续)、RocketMQ (待续)      |
| 文档框架           | SpringDoc + Swagger3.0        |
| 日志框架框架         | Logback                       |


### 4、框架目录结构:
```
flycloud
├─db       -- 系统sql
├─doc      -- 系统文档
├─flycloud-api         -- 内网接口
│  ├─flycloud-api-auth -- 授权对内接口
│  ├─flycloud_mall     -- 商城对内接口
│  ├─flycloud_system   -- 系统对内接口
├─flycloud-auth        -- 授权校验模块
├─flycloud-common      -- 公共模块
│  ├─flycloud-common-core       -- 公共模块核心（公共中的公共代码）
│  ├─flycloud-common-database   -- 数据库连接相关公共代码
│  └─flycloud-common-doc        -- springdoc(swagger)相关公共代码
│  └─flycloud-common-redis      -- redis相关公共代码
│  └─flycloud-common-seata      -- seata(分布式事务)相关公共代码
│  └─flycloud-common-security   -- 安全相关公共代码
│  ├─flycloud-common-rocketmq   -- rocketmq相关公共代码
├─flycloud-extend    -- 扩展模块 (如 xxl-job-admin、springboot-admin等)
│  ├─flycloud-file-admin        -- 文件管理后台服务
│  ├─flycloud-xxl-job-admin     -- 任务调度服务
├─flycloud-gateway   -- 网关服务
├─flycloud-generator -- 自动生成代码服务
└─flycloud-mall      -- 商城服务
└─flycloud-music     -- 音乐服务
└─flycloud-system    -- 系统服务
└─flycloud-test      -- 测试服务 (测试各种服务代码或中间件)
└─flycloud-ui        -- 系统的ui前端模块
│  ├─flycloud-file-admin-ui           -- 文件管理后台ui
└─flycloud-user      -- 用户服务 
└─logs     -- 系统日志 
```


### 5、后端框架服务:
| 服务                                | 地址                    |
|-----------------------------------|-----------------------|
| 优先启动:                             |
| flycloud-gateway     网关服务         | http://127.0.0.1:8080 |
| flycloud-auth        授权校验服务       | http://127.0.0.1:8088 |
| 系统业务服务:                           |
| flycloud-mall        商城服务         | http://127.0.0.1:8081 |
| flycloud-music       音乐服务         | http://127.0.0.1:8083 |
| flycloud-system      系统服务         | http://127.0.0.1:8085 |
| flycloud-user        用户服务         | http://127.0.0.1:8082 |
| 其他服务:                             |
| flycloud-generator   自动生成代码服务     | http://127.0.0.1:8089 |
| flycloud-test        测试服务         | http://127.0.0.1:8099 |
| 扩展服务:                             |
| flycloud-file-admin   文件管理后台      | http://127.0.0.1:9095 |
| flycloud-xxl-job-admin     任务调度服务 | http://127.0.0.1:9091 |


### 6、前端服务:
| 服务                                        | 地址                    |
|-------------------------------------------|-----------------------|
| 系统业务服务:                                   |
| flycloud-mall-ui (待续)       商城(后台端/商家端)ui | http://127.0.0.1:xxx  |
| flycloud-music-ui (待续)      音乐ui              | http://127.0.0.1:xxx  |
| flycloud-system-ui (待续)     系统后台ui            | http://127.0.0.1:xxx  |
| 扩展服务:                                     |
| flycloud-file-admin-ui   文件管理后台ui         | http://127.0.0.1:7075 |


### 7、Swagger文档说明:
-  本系统使用的是 Spring doc + swagger3.0
>   优点: 1. 由于Springfox 已停止维护, 且随着springboot升级bug众多; 且需要编写大量的注解来支持文档生成
>      <br>  2. 支持spring boot, spring cloud, spring gateway 等
>      <br>  3. 最重要是支持 javadoc (java注释), 由于无需写注解基于java注释可实现零入侵
-  使用方法1 (使用自带ui工具):
>    本系统文档的访问路径：`域名+网关端口+/swagger-ui.html`，如: http://localhost:8080/swagger-ui.html
-  使用方法2 (使用第三方工具):
>   由于框架采用openapi行业规范，如需使用第三方文档工具 如 apifox, 则数据源的url是: `域名+网关端口+/服务名/v3/api-docs/`, 如: http://localhost:8080/flycloud-system/v3/api-docs/
> | ![输入图片说明](https://github.com/15521142480/flycloud/blob/7996bf17103b53774015b516e2c55edf13ee3cee/doc/swagger/img.png "屏幕截图") |


### 8、generator自动生成代码说明:
>   有两种生成方式两种:
>   <br> 第一种: 通过后台管理生成
>   <br> 第二种: 直接访问接口生成: http://ip:网关端口/flycloud-generator/gen/generatorCode?tables=sys_user  (多个用,隔开; 默认生成的文件在当前根目录下, 具体看生成时的控制台日志信息)


### 9、系统日志说明:
>   使用springboot自带的Logback:
>   <br> 在服务模块的resource层新增logback-spring.xml文件,格式配置根据需求自行修改


### 10、实体模型(domain)说明:
>   BO -> 由于此系统采用的分布式微服务架构, 也就每个服务相对独立, 且都是服务之间的调用(网关), 所以DTO的概念换成了BO
>   <br> VO -> 客户端(页面)展示的数据, 通常以json存在的形式


### 11、es索引引擎说明:
>   elasticsearch 版本为: 7.17.7
>   elasticsearch 客户端框架为: easy-es; 零成本上手(简单 易用 方便)
