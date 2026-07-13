# 本地 Nacos 配置

此目录存放本机环境的 Nacos 连接信息

Maven Profile 与文件对应关系：

| Maven Profile | 本地文件 |
| --- | --- |
| `dev` | `nacos-dev.properties` |
| `test` | `nacos-test.properties` |
| `prod` | `nacos-prod.properties` |

每个文件均需要包含以下配置项：

```properties
nacos.server=
nacos.username=
nacos.password=
nacos.namespace=
nacos.discovery.group=DEFAULT_GROUP
nacos.config.group=DEFAULT_GROUP
```

执行 `mvn -Pdev package`、`mvn -Ptest package` 或 `mvn -Pprod package` 时，Maven 会将相应文件的值写入各服务构建产物中的 `application.yml`。
