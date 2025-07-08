FROM eclipse-temurin:21-jdk

WORKDIR /work

COPY BuildTools.jar .

RUN apt-get update && apt-get install -y git

CMD ["java", "-jar", "BuildTools.jar", "--rev","1.21.6","--output-dir","/build"]

