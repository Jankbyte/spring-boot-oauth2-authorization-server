# Spring Boot OAuth2 Authorization server
### About project
The a implementation of [OAuth 2.1](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-01) authorization server.

### Technologies stack of project
Project written with or using:
* Java 21
* Spring Framework
  * Boot 3.x
  * Data JPA
  * Hibernate
  * Authorization Server
  * Session Data Redis
* PostgreSQL
* Redis
* Docker
* Apache JMeter (testing)

### Futuares
The some global fuatures of project:
* Storing session in redis. That means you can start multiple instances of app and they can share sessions by redis.
* Project writted with JPA. You can easy add fields for model without editing SQL-queries.
* Support for docker. You can create image (see build-docker-image.sh) and deploy that do server.
* Customizing login and consent pages.

### How to start project
For running project in dev-mode:
1. Start environment (PostgreSQL, Redis) with docker compose: ``docker compose -f docker-compose/docker-compose-dev.yaml up -d``
2. Now you can start service: ``gradle clean bootRun``

The fully command:
```
cd /Users/myuser/projects/spring-boot-oauth2-authorization-server
docker compose -f docker-compose/docker-compose-dev.yaml up -d
gradle clean bootRun
```

### Dev-credentials
You can find dev/test credentials in env-props dir