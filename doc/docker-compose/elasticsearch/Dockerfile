FROM docker.elastic.co/elasticsearch/elasticsearch:8.18.3

# IK 插件版本必须与 Elasticsearch 精确一致；构建阶段固定下载地址，避免运行期漂移。
RUN bin/elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-ik/8.18.3
