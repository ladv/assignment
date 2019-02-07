Spring Boot application consists of :
	- spring-boot-starter-data-jpa
	- spring-boot-starter-web
	- spring-boot-starter-test
	- spring-boot-starter-security(security disabled to simplify usage)
Additional libraries used:
	mapstruct - to map entities to DTO
	liquibase - for versioning database structure

Requirements:
	jdk8+
    maven

Compile sources with mvn package
To start webapp run java -jar assignment.jar
Api can be accessed at http://localhost:8089/api/**

Exposed api uris:
	POST /users	- create user
	GET /users	- get all users
	GET /users/{id}	- get user by id
	POST /orders	- create product
	GET /orders	- get All products
	GET /orders/{id}- get product by id	
