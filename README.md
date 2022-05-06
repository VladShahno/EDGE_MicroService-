1. Create an .env file in the root of this project and insert the following 
variables:

* #PostgreSQL
* POSTGRES_USER=postgres
* POSTGRES_PASSWORD=postgres
* POSTGRES_DB=files_info
* 
* #SpringMail
* SPRING_MAIL_PASSWORD=qWERTY2017!
* SPRING_MAIL_USERNAME=shakhno2022@gmail.com

2. To up the application in a docker container, you need to run the command
      2.1  docker network create external-core_edge (creation of a common network for access to 
           CORE microservice containers)
      2.2  docker-compose up --build from the project root

3. URL for swagger -http://localhost:8081/swagger-ui.html#/ 

4. For ADMIN role log to - 
      login: Admin 
      password: xsw2@WSX 

5. For USER role log to -
      login: edgetester 
      password: xsw2@WSX
