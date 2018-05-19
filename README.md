Build and Deploy
----------------------

Make sure you have all necessary tools installed

    install/start docker
    Java 8, If you don't have docker!
    
Build the application:

    mvn clean install
    docker build -t "transaction-service" .
     
Run locally
----------------------

    Dockerfile
    * Java
        java -jar target/transaction-service.jar 
