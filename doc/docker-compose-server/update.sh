#!/usr/bin/env bash

set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=common.sh
source "$SCRIPT_DIR/common.sh"

if [[ $# -ne 1 ]]; then
  printf '用法：%s <服务名>\n' "$0" >&2
  printf '例如：%s flycloud-ai\n' "$0" >&2
  printf '可用服务：%s\n' "${SERVICES[*]}" >&2
  exit 1
fi

SERVICE="$1"
is_known_service "$SERVICE" || die "不允许更新未知服务：${SERVICE}；可用服务：${SERVICES[*]}"

require_deployment_tools
ensure_project_layout
acquire_deployment_lock
validate_compose_config
validate_service_artifact "$SERVICE"
prepare_log_directories "$SERVICE"

build_args=()
if [[ "${PULL_BASE_IMAGES:-false}" == "true" ]]; then
  build_args+=(--pull)
fi

# 先构建新镜像，构建失败不会影响当前正在运行的旧容器。
log "开始构建服务镜像：$SERVICE"
compose build "${build_args[@]}" "$SERVICE"

# 不联动重启依赖服务；强制使用刚构建的镜像重建目标容器。
log "开始更新服务容器：$SERVICE"
compose up -d --no-deps --force-recreate "$SERVICE"

show_status
log "服务更新完成：$SERVICE"
