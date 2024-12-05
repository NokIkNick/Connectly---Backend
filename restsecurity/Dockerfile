FROM eclipse-temurin:17-alpine
# This is the jar file that you want to run
COPY target/app.jar /app.jar
#This is the port that your javalin application will listen on
EXPOSE 7070
# This is the command that will be run when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]