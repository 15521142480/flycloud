#!/usr/bin/env bash

set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=common.sh
source "$SCRIPT_DIR/common.sh"

require_deployment_tools
ensure_project_layout
acquire_deployment_lock
validate_compose_config

# stop 保留容器、网络、镜像和宿主机日志，后续可通过 start.sh 原样启动。
log "停止服务容器（不会删除容器、镜像或日志）"
compose stop --timeout "$STOP_TIMEOUT_SECONDS"

show_status
log "停止完成"
