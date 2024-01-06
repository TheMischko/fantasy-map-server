FROM eclipse-temurin:17-jdk-jammy
WORKDIR ./app

# Copy the current directory contents into the container at /app
COPY target/server-1.0-SNAPSHOT-jar-with-dependencies.jar .

# Compile the application (if necessary)
# RUN mvn clean install

# Specify the command to run on container start
CMD ["java", "-jar", "server-1.0-SNAPSHOT-jar-with-dependencies.jar"]