FROM openjdk:27-ea-oracle
WORKDIR /app
ADD target/auth-0.0.1-SNAPSHOT.jar auth-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","auth-0.0.1-SNAPSHOT.jar"] 