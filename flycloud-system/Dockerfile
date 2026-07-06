
#jdk镜像
FROM eclipse-temurin:21-jre

# 作者信息
LABEL maintainer="lxs <2570078967@qq.com>"

# 暴露端口
EXPOSE 8085

# 设置容器内jar工作目录
WORKDIR /flycloud

# 创建非 root 用户并创建日志目录
# 创建固定 UID/GID 的非 root 用户
RUN groupadd -r -g 10001 flycloud && \
    useradd -r -u 10001 -g 10001 -m -d /home/flycloud flycloud && \
    mkdir -p /flycloud/logs && \
    chown -R 10001:10001 /flycloud /home/flycloud

# 声明日志卷，容器内应用生成的日志将被存储在这个位置
VOLUME /flycloud/logs

# 设置环境变量来指定时区
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS=""

# 将jar包添加到容器中
COPY --chown=10001:10001 flycloud-system.jar app.jar

# 使用非 root 用户运行
USER 10001:10001

# 启动命令
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
