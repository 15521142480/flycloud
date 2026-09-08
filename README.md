# FlyCloud（飞翔云）

FlyCloud 是一套基于 Spring Cloud Alibaba 的前后端分离微服务平台，覆盖系统权限、AI 助手、工作流、商城、支付、会员、即时通讯、报表、代码生成和任务调度等场景。
项目采用 JDK 21、Spring Boot 3.5、Vue 3 和 uni-app，可作为微服务学习项目，也可作为企业后台与业务系统的二次开发基础。

> 当前主线已接入 `flycloud-ai`：基于 Spring AI 提供多模型对话、流式输出、Chat Memory、Tool Calling、Embedding、Qdrant、RAG、Agent 和 MCP 等能力。

> 目前整合了
> <br>
> JDK 21、Spring Boot 3.5.3、Spring Cloud 2025.0.3、Spring Cloud Alibaba 2025、Nacos 3、Spring Security、JWT、OpenFeign；
> <br>
> Spring AI 1.1.5、OpenAI / DeepSeek / 阿里云百炼（通义千问）、Qdrant 1.13.4、RAG、Tool Calling、Agent、MCP；
> <br>
> Mysql 8.4 + MyBatis-Plus 3.5.7 + Seata 2.5、Redis + ElasticSearch 8 + RocketMQ 5.3.3；
> <br>
> Flowable7.1.0 + BPMN 2.0 + bpmn.js、WebSocket + LiveKit、Velocity、Xxl-job；
> <br>
> Vue 3.5 + Element Plus 2.8、uni-app、TypeScript、Vite、ECharts 等主流技术。

