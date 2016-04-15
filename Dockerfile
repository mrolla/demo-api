FROM java:8

COPY build/libs/api-demo.jar /opt/api-demo/api-demo.jar
WORKDIR /opt/api-demo

EXPOSE 4567

ENTRYPOINT ["java", "-jar", "api-demo.jar"]