FROM openjdk:8
EXPOSE 8080
WORKDIR /tmp
ADD target/employeedirectorymain.jar /tmp
CMD java -jar employeedirectorymain.jar