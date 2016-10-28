FROM openjdk:8

WORKDIR /opt

ADD target/submit-1.0-SNAPSHOT.jar submit.jar
ADD configuration.yaml .

EXPOSE 9002 9003

CMD ["java", "-jar", "submit.jar"]
