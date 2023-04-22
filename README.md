
### 系统前言:
>   flycloud-server 是一个微服务分布式架构系统, 主要做商城和音乐的一个业务管理系统.


### 目前集成了:
>   SpringCloudAlibaba + Nacos、
>   Mybatis-plus + Velocity、
>   SpringDoc + Swagger3.0、


### generator自动生成代码说明
>   有两种生成方式两种:
>   第一种: 通过后台管理生产
>   第二种: 直接访问接口生成: http://ip:网关端口/flycloud-generator/gen/generatorCode?tables=sys_user  (多个用,隔开; 默认生成的文件在当前根目录下, 具体看生成时的控制台日志信息)






### 实体模型(domain)说明:
>   BO -> 由于此系统采用的分布式微服务架构, 也就每个服务相对独立, 且都是服务之间的调用(网关), 所以DTO的概念换成了BO
>   VO -> 客户端(页面)展示的数据, 通常以json存在的形式
