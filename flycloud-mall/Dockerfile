
#继承自哪一个基础的镜像
FROM anapsix/alpine-java:8_server-jre_unlimited
#FROM centos:centos7

# 作者信息
MAINTAINER lxs <2570078967@qq.com>

# 配置参数
ENV SERVER_PORT=8081

# 寄主映射到宿主的文件
VOLUME /project/flycloud-server/tmp

# 切换目录
#WORKDIR /project/flycloud-server/

# 暴露短口
EXPOSE ${SERVER_PORT}

# 复制文件或修改名称
#ADD ./target/flycloud-mall.jar ./app.jar
ADD flycloud-mall.jar ./app.jar

# 启动命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}","-jar", "app.jar"]
