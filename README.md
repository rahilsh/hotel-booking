# hotel-booking
Simple hotel booking backend application built on spring boot

```
Steps to start app

1. Update H2 DB file path by updating `spring.datasource.url=` in `resources/application.properties`
2. Run HotelBookingApplication.java using IDE or jar

```

```
Tools

* Access `http://localhost:8080/swagger-ui.html` to view API docs(swagger ui)
* Access H2 DB console on `http://localhost:8080/h2-console`

```

```
Features

* Uses spring-data-jpa which allows us to use any RDBMS
* Complete Object Oriented Design which is highly extensible
* Rest APIs for easy adoption
* Handles concurrency issues when multiple user selects same room.
* Support for defining strategy to assign a room
```

```
Future development

* Integration with Payment module
* Minimal user interface
* Use priority queue to assign a room more efficiently
* Authentice and Authorize API calls
* User login support
```