- [1、在线体验与仓库地址](#1在线体验与仓库地址)
- [2、项目介绍](#2项目介绍)
- [3、技术栈](#3技术栈)
- [4、项目目录](#4项目目录)
- [5、快速开始](#5快速开始)
- [6、服务与端口](#6服务与端口)
- [7、系统基础功能](#7系统基础功能)
- [8、AI 助手](#8ai-助手)
- [9、工作流程](#9工作流程)
- [10、商城系统](#10商城系统)
- [11、工程与中间件能力](#11工程与中间件能力)
- [12、接口文档与开发约定](#12接口文档与开发约定)


## 1、在线体验与仓库地址

### 在线演示

### 👉 演示地址1（飞翔云管理系统）：<a href="https://www.laixueshi.cn" target="_blank" rel="noopener noreferrer">https://www.laixueshi.cn</a>
### 👉 演示地址2（飞翔云商城 H5）：<a href="https://www.laixueshi.cn/mall-app" target="_blank" rel="noopener noreferrer">https://www.laixueshi.cn/mall-app</a>
<br>
演示账号仅用于体验，请勿在演示环境中录入敏感信息。

| 账号          | 密码 | 说明 |
|-------------| --- | --- |
| `admin`     | `admin123` | 平台管理员 |
| `lxs`       | `123456` | 平台管理员 |

### 项目仓库

| 内容 | 地址 |
| --- | --- |
| 后端与完整工程 | [https://github.com/15521142480/flycloud](https://github.com/15521142480/flycloud) |
| 前端工程 | [`flycloud-ui`](flycloud-ui)（与后端位于同一仓库） |

### 分支说明

| 分支 | 说明 | JDK | 前端 |
| --- | --- | --- | --- |
| `main` | 主分支，使用当前技术栈 | JDK 21 | Vue 3 |
| `dev/main` | 日常开发分支 | JDK 21 | Vue 3 |
| `jdk8` | JDK 8 稳定归档版本 | JDK 8 | Vue 2 + Vue 3 |
| `jdk8_two_server` | 面向低配置环境的双服务版本，认证服务集成用户服务 | JDK 8 | Vue 2 + Vue 3 |

新项目建议使用 `main`；需要维护旧环境时再选择归档分支。

## 2、项目介绍

FlyCloud 以网关为统一入口，通过 Nacos 完成服务注册与配置管理，并将通用能力拆分为独立公共模块。开发者可以只启动网关、认证和系统服务体验基础后台，也可以按需加入 AI、工作流、商城和扩展服务。

| 业务域 | 主要能力 |
| --- | --- |
| 系统平台 | 用户、角色、菜单、部门、岗位、数据权限、字典、配置、日志、租户和 OAuth2 |
| AI 助手 | 多模型对话、SSE 流式输出、会话记忆、业务工具调用、RAG、Agent、MCP |
| 工作流程 | BPMN 可视化建模、动态表单、会签/或签、加减签、转办/委派、超时与提醒 |
| 商城系统 | 商品、SKU、订单、售后、配送、营销、分销、会员、支付、统计和页面装修 |
| 即时通讯 | 好友、群组、私聊/群聊、消息通知、WebSocket 实时通信和 RTC 通话 |
| 工程能力 | 代码生成、接口文档、报表、文件管理、分布式事务、消息队列、搜索和任务调度 |

```mermaid
flowchart LR
    A[Vue 3 管理端] --> G[flycloud-gateway]
    B[uni-app 商城端] --> G
    G --> AU[flycloud-auth]
    G --> S[flycloud-system]
    G --> AI[flycloud-ai]
    G --> BPM[flycloud-bpm]
    G --> M[flycloud-mall]
    G --> GEN[flycloud-generator]
    AU --> N[Nacos]
    S --> N
    AI --> N
    BPM --> N
    M --> N
    GEN --> N
    S --> DB[(MySQL / Redis)]
    AI --> DB
    BPM --> DB
    M --> DB
    AI --> Q[(Qdrant)]
    S --> ES[(Elasticsearch / RocketMQ)]
```


## 3、技术栈

以下版本以当前主工程和部署文件为准。

### 后端与基础设施

| 分类 | 技术或版本 |
| --- | --- |
| 基础环境 | JDK 21、Maven、MySQL 8.4 |
| 核心框架 | Spring Boot 3.5.3、Spring Cloud 2025.0.3、Spring Cloud Alibaba 2025.0.0.0 |
| 注册与配置中心 | Nacos 3.0.3 |
| AI | Spring AI 1.1.5、OpenAI / DeepSeek / 阿里云百炼（通义千问）、Reactor、SSE |
| AI 知识库与协议 | Embedding、Qdrant 1.13.4、RAG、Tool Calling、Agent、MCP Streamable HTTP |
| 安全与服务调用 | Spring Security、JWT、OpenFeign、Spring Cloud LoadBalancer |
| 数据访问 | MyBatis-Plus 3.5.7、Dynamic DataSource、Druid、MapStruct |
| 工作流 | Flowable 7.1.0、BPMN 2.0、bpmn.js 8.9.0 |
| 缓存 | Redis、Redisson、JetCache |
| 分布式事务 | Seata 2.5.0 |
| 消息队列 | RocketMQ 5.3.3 |
| 搜索引擎 | Spring Data Elasticsearch、Elasticsearch 8.18.3 + IK |
| 实时通信与文档 | WebSocket、LiveKit、OnlyOffice |
| 接口文档 | SpringDoc 2.8.17、Knife4j 4.5.0、OpenAPI 3 |
| 调度与报表 | XXL-JOB 3.2.0、积木报表 JimuReport 2.3.4 |

### 前端

| 工程 | 技术栈 |
| --- | --- |
| 管理后台 | Vue 3.5、TypeScript 5.3、Vite 5.4、Element Plus 2.8、Pinia、Vue Router、ECharts、UnoCSS |
| 流程设计器 | bpmn.js、diagram-js、form-create |
| AI 交互 | Fetch + SSE、Markdown 渲染、会话式工作区 |
| 商城移动端 | uni-app，可发布 H5、微信小程序、iOS 和 Android |

## 4、项目目录

```text
flycloud
├─config       -- 系统配置
├─db           -- 系统sql
├─doc          -- 系统文档
├─ext-lib      -- 外部jai包
├─flycloud-ai               -- ai服务
├─flycloud-api              -- 微服务内部 API、实体与 Feign 契约
│  ├─flycloud_bpm_api                   -- 工作流api
│  ├─flycloud_system_api                -- 系统api
│  ├─flycloud_mall_api                  -- 商城api
├─flycloud-auth             -- 授权服务
├─flycloud-bpm              -- 工作流服务
├─flycloud-common           -- 公共模块
│  ├─flycloud-common-core               -- core/公共模块核心代码
│  ├─flycloud-common-database           -- database/数据库连接
│  └─flycloud-common-doc                -- doc/接口文档
│  └─[flycloud-common-elasticsearch     -- es/搜索引擎
│  └─flycloud-common-feign              -- feign/服务接口调用
│  └─flycloud-common-onlineoffice       -- onlineoffice/在线文档
│  └─flycloud-common-redis              -- redis/缓存
│  └─flycloud-common-report             -- report/报表
│  ├─flycloud-common-rocketmq           -- rocketmq/通讯队列
│  └─flycloud-common-seata              -- seata/分布式事务
│  └─flycloud-common-security           -- security/微服务之间的授权验证
│  └─flycloud-common-websocket          -- websocket/信息通讯
│  └─flycloud-common-xxljob             -- xxljob/任务调度
├─flycloud-extend           -- 可独立部署的扩展服务 (如 xxl-job-admin、springboot-admin等)
│  ├─flycloud-file-admin                -- 文件管理后台服务
│  ├─flycloud-xxljob-admin              -- 任务调度服务
├─flycloud-gateway          -- 网关服务
├─flycloud-generator        -- 自动生成代码服务
└─flycloud-mall             -- 商家服务
└─flycloud-system           -- 平台服务
└─flycloud-test             -- 测试服务
└─flycloud-ui               -- 系统前端ui模块
│  ├─flycloud-admin-ui                  -- 管理后台ui
│  ├─flycloud-mall-app-ui               -- 商城移动端（兼容h5/小程序等）
└─logs        -- 系统日志
```

## 5、快速开始

### 5.1 环境准备

基础后台至少需要：

- JDK 21、Maven 3.9+
- MySQL 8.x、Redis
- Nacos 3.x
- Node.js 16+、pnpm 8.6+

使用对应功能时，再启动 Qdrant、Elasticsearch、RocketMQ、Seata、XXL-JOB、OnlyOffice 等组件。项目提供的部署示例位于 [`doc/docker-compose`](doc/docker-compose)。

### 5.2 初始化数据库

1. 新环境先导入 [`db/mysql/表结构/fly-cloud表结构.sql`](db/mysql/表结构/fly-cloud表结构.sql)。
2. 按需导入 [`db/mysql/表数据/fly-cloud表数据`](db/mysql/表数据/fly-cloud表数据) 下的基础数据。
3. 已有数据库按文件名顺序执行 [`db/mysql/升级脚本`](db/mysql/升级脚本)；AI 会话、知识库和文档分块表由 `V20260828__ai_chat_memory.sql` 提供。
4. 使用 Seata 时，额外初始化 Seata Server 和客户端 `undo_log` 表。

执行 SQL 前请先备份现有数据库，并根据目标环境确认库名和字符集。

### 5.3 配置 Nacos

1. 将 [`config/nacos/nacos-dev.example.properties`](config/nacos/nacos-dev.example.properties) 复制为 `config/nacos/nacos-dev.properties`，填写本地 Nacos 地址、账号、命名空间和分组。
2. 参考 [`doc/nacos-example`](doc/nacos-example) 中的脱敏示例，在 Nacos 导入 `application-common.yaml`、`application-datasource.yaml` 和需要启动的 `flycloud-*-dev.yaml`。
3. `dev`、`test`、`prod` Profile 与本地文件的对应关系见 [`config/nacos/README.md`](config/nacos/README.md)。

数据库密码、AI 模型密钥等敏感配置应通过环境变量、Nacos 加密配置或密钥管理服务注入，不要提交到 Git。例如启用百炼模型时：

```bash
export DASHSCOPE_API_KEY="your-api-key"
```

### 5.4 启动基础设施与后端

AI 的向量检索使用 Qdrant，可通过示例编排启动：

```bash
docker compose -f doc/docker-compose/docker-compose-qdrant.yml up -d
```

构建后端：

```bash
mvn clean package -Pdev -DskipTests
```

推荐启动顺序：

1. MySQL、Redis、Nacos，以及本次功能依赖的其他中间件。
2. `flycloud-gateway`、`flycloud-auth`、`flycloud-system`。
3. 按需启动 `flycloud-ai`、`flycloud-bpm`、`flycloud-mall`、`flycloud-generator` 和扩展服务。

### 5.5 启动前端

管理后台：

```bash
cd flycloud-ui/flycloud-admin-ui
pnpm install
pnpm dev
```

商城移动端使用 HBuilderX 打开 `flycloud-ui/flycloud-mall-app-ui`，选择运行到 H5、微信小程序或 App；接口地址和端口配置位于该工程的 `.env`。

## 6、服务与端口

### 后端服务

| 启动级别 | 服务 | 职责 | 默认地址 |
| --- | --- | --- | --- |
| 必需 | `flycloud-gateway` | 统一入口与路由 | `http://127.0.0.1:8080` |
| 必需 | `flycloud-auth` | 登录、令牌和授权 | `http://127.0.0.1:8088` |
| 必需 | `flycloud-system` | 系统、会员、支付、IM 等核心能力 | `http://127.0.0.1:8085` |
| 按需 | `flycloud-ai` | AI 助手与 AI 实验室 | `http://127.0.0.1:8086` |
| 按需 | `flycloud-bpm` | 工作流 | `http://127.0.0.1:8090` |
| 按需 | `flycloud-mall` | 商城 | `http://127.0.0.1:8081` |
| 按需 | `flycloud-generator` | 代码生成 | `http://127.0.0.1:8089` |
| 按需 | `flycloud-test` | 测试与示例 | `http://127.0.0.1:8099` |
| 扩展 | `flycloud-file-admin` | 文件管理后台 | `http://127.0.0.1:9095` |
| 扩展 | `flycloud-xxljob-admin` | 任务调度中心 | `http://127.0.0.1:9091/xxl-job-admin` |

### 前端服务

| 工程 | 默认地址 |
| --- | --- |
| `flycloud-admin-ui` | `http://127.0.0.1:7075` |
| `flycloud-mall-app-ui`（H5） | `http://127.0.0.1:3000/mall-app` |

业务访问建议统一经过网关；各服务端口主要用于本地调试和单服务接口文档。

## 7、系统基础功能

| 功能 | 说明                                                        |
| --- |-----------------------------------------------------------|
| 登录与认证 | 图文点选验证码、密码端到端加密、JWT 登录态和 OAuth2/第三方登录扩展                   |
| 首页 | 汇总业务数据、流程待办、通知和常用入口                                       |
| 用户管理 | 管理后台用户、状态、部门、岗位和角色关系                                      |
| 角色与权限 | 分配菜单权限、按钮权限和数据权限，支持权限可视化配置；<br> 其中菜单权限采用自研的新型设计思路来实现权限的可视化 |
| 菜单管理 | 动态配置路由、组件、菜单层级、图标、显示状态与按钮标识                               |
| 组织管理 | 部门树、岗位和基于组织结构的数据范围控制                                      |
| 系统配置 | 字典、参数、通知公告                                         |
| 审计运维 | 登录日志、操作日志、API 访问日志、错误日志和在线用户                              |
| 会员与支付 | 会员等级/标签/积分、地址、支付应用、渠道、订单、退款和转账                            |
| 即时通讯 | 好友和群组关系、私聊/群聊、丰富消息类型、WebSocket 通知与 RTC 通话                 |

### 登录安全与角色权限

| 图文点选验证码 | 角色菜单权限 |
| --- | --- |
| ![图文点选验证码](doc/img/ImageTextClickCaptcha.png) | ![角色菜单权限](doc/img/roleMenu-1.png) |

## 8、AI 助手

`flycloud-ai` 是独立的 AI 微服务，基于 Spring AI 1.1.5 实现。管理端提供两个入口：

- **AI 助手**：面向实际使用的统一会话入口，自动组合记忆、知识检索和受控业务工具。
- **AI 实验室过程**：按 9 个阶段拆解 AI 能力，便于学习、调试和验证每一层能力。

### 8.1 技术组成

| 能力 | 实现 |
| --- | --- |
| 模型接入 | 支持 OpenAI、DeepSeek、阿里云百炼（通义千问），由配置选择当前供应商和模型 |
| 对话输出 | 同时支持普通响应和 SSE 流式响应，并返回模型与 Token 用量 |
| 会话记忆 | Redis 保存有时效的短期上下文，MySQL 保存完整会话和消息历史 |
| Tool Calling | 模型按需查询系统用户、商城订单与会员公共信息，通过 Feign 复用业务服务 |
| 权限保护 | 工具调用继承当前登录身份，只返回授权范围内的业务数据和非敏感字段 |
| Embedding / Vector Store | 使用 EmbeddingModel 生成向量，Qdrant 保存和检索知识片段 |
| RAG | 仅对知识规则类问题检索知识库，并通过相似度阈值过滤无关内容 |
| Agent | 将业务工具与 RAG 上下文编排为多步骤任务，例如“查询订单并判断退款条件” |
| MCP | 内置 MCP Server 与 Client，使用 Streamable HTTP，并转发当前请求认证信息 |
| 智能路由 | 普通问答、业务查询、知识问答和复合任务按意图启用不同能力，减少无效调用 |

### 8.2 AI 实验室学习路径

| 阶段 | 内容 |
| --- | --- |
| 1 | 原生 HTTP 调用模型 API |
| 2 | 使用 Spring AI 统一调用模型 |
| 3 | Tool Calling / 工具调用 |
| 4 | Chat Memory / 多轮对话 |
| 5 | Embedding / 文本向量化 |
| 6 | Qdrant Vector Store / 相似度检索 |
| 7 | RAG / 检索增强生成 |
| 8 | Agent / 业务工具与知识库编排 |
| 9 | MCP Client + MCP Server 协议调用 |

### 8.3 AI 功能截图

| 通用问答与多轮会话 | 受控系统用户查询 |
| --- | --- |
| ![AI 通用问答与会话记录](doc/img/ai/ai-1.png) | ![AI 受控查询系统用户](doc/img/ai/ai-2.png) |

| 订单查询与知识规则组合 | Qdrant 向量检索 |
| --- | --- |
| ![AI 查询订单并结合退款规则回答](doc/img/ai/ai-3.png) | ![AI Vector Store 相似度检索](doc/img/ai/ai-4.png) |

| RAG 知识问答 | Agent 多步骤任务 |
| --- | --- |
| ![AI RAG 退款规则问答](doc/img/ai/ai-5.png) | ![AI Agent 查询订单并判断退款条件](doc/img/ai/ai-6.png) |

| MCP 协议工具调用 |
| --- |
| ![AI MCP Client 与 Server 工具调用](doc/img/ai/ai-7.png) |

## 9、工作流程

工作流服务基于 Flowable 7.1.0 和 BPMN 2.0，管理端使用 bpmn.js 完成可视化流程设计。系统同时提供流程配置后台和审批中心，覆盖从建模到归档的完整闭环：

```text
流程分类与表单 → BPMN 建模 → 节点人员/权限/规则配置 → 发布 → 用户发起
→ 待办审批与协作 → 消息/超时处理 → 流程结束 → 实例、任务和审批记录归档
```

### 9.1 功能分区

| 分区 | 功能 | 说明 |
| --- | --- | --- |
| 流程配置 | 流程模型 | 可视化设计、修改、预览、导入导出和发布 BPMN 模型 |
| 流程配置 | 流程表单 | 支持业务表单和拖拽式动态表单，可配置 PC、平板和移动端展示 |
| 流程配置 | 分类与用户组 | 按 OA、采购、合同、订单等场景分类，并复用审批人员分组 |
| 流程配置 | 表达式与监听器 | 配置条件表达式和流程事件监听，用于通知、统计和业务状态回写 |
| 运行管理 | 流程实例 | 查看所有已发起流程的状态、表单、流程图和审批进度 |
| 运行管理 | 流程任务 | 查看任务处理人、处理结果、意见和流转轨迹 |
| 审批中心 | 发起流程 | 当前用户从业务表单或动态表单发起申请 |
| 审批中心 | 我的流程 | 查看本人发起或参与的流程 |
| 审批中心 | 待办/已办 | 集中处理当前待办，并追踪历史已办事项 |
| 审批中心 | 抄送我的 | 查看抄送给当前用户的流程与表单信息 |
| 示例 | 请假申请 | 演示业务表单提交后启动流程，可扩展到合同、采购和订单等业务 |

### 9.2 流程引擎能力

| 能力 | 说明 | 状态 |
| --- | --- | --- |
| BPMN 设计器 | 基于 BPMN 标准，可视化配置事件、审批节点、网关和子流程 | ✅ |
| 会签 | 同一节点多人同时审批，全部通过后进入下一节点 | ✅ |
| 或签 | 同一节点任一审批人处理后进入下一节点 | ✅ |
| 依次审批 | 多人按顺序接收并完成审批 | ✅ |
| 抄送 | 将流程结果通知抄送人，并对同一审批自动排重 | ✅ |
| 驳回 | 可退回发起人、上一节点或指定节点后重新审批 | ✅ |
| 转办 | 当前审批人将任务交给其他人继续处理 | ✅ |
| 委派 | 被委派人处理后回到原审批人确认，再进入下一节点 | ✅ |
| 加签/减签 | 在当前节点动态增加或减少审批人，支持前加签和后加签 | ✅ |
| 撤销/终止 | 发起人可撤销，管理员可在任意节点终止流程实例 | ✅ |
| 表单权限 | 每个审批节点可独立配置字段只读、可编辑或隐藏 | ✅ |
| 超时审批 | 超时后自动通过、不通过或驳回 | ✅ |
| 自动提醒 | 通过短信、邮件或站内信提醒，支持重复提醒频次 | ✅ |
| 父子流程 | 支持同步或异步子流程，并控制主流程后续执行 | ✅ |
| 条件/并行分支 | 支持排他条件决策和多分支并行执行 | ✅ |

### 9.3 工作流截图

| 流程模型 / BPMN 流程设计器                 | 自定义表单提交                 |
|-----------------------------------|----------------------------------|
| ![工作流流程模型](doc/img/bpm/bpm-1.png) | ![自定义表单提交](doc/img/bpm/bpm-2.png) |

| 节点1审批-委派                     | 委派者审批                           |
|------------------------------|----------------------------------|
| ![节点1审批委派](doc/img/bpm/bpm-3.png) | ![委派者审批](doc/img/bpm/bpm-4.png) |

| 节点1审批                           | 节点2审批                           |
|---------------------------------|---------------------------------|
| ![节点1审批](doc/img/bpm/bpm-5.png) | ![节点2审批](doc/img/bpm/bpm-6.png) |

| 节点3审批                           | 审批纪录                            |
|---------------------------------|---------------------------------|
| ![节点3审批](doc/img/bpm/bpm-7.png) | ![审批纪录](doc/img/bpm/bpm-8.png) |


## 10、商城系统

商城由 `flycloud-mall`、管理后台商城模块和 `flycloud-mall-app-ui` 组成，形成“后台配置商品与营销 → 用户浏览下单 → 支付配送 → 售后与统计”的业务闭环。

### 10.1 商城后台

| 模块 | 主要功能 |
| --- | --- |
| 商城首页 | 交易趋势、运营数据、会员数据和常用入口 |
| 商品中心 | SPU/SKU、分类、品牌、属性、库存、评论、收藏和浏览记录 |
| 订单中心 | 订单查询、价格/地址调整、发货、自提、备注、详情和操作日志 |
| 售后退款 | 售后申请、审核、退款、退货物流和售后日志 |
| 配送管理 | 快递公司、运费模板、自提门店和核销 |
| 营销中心 | 优惠券、满减、秒杀、拼团、砍价、积分、奖励和 Banner/文章 |
| 商城装修 | 页面与模板装修，拖拽配置导航、图文、商品和广告组件 |
| 分销管理 | 分销用户、佣金记录、提现和推广关系 |
| 客服与统计 | 客服会话，以及商品、交易、会员和支付统计 |

#### 商城后台截图

| 商品基础信息 | 多规格 SKU 与库存 |
| --- | --- |
| ![商城商品基础信息编辑](doc/img/mall/mall-1.png) | ![商城多规格 SKU 与库存管理](doc/img/mall/mall-2.png) |

| 订单管理 | 商城页面装修 |
| --- | --- |
| ![商城订单管理](doc/img/mall/mall-3.png) | ![商城可视化页面装修](doc/img/mall/mall-4.png) |

| 订单状态与支付信息 |
| --- |
| ![商城订单状态与支付信息](doc/img/mall/mall-5.png) |

### 10.2 商城移动端

移动端基于 uni-app，一套代码可面向 H5、微信小程序、iOS 和 Android 发布，主要包含商品搜索与详情、购物车、订单、支付、物流、售后、会员、积分、优惠券、分销和客服等功能。

| 商品详情 | 确认订单 |
| --- | --- |
| ![商城移动端商品详情](doc/img/mall/mall-app-1.png) | ![商城移动端确认订单](doc/img/mall/mall-app-2.png) |

| 收银台 | 会员中心 |
| --- | --- |
| ![商城移动端收银台](doc/img/mall/mall-app-3.png) | ![商城移动端会员中心](doc/img/mall/mall-app-4.png) |

| 订单详情与售后入口 |
| --- |
| ![商城移动端订单详情](doc/img/mall/mall-app-5.png) |

## 11、工程与中间件能力

### 11.1 代码生成

支持两种生成方式：

1. 在管理后台选择数据表并生成代码，生成结果位于后端部署根目录的 `src` 文件夹。
2. 通过网关接口生成：

```text
http://<网关地址>:8080/flycloud-generator/gen/generatorCode?tables=sys_user
```

多个表名使用英文逗号分隔，实际输出目录以生成服务控制台日志为准。

| 数据表配置 | 字段配置 | 生成结果 |
| --- | --- | --- |
| ![代码生成数据表配置](doc/img/gen-3.png) | ![代码生成字段配置](doc/img/gen-1.png) | ![代码生成结果](doc/img/gen-2.png) |

### 11.2 搜索与消息

- Elasticsearch 使用 Spring Data Elasticsearch；部署示例为 Elasticsearch 8.18.3 + IK 分词器。
- RocketMQ 用于异步消息，项目已封装生产者、消费者幂等和 Outbox 投递能力。
- Elasticsearch 与 RocketMQ 相关业务可通过配置开关启用，未启用时不影响基础系统运行。

### 11.3 分布式事务、调度与扩展

- Seata 提供跨服务分布式事务能力。
- XXL-JOB 提供任务调度中心和业务执行器封装。
- 文件管理、积木报表、OnlyOffice 和 WebSocket 均按公共模块或扩展服务接入。
- 后端服务的 Docker Compose 脚本、AI 配置和安全更新流程见 [Docker Compose 部署说明](doc/docker-compose-server/README.md)，其他中间件部署文件位于 [`doc/docker-compose`](doc/docker-compose)。

## 12、接口文档与开发约定

### 12.1 Swagger / OpenAPI

系统使用 SpringDoc + Knife4j：

- 网关聚合文档：`http://localhost:8080/doc.html`
- 单服务文档示例：`http://localhost:8086/doc.html`
- OpenAPI 数据源：`http://localhost:8080/<服务名>/v3/api-docs/`
- 系统服务示例：`http://localhost:8080/flycloud-system/v3/api-docs/`

Apifox 等第三方工具可直接导入对应服务的 OpenAPI 数据源。

| 聚合文档                                   | 单服务文档 | OpenAPI 数据源                                  |
|----------------------------------------| --- |----------------------------------------------|
| ![Swagger 聚合文档](doc/img/swagger-1.png) | ![Swagger 单服务文档](doc/img/swagger-2.png) | ![Swagger OpenAPI 数据源](doc/img/swagger-3.png) |

### 12.2 实体模型

| 类型 | 用途 |
| --- | --- |
| Entity / Domain | 与数据库表或领域对象对应，仅在服务内部使用 |
| BO | 服务内部或微服务调用时的业务入参对象；项目中承担传统 DTO 的主要职责 |
| VO | 返回给前端或调用方的展示对象，通常序列化为 JSON |

建议保持 Controller、Service、Mapper、BO/VO 职责清晰，不直接将数据库实体暴露给客户端；跨服务调用统一复用 `flycloud-api` 中的契约。
