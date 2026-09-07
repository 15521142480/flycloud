#!/usr/bin/env bash

set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=common.sh
source "$SCRIPT_DIR/common.sh"

require_deployment_tools
ensure_project_layout
acquire_deployment_lock
validate_compose_config

# start 只启动已经存在的容器，不构建镜像，也不重建容器。
log "启动已有服务容器"
compose start

show_status
log "启动完成"
