#FROM openjdk:8-jdk-alpine
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
#
#
#
##使用jdk8作为基础镜像
#FROM openjdk:8-jdk-alpine
#FROM java:8
##指定作者
#MAINTAINER claire
##暴漏容器的8088端口
#EXPOSE 9090
##将复制指定的docker-demo-0.0.1-SNAPSHOT.jar为容器中的job.jar，相当于拷贝到容器中取了个别名
#ADD boot-for-k8sapp-1.0.0.jar /opt
##创建一个新的容器并在新的容器中运行命令
#RUN bash -c 'touch /app.jar'
##设置时区
##ENV TZ=PRC
##RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
##相当于在容器中用cmd命令执行jar包  指定外部配置文件
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM java:8

MAINTAINER claire

ADD boot-for-k8sapp-1.0.0.jar /opt

WORKDIR /opt

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "boot-for-k8sapp-1.0.0.jar"]
