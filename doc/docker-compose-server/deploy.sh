#!/usr/bin/env bash

set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=common.sh
source "$SCRIPT_DIR/common.sh"

require_deployment_tools
ensure_project_layout
acquire_deployment_lock

log "开始校验 Compose 配置和服务构建文件"
validate_compose_config
validate_all_service_artifacts

log "准备日志目录"
prepare_log_directories "${SERVICES[@]}"

# 先完成全部镜像构建；任何服务构建失败时，不会重建正在运行的容器。
build_args=()
if [[ "${PULL_BASE_IMAGES:-false}" == "true" ]]; then
  build_args+=(--pull)
fi

log "开始构建全部服务镜像"
compose build "${build_args[@]}"

log "开始创建或更新服务容器"
compose up -d --remove-orphans

show_status
log "部署完成"
