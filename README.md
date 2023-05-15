# Priceline Role API
This API is responsible to create roles and memberships.

A role is defined as a function that an user will perform inside a team, while a membership is defined by the combination of user, team and role.

## Table of contents
 * [Purpose](#purpose)
 * [Implementation](#implementation)
   * [Entities](#entities)
   * [Services](#services)
   * [Repositories](#repositories)
   * [Controllers](#controllers)
   * [Tests](#tests)
 * [How to run](#how-to-run)
 * [Database](#database)
 * [Swagger](#swagger)
 * [Known Issues](#known-issues)
 * [Enhancements](#enhancements)
 * [Suggestions](#suggestions)
 * [Contact](#contact)

## Purpose
Considering the requirements, his API was created in order to allow the creation of roles and membership assignments.

It offers endpoints that fully handle roles and memerships, and it interacts with external endpoints that was provided in the requirements.

External endpoints are required to ensure data consistency between the different APIs (user, team and role).

## Implementation
It consists in a SpringBoot application developed with Java 17. In their tech stack, it's possible to mention: MySQL database, Flyway, Swagger, jUnit tests and Mockito.

### Entities
The entities of the system are available inside package ``com.priceline.role.model``.

``BaseEntity.java`` is the class responsible to ensure code reuse and easily abstraction when using interfaces.

Moving forward we can find the main classes: ``Membership.java`` and ``Role.java``. They are responsible to map and handle the data involved in all operations.

### Services
The services of the system are available inside package ``com.priceline.role.service``.

Inside ``com.priceline.role.service.base`` package it's possible to find several generic interfaces that are used on the current services and also can be used by future service classes.

The main services are ``MembershipServiceImpl.java`` and ``RoleServiceImpl.java``. They are responsible to perform all the actions in the API and they are the only way to access the repository classes.

### Repositories
Repository classes can be found at ``com.priceline.role.repository`` package. They are interfaces extending ``JpaRepository`` and also added custom SpringData methods required to achieve the requirements.

### Controllers
Currently, the API is small and it has only ``RoleController.java`` and ``MembershipController.java``, both availabe in package ``com.priceline.role.controller``.

### Tests
Unit tests are implemented in all layers (repository, service and controller).

Currently, according to JaCoCo, the unit tests are covering 60% of the code.

To run JaCoCo reports again, execute the following command:
```shell
$ mvn clean install
```

Results are available at ``<path>/priceline/target/site/jacoco/index.html``


## How to run?
Code is available at [GitHub](#https://github.com/heliorosajr/priceline).

If you want to run the code in your IDE, you can import it as a regular maven project. Do not forget to create a MySQL database named priceline and also to configure the following required environment variables:
```shell
export DATABASE_USER=<your_user>
export DATABASE_PASSWORD=<your_password>
export DATABASE_URL=jdbc:mysql://<DB_HOST>:<DB_PORT>/priceline?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true
```

However, if you would like to use the API, you can use a docker for testing. 

First, build the jar file with:
```shell
mvn clean install -DskipTests
```

Then use the following command:
```shell
docker-compose up
```
After a few moments, you should be able to access the database and to make requests to the API.


## Database
All API data is stored in a MySQL database and the application is configured with flyway to handle database migrations. Migration files can be found at ``src/main/java/resources/db/migration``.

## Swagger
All endpoints are documented and you can find details accessing API Swagger, available [here](http://localhost:8080/swagger-ui/index.html#/)

## Known Issues
For some reason, after changing property file to use environment variable, repository tests started to fail. I'm investigating this problem.

## Enhancements
- Increase code coverage (currently it is close to 60%)
- Jenkins integration to stablish CI/CD
- Improve property files management with ansible scripts to easily deploy to test and production environments
- Migrate current synchronous request to asynchronous using queue mechanisms
- Implement Spring Security to protect endpoints

## Suggestions
- Implement Spring Security to protect endpoints
- Adopt pub/sub mechanism to keep subscribers data updated. With this approach, subscribers will be up-to-date with latest changes and will be able to process their data as required.

## Contact
If you have any questions about the implementation, suggestions or critics, please let me know. I'll be glad to answer.
- [Hélio De Rosa Junior](mailto:helio_junior@gmail.com) (11) 9 97195-5771