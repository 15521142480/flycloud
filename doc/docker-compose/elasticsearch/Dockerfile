FROM docker.elastic.co/elasticsearch/elasticsearch:8.18.3

# IK 插件版本必须与 Elasticsearch 精确一致；构建阶段固定下载地址，避免运行期漂移。
RUN bin/elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-ik/8.18.3

# IK 配置指定外置词典位置；词典本身由 Compose 以只读方式挂载，更新词典无需重建镜像（即在/docker/elasticsearch/custom.dic新增完后，执行 docker compose restart elasticsearch_one）。
RUN mkdir -p /usr/share/elasticsearch/plugins/analysis-ik/config/custom
COPY IKAnalyzer.cfg.xml /usr/share/elasticsearch/plugins/analysis-ik/config/IKAnalyzer.cfg.xml
