# hotel-booking

Simple hotel room booking backend application built on Spring Boot.

## Tech Stack

- **Java 21** / **Spring Boot 4.1**
- **Spring Data JPA** — database abstraction (swap any RDBMS)
- **H2** — embedded database (file or in-memory)
- **SpringDoc OpenAPI 3** — auto-generated API docs (Swagger UI)
- **Lombok** — boilerplate reduction
- **Maven** — build tool

## Getting Started

1. Update the H2 DB file path in `src/main/resources/application.properties`:
   ```
   spring.datasource.url=
   ```
2. Run `HotelBookingApplication.java` via your IDE or build and run the jar:
   ```bash
   mvn package
   java -jar target/hotel-booking-*.jar
   ```

## Tools

| Tool | URL |
|------|-----|
| Swagger UI (API docs) | `http://localhost:8080/swagger-ui.html` |
| H2 DB Console | `http://localhost:8080/console` |

> The JDBC URL is printed in startup logs, e.g. `jdbc:h2:mem:<uuid>`

## API Overview

| Resource | Endpoints |
|----------|-----------|
| Hotels | `GET/POST /hotels`, `GET/DELETE /hotels/{id}` |
| Rooms | `GET/POST /rooms`, `GET/DELETE /rooms/{id}` |
| Persons | `GET/POST /persons`, `GET /persons/{id}` |
| Bookings | `GET/POST /bookings`, `GET /bookings/{id}`, `PATCH /bookings/{id}` |

### Booking a Room

- **By room ID** — specify `roomId` in the request body to book a specific room.
- **By strategy** — omit `roomId` (or set to `0`) to let the system auto-assign a room using the configured strategy.

### Booking Status

Bookings transition from `BOOKED` → `CANCELLED` or `ENDED` via `PATCH /bookings/{id}`.

## Features

- Uses Spring Data JPA — pluggable with any RDBMS
- Complete object-oriented design, highly extensible
- REST APIs for easy adoption
- Concurrency-safe: handles race conditions when multiple users select the same room
- Pluggable room assignment strategy (`TopToBottom`, `BottomToTop`)

## Future Development

- Integration with a payment module
- Minimal user interface
- Priority queue for more efficient room assignment
- API authentication and authorization
- User login support
