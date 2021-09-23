Welcome to "quarkus-microservices-with-jwt".

In order to learn the Quarkus framework and help others learn it, I passionately created this project which contains three Quarkus microservices:

- Product microservice uses JWT Authentication and provides Product CRUD operation endpoints (For simplicity reasons I included the .pem publickey and private key directly under /src/main/resources which is not a secure approach obviously). 
- Coffee microservice uses HTTP Basic Authentication and provides Coffee CRUD operation endpoints.
- Car microservice does not use authentication and provides Car CRUD operation endpoints.

Each microservice contains unit and integration tests using testContainer. Flyway for database migration. Postgres as database server. docker and docker-compose.

To run the Product microservice for example, follow these steps:

- run the command "docker-compose up db"
- access /apps/product-service then run "mvn clean install" command to generate the target directory which contains the project artifect
- run the command "docker-compose up product-service" (run this after the database is up and running and the artifect of the project is generated locally)
- to interact with the endpoint a postman collection can be found under apps/product-service/src/main/resources/product-service API.postman_collection.json

Enjoy learning Quarkus :)
