# flycloud-common-report

报表框架公共模块。每一种报表框架都必须使用独立的包、自动配置和依赖声明，避免业务模块与框架实现耦合。

当前实现：

- `jmreport`：积木报表和积木仪表盘，随 `flycloud-system` 一同启动。
- GoView：由 `flycloud-system` 提供项目与数据接口；其设计器是独立前端服务，地址通过管理端的 `VITE_GOVIEW_URL` 配置。

## 数据库初始化

报表模块所需的全部表已整理为 MySQL 8.4.10 脚本。新环境或已有库升级时执行：

```bash
mysql -h 127.0.0.1 -u root -p fly-cloud < db/mysql/升级脚本/V20260712__report_mysql8.sql
```

脚本包含 `jimu_*`、`onl_drag_*`、`report_go_view_project` 表，以及报表菜单和角色授权。
积木报表部分基于官方 `v2.3.4` 初始化脚本整理，移除了官方演示数据、删表语句和 MySQL 8.4 已弃用的整数显示宽度与 `ZEROFILL`。
