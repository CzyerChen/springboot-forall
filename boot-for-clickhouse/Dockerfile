# 使用官方的Java运行时作为基础镜像
FROM eclipse-temurin:17-jdk

# 设置工作目录
WORKDIR /app

# 将本地的.jar文件复制到容器的工作目录中
COPY target/clickhouse-client-demo.jar app.jar

# 声明运行时容器所需环境变量
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "-javaagent:/skywalking/agent/skywalking-agent.jar", "/app/app.jar"]
#CMD ["--spring.profiles.active=prod"]

# 若需构建时下载依赖，也可以先拷贝源码和pom.xml，然后在容器内编译打包
# COPY . .
# RUN ./mvnw clean package -DskipTests && \
#     mv target/my-app.jar app.jar