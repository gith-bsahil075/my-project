# Plain Java SE 21 project (no Maven/Gradle), so the Dockerfile just
# compiles with javac and runs with java -- no build tool needed.

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY src ./src
COPY public ./public

RUN javac -d bin \
    src/Main.java src/ServerMain.java \
    src/model/*.java src/dao/*.java src/service/*.java \
    src/server/*.java src/util/*.java

# Render (and most hosts) inject $PORT at runtime; ServerMain reads it
# automatically and falls back to 8080 if it's not set.
EXPOSE 8080

CMD ["java", "-cp", "bin", "ServerMain"]
