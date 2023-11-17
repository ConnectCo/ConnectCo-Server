FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ConnectCo.jar
ENTRYPOINT ["java","-jar","/ConnectCo.jar"]
