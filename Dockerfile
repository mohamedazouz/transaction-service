FROM java:8

ADD target/transaction-service.jar transaction-service.jar

CMD java -jar transaction-service.jar
