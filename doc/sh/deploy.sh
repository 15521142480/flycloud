

#!/bin/bash

set -e

PROJECT_DIR="/project/flycloud-service"
COMPOSE_FILE="$PROJECT_DIR/docker-compose-server.yml"

# 创建日志目录
mkdir -p \
  "$PROJECT_DIR/flycloud-gateway/logs" \
  "$PROJECT_DIR/flycloud-auth/logs" \
  "$PROJECT_DIR/flycloud-system/logs" \
  "$PROJECT_DIR/flycloud-bpm/logs" \
  "$PROJECT_DIR/flycloud-mall/logs"

# 设置日志目录权限
chown -R 10001:10001 \
  "$PROJECT_DIR/flycloud-gateway/logs" \
  "$PROJECT_DIR/flycloud-auth/logs" \
  "$PROJECT_DIR/flycloud-system/logs" \
  "$PROJECT_DIR/flycloud-bpm/logs" \
  "$PROJECT_DIR/flycloud-mall/logs"

# 构建并启动服务
docker compose -f "$COMPOSE_FILE" up -d --build
