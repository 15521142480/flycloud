FROM docker.elastic.co/elasticsearch/elasticsearch:8.18.3

# IK 插件版本必须与 Elasticsearch 精确一致；构建阶段固定下载地址，避免运行期漂移。
RUN bin/elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-ik/8.18.3

# 生产环境通过 jvm.options.d 固化 JVM 参数，不使用仅适用于临时调试的 ES_JAVA_OPTS。
COPY jvm.options.d/99-flycloud-memory.options /usr/share/elasticsearch/config/jvm.options.d/99-flycloud-memory.options
