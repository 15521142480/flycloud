# 构建镜像，执行命令：【docker build -t makunet:1.0 .】
FROM openjdk:8u212-jre
MAINTAINER 阿沐

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

ENTRYPOINT ["java", "-server", "-Xms512M", "-Xmx512M", "-Djava.security.egd=file:/dev/./urandom", "-Dfile.encoding=UTF-8", "-XX:+HeapDumpOnOutOfMemoryError", "-jar", "/app/app.jar" ]


