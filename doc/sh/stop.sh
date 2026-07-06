#!/bin/bash

set -e

PROJECT_DIR="/project/flycloud-service"
COMPOSE_FILE="$PROJECT_DIR/docker-compose-server.yml"

docker compose -f "$COMPOSE_FILE" down
