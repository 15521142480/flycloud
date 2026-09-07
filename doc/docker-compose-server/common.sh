#!/usr/bin/env bash

# 所有部署脚本共用的安全选项和辅助函数。
set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
INITIAL_PROJECT_DIR="${PROJECT_DIR:-$SCRIPT_DIR}"
ENV_FILE="${ENV_FILE:-$INITIAL_PROJECT_DIR/.env}"

# 只读取允许的单个配置值，不直接 source .env，避免其中的特殊字符被当作 shell 命令执行。
dotenv_value() {
  local requested_key="$1"
  local default_value="$2"
  local line key value

  [[ -f "$ENV_FILE" ]] || {
    printf '%s' "$default_value"
    return
  }

  while IFS= read -r line || [[ -n "$line" ]]; do
    line="${line#"${line%%[![:space:]]*}"}"
    [[ -z "$line" || "${line:0:1}" == "#" || "$line" != *"="* ]] && continue

    key="${line%%=*}"
    key="${key%"${key##*[![:space:]]}"}"
    [[ "$key" == "$requested_key" ]] || continue

    value="${line#*=}"
    value="${value#"${value%%[![:space:]]*}"}"
    value="${value%"${value##*[![:space:]]}"}"
    if [[ ${#value} -ge 2 ]]; then
      if [[ "${value:0:1}" == '"' && "${value: -1}" == '"' ]] || \
         [[ "${value:0:1}" == "'" && "${value: -1}" == "'" ]]; then
        value="${value:1:${#value}-2}"
      fi
    fi
    printf '%s' "$value"
    return
  done < "$ENV_FILE"

  printf '%s' "$default_value"
}

PROJECT_DIR="${PROJECT_DIR:-$(dotenv_value PROJECT_DIR "$SCRIPT_DIR")}"
COMPOSE_FILE="${COMPOSE_FILE:-$PROJECT_DIR/docker-compose-server.yml}"
COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-$(dotenv_value COMPOSE_PROJECT_NAME flycloud-server)}"
APP_UID="${APP_UID:-$(dotenv_value APP_UID 10001)}"
APP_GID="${APP_GID:-$(dotenv_value APP_GID 10001)}"
LOG_DIR_MODE="${LOG_DIR_MODE:-$(dotenv_value LOG_DIR_MODE 0750)}"
STOP_TIMEOUT_SECONDS="${STOP_TIMEOUT_SECONDS:-$(dotenv_value STOP_TIMEOUT_SECONDS 30)}"
PULL_BASE_IMAGES="${PULL_BASE_IMAGES:-$(dotenv_value PULL_BASE_IMAGES false)}"
REPAIR_LOG_OWNERSHIP="${REPAIR_LOG_OWNERSHIP:-$(dotenv_value REPAIR_LOG_OWNERSHIP false)}"

SERVICES=(
  flycloud-gateway
  flycloud-auth
  flycloud-system
  flycloud-bpm
  flycloud-mall
  flycloud-ai
)

export PROJECT_DIR COMPOSE_PROJECT_NAME

log() {
  printf '[%s] %s\n' "$(date '+%Y-%m-%d %H:%M:%S')" "$*"
}

warn() {
  printf '[%s] WARN: %s\n' "$(date '+%Y-%m-%d %H:%M:%S')" "$*" >&2
}

die() {
  printf '[%s] ERROR: %s\n' "$(date '+%Y-%m-%d %H:%M:%S')" "$*" >&2
  exit 1
}

on_error() {
  local exit_code=$?
  printf '[%s] ERROR: 命令执行失败，脚本=%s，行号=%s，退出码=%s\n' \
    "$(date '+%Y-%m-%d %H:%M:%S')" "${BASH_SOURCE[1]:-${BASH_SOURCE[0]}}" "${BASH_LINENO[0]:-unknown}" "$exit_code" >&2
  exit "$exit_code"
}

trap on_error ERR

require_deployment_tools() {
  command -v docker >/dev/null 2>&1 || die "未找到 docker 命令"
  docker compose version >/dev/null 2>&1 || die "未安装 Docker Compose V2（docker compose）"
}

validate_script_config() {
  [[ "$PROJECT_DIR" == /* ]] || die "PROJECT_DIR 必须是绝对路径：$PROJECT_DIR"
  [[ "$APP_UID" =~ ^[0-9]+$ ]] || die "APP_UID 必须是非负整数：$APP_UID"
  [[ "$APP_GID" =~ ^[0-9]+$ ]] || die "APP_GID 必须是非负整数：$APP_GID"
  [[ "$LOG_DIR_MODE" =~ ^0?[0-7]{3}$ ]] || die "LOG_DIR_MODE 必须是 750 或 0750 形式的权限值：$LOG_DIR_MODE"
  [[ "$STOP_TIMEOUT_SECONDS" =~ ^[1-9][0-9]*$ ]] || die \
    "STOP_TIMEOUT_SECONDS 必须是大于 0 的整数：$STOP_TIMEOUT_SECONDS"
  [[ "$PULL_BASE_IMAGES" == "true" || "$PULL_BASE_IMAGES" == "false" ]] || die \
    "PULL_BASE_IMAGES 只能是 true 或 false：$PULL_BASE_IMAGES"
  [[ "$REPAIR_LOG_OWNERSHIP" == "true" || "$REPAIR_LOG_OWNERSHIP" == "false" ]] || die \
    "REPAIR_LOG_OWNERSHIP 只能是 true 或 false：$REPAIR_LOG_OWNERSHIP"
}

check_env_file() {
  local env_mode

  if [[ ! -f "$ENV_FILE" ]]; then
    warn "未找到环境变量文件：$ENV_FILE；将使用默认值，AI 模型密钥可能为空"
    return
  fi

  if [[ "$(uname -s)" == "Darwin" ]]; then
    env_mode="$(stat -f '%Lp' "$ENV_FILE")"
  else
    env_mode="$(stat -c '%a' "$ENV_FILE")"
  fi

  [[ "${env_mode: -2}" == "00" ]] || warn \
    ".env 当前权限为 ${env_mode}，建议执行 chmod 600 '$ENV_FILE'，避免模型密钥被其他用户读取"
}

ensure_project_layout() {
  validate_script_config
  [[ -d "$PROJECT_DIR" ]] || die "项目目录不存在：$PROJECT_DIR"
  [[ -f "$COMPOSE_FILE" ]] || die "Compose 文件不存在：$COMPOSE_FILE"
  check_env_file
}

compose() {
  local compose_args=()
  if [[ -f "$ENV_FILE" ]]; then
    compose_args+=(--env-file "$ENV_FILE")
  fi
  docker compose "${compose_args[@]}" -p "$COMPOSE_PROJECT_NAME" -f "$COMPOSE_FILE" "$@"
}

validate_compose_config() {
  compose config --quiet
}

acquire_deployment_lock() {
  if ! command -v flock >/dev/null 2>&1; then
    warn "系统未安装 flock，无法防止多个部署脚本并发执行"
    return
  fi

  exec 9>"$PROJECT_DIR/.flycloud-deploy.lock"
  flock -n 9 || die "已有部署、启停或更新任务正在执行"
}

is_known_service() {
  local requested_service="$1"
  local service
  for service in "${SERVICES[@]}"; do
    [[ "$requested_service" == "$service" ]] && return 0
  done
  return 1
}

validate_service_artifact() {
  local service="$1"
  local service_dir="$PROJECT_DIR/$service"

  [[ -d "$service_dir" ]] || die "服务目录不存在：$service_dir"
  [[ -f "$service_dir/Dockerfile" ]] || die "Dockerfile 不存在：$service_dir/Dockerfile"
  [[ -s "$service_dir/$service.jar" ]] || die "服务 JAR 不存在或为空：$service_dir/$service.jar"
}

validate_all_service_artifacts() {
  local service
  for service in "${SERVICES[@]}"; do
    validate_service_artifact "$service"
  done
}

prepare_log_directory() {
  local service="$1"
  local log_dir="$PROJECT_DIR/$service/logs"
  local owner

  if [[ -e "$log_dir" && ! -d "$log_dir" ]]; then
    die "日志路径已存在但不是目录：$log_dir"
  fi

  if [[ ! -d "$log_dir" ]]; then
    mkdir -p -- "$log_dir"
    log "已创建日志目录：$log_dir"
  else
    log "复用已有日志目录（不会删除历史日志）：$log_dir"
  fi

  if [[ "$(id -u)" == "0" ]]; then
    # 只修改日志目录本身，不递归扫描或改写历史日志文件。
    chown "$APP_UID:$APP_GID" "$log_dir"
    chmod "$LOG_DIR_MODE" "$log_dir"

    # 仅在显式要求修复旧权限时递归处理，默认关闭。
    if [[ "${REPAIR_LOG_OWNERSHIP:-false}" == "true" ]]; then
      warn "正在按要求递归修复历史日志权限：$log_dir"
      chown -R "$APP_UID:$APP_GID" "$log_dir"
    fi
    return
  fi

  if [[ "$(uname -s)" == "Darwin" ]]; then
    owner="$(stat -f '%u:%g' "$log_dir")"
  else
    owner="$(stat -c '%u:%g' "$log_dir")"
  fi
  [[ "$owner" == "$APP_UID:$APP_GID" ]] || die \
    "日志目录属主为 ${owner}，容器需要 ${APP_UID}:${APP_GID}；请使用 root 执行部署或先修复该目录属主：${log_dir}"
}

prepare_log_directories() {
  local service
  for service in "$@"; do
    prepare_log_directory "$service"
  done
}

show_status() {
  log "当前服务状态："
  compose ps
}
