
### 1、项目说明:
- flycloud-server 是一款基于Spring Cloud Alibaba的微服务架构。
- 目前整合了 Spring Boot 2.7.0、 Spring Cloud 2021、Spring Cloud Alibaba 2021、Nacos2.X、Spring Security Oauth2、ElasticSearch、MyBatis Plus、Seata、Redis、Rabbitmq 等主流技术。
- 主要以商城项目为主，扩展项目为辅的一个飞翔云系统。


### 2、地址

### 👉 演示地址：http://39.98.125.88:7075  （飞翔云平台系统；业务功能开发中...）
| 账号        | 密码（使用后台登录用原值，接口调试登录用md5值）                            |
|-----------|------------------------------------------------------|
| admin     | md5值：0192023a7bbd73250516f069df18b500 原值：admin123    |
| fileadmin | md5值：a66abb5684c45962d887564f08346e8d 原值：admin123456 |


### 👉 项目地址：
| 后端                                        | 前端                                                                                |
|-------------------------------------------|-----------------------------------------------------------------------------------|
| https://github.com/15521142480/flycloud   | https://github.com/15521142480/flycloud/tree/master/flycloud-ui 即后端的flycloud-ui模块 |

### 分支说明：
 两个主服务分支版本（即auth集成用户查询）

| 分支                     | 说明                       | jdk               |
|------------------------|--------------------------|-------------------|
| master                 | 主分支                      | jdk8              |
| two_master_server_dev  | 两个主服务分支版本（即auth服务集成用户服务） | jdk8              |


### 3、系统集成了:

| 后端           | 框架                                                 |
|--------------|----------------------------------------------------|
| jdk          | jdk8                                               |
| 核心框架         | Spring Boot 2.6                                    |
| 微服务          | Spring cloud 2021 Alibaba、Spring cloud Alibaba2021 |
| 注册和配置中心      | Nacos 2.1.1                                        |
| 安全/授权框架      | Spring Security + OAuth2.0                         |
| 工作流框架        | Flowable 6.8.0                                     |
| 数据库/持久层/自动生成 | Mysql5.7 + Mybatis plus、 Velocity                  |
| 分布式事务        | Seata (待续)                                         |
| 缓存           | Ehcache、 Redis                                     |
| 搜索引擎         | ElasticSearch (待续)                                 |
| 消息队列框架       | RocketMQ (待续)                                      |
| 文档框架         | SpringDoc + Swagger3.0                             |
| 日志框架框架       | Logback                                            |


| 前端      | 框架                                                 |
|---------|----------------------------------------------------|
| 项目框架    | vue2.0                                             |
| ui框架    | Element、iview、vant                                 |


### 4、框架目录结构:
```
flycloud
├─db       -- 系统sql
├─doc      -- 系统文档
├─flycloud-api              -- 内网接口（实体和feign等api层）
│  ├─flycloud_bpm_api                   -- 工作流api
│  ├─flycloud_system_api                -- 系统api
├─flycloud-auth             -- 授权服务
├─flycloud-bpm              -- 工作流服务
├─flycloud-common           -- 公共模块
│  ├─flycloud-common-core               -- 公共模块核心代码
│  ├─flycloud-common-database           -- 数据库连接相关
│  └─flycloud-common-doc                -- springdoc(swagger)相关
│  └─[flycloud-common-elasticsearch     -- es相关
│  └─flycloud-common-feign              -- feign相关
│  └─flycloud-common-redis              -- redis相关
│  └─flycloud-common-seata              -- seata(分布式事务)相关
│  └─flycloud-common-security           -- 安全相关
│  ├─flycloud-common-rocketmq           -- rocketmq相关
├─flycloud-extend           -- 扩展模块 (如 xxl-job-admin、springboot-admin等)
│  ├─flycloud-file-admin                -- 文件管理后台服务
│  ├─flycloud-xxl-job-admin             -- 任务调度服务
├─flycloud-gateway          -- 网关服务
├─flycloud-generator        -- 自动生成代码服务
└─flycloud-mall             -- 商家服务
└─flycloud-music            -- 音乐服务
└─flycloud-system           -- 平台服务
└─flycloud-test             -- 测试服务 (测试各种服务代码或中间件)
└─flycloud-ui               -- 系统的ui前端模块
│  ├─flycloud-admin-ui                  -- 文件管理后台ui (新, vue3)
│  ├─flycloud-file-admin-ui             -- 文件管理后台ui
│  ├─flycloud-platform-admin-ui         -- 平台管理后台ui (旧, vue2)
└─logs     -- 系统日志 
```


### 5、后端服务:
| 服务                                | 地址                    |
|-----------------------------------|-----------------------|
| 优先启动（必须）:                         |
| flycloud-gateway     网关服务         | http://127.0.0.1:8080 |
| flycloud-auth        授权校验服务       | http://127.0.0.1:8088 |
| flycloud-system      系统服务         | http://127.0.0.1:8085 |
| 系统业务服务:                           |
| flycloud-mall        商城服务         | http://127.0.0.1:8081 |
| flycloud-music       音乐服务         | http://127.0.0.1:8083 |
| 其他服务:                             |
| flycloud-generator   自动生成代码服务     | http://127.0.0.1:8089 |
| flycloud-test        测试服务         | http://127.0.0.1:8099 |
| 扩展服务:                             |
| flycloud-file-admin   文件管理后台      | http://127.0.0.1:9095 |
| flycloud-xxl-job-admin     任务调度服务 | http://127.0.0.1:9091 |


### 6、前端服务:（可选）
| 服务                                        | 地址                    |
|-------------------------------------------|-----------------------|
| flycloud-platform-admin-ui   平台ui         | http://127.0.0.1:7075 |
| flycloud-mall-ui (待续)       商城(后台端/商家端)ui | http://127.0.0.1:xxx  |
| flycloud-music-ui (待续)      音乐ui          | http://127.0.0.1:xxx  |
| flycloud-system-ui (待续)     系统后台ui        | http://127.0.0.1:xxx  |
| flycloud-file-admin-ui   文件管理后台ui         | http://127.0.0.1:xxx  |


### 7、Swagger文档说明:
-  本系统使用的是 Spring doc
>  由于框架采用openapi行业规范，如需使用第三方文档工具 如 apifox, 则数据源的url是: `域名+网关端口+/服务名/v3/api-docs/`, 如: http://localhost:8080/flycloud-system/v3/api-docs/
> | ![输入图片说明](https://github.com/15521142480/flycloud/blob/7996bf17103b53774015b516e2c55edf13ee3cee/doc/swagger/img.png "屏幕截图") |


### 8、generator自动生成代码说明:
>   有两种生成方式两种:
>   <br> 第一种: 通过后台管理生成
>   <br> 第二种: 直接访问接口生成: http://ip:网关端口/flycloud-generator/gen/generatorCode?tables=sys_user  (多个用,隔开; 默认生成的文件在当前根目录下, 具体看生成时的控制台日志信息)


### 9、实体模型(domain)说明:
>   BO -> 由于此系统采用的分布式微服务架构, 也就每个服务相对独立, 且都是服务之间的调用(网关), 所以DTO的概念换成了BO
>   <br> VO -> 客户端(页面)展示的数据, 通常以json存在的形式


### 10、es索引引擎说明:
>   elasticsearch 版本为: 7.17.7
>   elasticsearch 客户端框架为: easy-es; 零成本上手(简单 易用 方便)
