#!/bin/bash

set -e

PROJECT_DIR="/project/flycloud-service"
COMPOSE_FILE="$PROJECT_DIR/docker-compose-server.yml"

# 启动服务
docker compose -f "$COMPOSE_FILE" up -d
