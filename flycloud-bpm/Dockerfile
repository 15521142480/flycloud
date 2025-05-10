
#继承自哪一个基础的镜像
FROM openjdk:8-jre

# 作者信息
MAINTAINER lxs <2570078967@qq.com>

# 暴露短口
EXPOSE 8090

# 设置容器内jar工作目录
WORKDIR /flycloud

# 声明日志卷，容器内应用生成的日志将被存储在这个位置
VOLUME /flycloud/logs

# 设置环境变量来指定时区
ENV TZ=Asia/Shanghai

# 将jar包添加到容器中
ADD flycloud-bpm.jar ./app.jar

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
