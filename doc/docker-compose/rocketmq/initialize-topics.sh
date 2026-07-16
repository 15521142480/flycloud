#!/bin/sh
# RocketMQ 业务 Topic 初始化脚本。
#
# 使用方式（在部署机执行）：
#   chmod +x /docker/rocketmq/config/initialize-topics.sh
#   /docker/rocketmq/config/initialize-topics.sh
#
# 本脚本可重复执行。updateTopic 会创建不存在的 Topic，已存在时更新其队列配置。
# Broker 保持 autoCreateTopicEnable=false，防止拼写错误或恶意请求自动创建大量 Topic。

set -eu

# Broker 容器名称，与 Compose 中的 container_name 保持一致。
BROKER_CONTAINER="rocketmq_broker"
# 容器网络内的 NameServer 地址。
NAMESRV_ADDR="rocketmq_namesrv:9876"
# 与 broker.conf 的 brokerClusterName 保持一致。
BROKER_CLUSTER="FlycloudRocketMqCluster"
# 单 Broker 个人环境的读写队列数；生产集群应按吞吐量单独调整。
READ_QUEUE_NUMS="8"
WRITE_QUEUE_NUMS="8"
# 已启用的业务域 Topic。新增业务 Topic 后在此处显式登记。
ROCKETMQ_TOPICS="flycloud-system-user-event flycloud-mall-product-event flycloud-bpm-model-event"

if ! docker inspect -f '{{.State.Running}}' "$BROKER_CONTAINER" 2>/dev/null | grep -qx 'true'; then
  echo "RocketMQ Broker 容器未运行：$BROKER_CONTAINER" >&2
  exit 1
fi

for topic in $ROCKETMQ_TOPICS; do
  echo "初始化 Topic：$topic"
  docker exec "$BROKER_CONTAINER" sh mqadmin updateTopic \
    -n "$NAMESRV_ADDR" \
    -c "$BROKER_CLUSTER" \
    -t "$topic" \
    -r "$READ_QUEUE_NUMS" \
    -w "$WRITE_QUEUE_NUMS"
done

echo "Topic 初始化完成。"
