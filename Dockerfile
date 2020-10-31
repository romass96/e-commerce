FROM openjdk:11-apline
MAINTAINER Roman Babii <romababiy96@gmail.com>
EXPOSE 8085
ADD target/ugolek-0.1.jar /app/ugolek.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/ugolek.jar"]
