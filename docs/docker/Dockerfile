FROM openjdk:17
LABEL authors="lixw"
WORKDIR /app
ADD ../llmchat-server/llmchat-server-start/target/*.jar app.jar
EXPOSE 8686
ENTRYPOINT ["java","-Dloader.path=lib","-Dspring.profiles.active=prod","-Dserver.port=8686","-jar","app.jar"]