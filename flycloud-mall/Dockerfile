
#jdk镜像
FROM eclipse-temurin:21-jre

# 作者信息
LABEL maintainer="lxs <2570078967@qq.com>"

# 暴露端口
EXPOSE 8081

# 设置容器内jar工作目录
WORKDIR /flycloud

# 创建非 root 用户并创建日志目录
RUN groupadd -r flycloud && \
    useradd -r -g flycloud flycloud && \
    mkdir -p /flycloud/logs && \
    chown -R flycloud:flycloud /flycloud

# 声明日志卷，容器内应用生成的日志将被存储在这个位置
VOLUME /flycloud/logs

# 设置环境变量来指定时区
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS=""

# 将jar包添加到容器中
COPY --chown=flycloud:flycloud flycloud-mall.jar app.jar

# 使用非 root 用户运行
USER flycloud

# 启动命令
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
