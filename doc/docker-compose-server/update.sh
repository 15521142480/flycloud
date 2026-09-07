
#!/bin/bash

set -e

PROJECT_DIR="/project/flycloud-service"
COMPOSE_FILE="$PROJECT_DIR/docker-compose-server.yml"

SERVICE="$1"

if [ -z "$SERVICE" ]; then
  echo "请输入需要更新的服务名称"
  echo "例如：./update.sh flycloud-auth"
  exit 1
fi

echo "开始更新服务：$SERVICE"

# 重新构建指定服务镜像
docker compose -f "$COMPOSE_FILE" build "$SERVICE"

# 重新创建并启动指定服务
docker compose -f "$COMPOSE_FILE" up -d --no-deps "$SERVICE"

echo "服务更新完成：$SERVICE"
