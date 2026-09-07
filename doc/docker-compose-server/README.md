# FlyCloud Docker Compose 部署说明

本目录用于在单台 Linux 服务器上部署 FlyCloud 后端服务，包含网关、认证、系统、工作流、商城和 AI 服务。

## 1. 目录结构

生产服务器建议保持以下结构：

```text
/project/flycloud-service/
├── deploy.sh
├── start.sh
├── stop.sh
├── update.sh
├── common.sh
├── docker-compose-server.yml
├── .env
├── flycloud-gateway/
│   ├── Dockerfile
│   ├── flycloud-gateway.jar
│   └── logs/
├── flycloud-auth/
│   ├── ...
├── flycloud-system/
│   ├── ...
├── flycloud-bpm/
│   ├── ...
├── flycloud-mall/
│   ├── ...
└── flycloud-ai/
│   ├── ...
```

每个服务目录都必须包含对应的 `Dockerfile`、同名 JAR 文件和持久化日志目录。

## 2. 首次部署

服务器需要安装 Docker Engine 与 Docker Compose v2，并确保当前用户能够执行 Docker 命令。

```bash
cd /project/flycloud-service
chmod 600 .env
chmod +x deploy.sh start.sh stop.sh update.sh
```

编辑 `.env`，填写生产环境需要的配置。AI 服务只需填写实际使用的模型供应商密钥，真实密钥禁止提交到 Git。

完成配置后执行：

```bash
./deploy.sh
```

部署脚本会依次完成环境校验、JAR/Dockerfile 校验、日志目录准备、全部镜像构建和容器启动。镜像全部构建成功后才会更新容器，避免构建失败时提前停止现有服务。

## 3. 日常操作

更新单个服务，例如 AI 服务：

```bash
./update.sh flycloud-ai
```

停止或重新启动已有容器：

```bash
./stop.sh
./start.sh
```

`start.sh` 只启动已经存在的容器，不负责首次创建或重新构建。首次部署及全量更新使用 `deploy.sh`。

## 4. 日志不会被部署脚本删除

`mkdir -p` 是幂等操作：目录不存在时创建，目录已存在时直接成功返回，既不会先删除目录，也不会清空已有文件。因此以下操作不会删除历史日志：

```bash
mkdir -p /project/flycloud-service/flycloud-ai/logs
```

当前脚本还做了以下保护：

- 明确检查日志路径；如果路径存在但不是目录，部署立即终止。
- 已存在的日志目录直接复用，并输出“不删除已有日志”的提示。
- 默认只修正日志目录本身的权限，不递归修改历史日志，避免每次部署都扫描大量文件。
- 只有明确设置 `REPAIR_LOG_OWNERSHIP=true` 时，才递归修复日志文件属主；该操作仍不会删除日志。
- `stop.sh` 使用 `docker compose stop`，只停止容器，不删除容器、镜像、网络或日志。
- 应用日志通过 bind mount 保存在宿主机的 `<服务名>/logs`，重建容器后仍然存在。

应用文件日志和 Docker 标准输出日志是两套日志。Compose 已为 Docker 的 `json-file` 日志设置轮转，默认单文件 20 MB、最多 5 个文件，避免占满磁盘；应用日志继续按照项目中的 Logback 配置轮转。

## 5. flycloud-ai 部署注意事项

`flycloud-ai` 已纳入全量部署和单服务更新流程，默认端口为 `8086`，日志保存在：

```text
/project/flycloud-service/flycloud-ai/logs
```

模型密钥从服务器的 `.env` 注入容器。Qdrant、Nacos 等服务地址继续由 Nacos 配置中心管理。需要特别注意：容器里的 `127.0.0.1` 或 `localhost` 指向容器自身；如果 Qdrant 运行在宿主机或另一个容器中，应配置为容器可访问的宿主机地址、域名或同一 Docker 网络内的服务名。

## 6. 脚本安全机制

- 使用严格模式，未定义变量、命令失败或管道失败会立即终止。
- 使用部署锁，防止两个部署或更新任务同时修改同一组容器。
- `update.sh` 只允许更新 Compose 中已声明的服务，避免服务名拼写错误或参数注入。
- 更新单个服务时先构建新镜像，再重建目标容器；构建失败不会影响当前运行中的容器。
- 容器以非 root 用户运行，并启用 `no-new-privileges`。
- 容器收到停止信号后有默认 30 秒的优雅退出时间。

## 7. 生产环境进一步建议

当前方案适合单机 Docker Compose 部署。若按照更严格的企业生产规范继续演进，建议按优先级补充：

1. 为每个服务接入 Spring Boot Actuator，并在 Compose 中配置健康检查和依赖就绪判断。
2. 使用私有镜像仓库和不可变版本号（提交号或发布号），保留上一个可用镜像，实现快速回滚；不要长期只使用 `latest`。
3. 根据压测结果设置 CPU、内存和 JVM 上限，并接入主机、容器、JVM、接口与磁盘告警。
4. 生产环境只向公网暴露网关端口；其他服务端口应通过防火墙、回环地址或内部网络限制访问。修改前要先确认 Nacos、运维探针及内部调用拓扑。
5. 定期备份 Nacos 配置、数据库、对象存储和宿主机日志；部署脚本本身不能替代备份与灾难恢复。

当前未直接加入通用健康检查和资源上限，是因为各服务的 Actuator 暴露策略与实际服务器容量尚不一致。应先统一健康检查端点并完成容量评估，再启用这些限制，避免把正常容器误判为故障或引发 OOM。
