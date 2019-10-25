FROM java:8

COPY target/nation-details-jar-with-dependencies.jar nation-details.jar

RUN chmod +x nation-details.jar

CMD ["java", "-jar", "nation-details.jar"]