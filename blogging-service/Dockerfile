FROM openjdk:14.0.1-oraclelinux7
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY config-entrypoint.sh /config-entrypoint.sh
#RUN yum -y install nc
ENTRYPOINT ["/config-entrypoint.sh"]