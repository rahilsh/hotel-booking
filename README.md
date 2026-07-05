# hotel-booking

Enterprise-grade hotel room booking backend application built on Spring Boot with JDBC Template.

## Tech Stack

- **Java 21** / **Spring Boot 4.1**
- **JDBC Template** — direct SQL control without ORM overhead
- **H2** — embedded database (file or in-memory)
- **SpringDoc OpenAPI 3** — auto-generated API docs (Swagger UI)
- **Lombok** — boilerplate reduction
- **Maven** — build tool

## Getting Started

1. Database schema is auto-initialized from `src/main/resources/schema.sql`
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

> Default JDBC URL: `jdbc:h2:mem:test`

## API Overview (v1)

All endpoints are versioned with `/v1/` prefix.

| Resource | Endpoints |
|----------|-----------|
| Hotels | `GET/POST /v1/hotels`, `GET/DELETE /v1/hotels/{id}` |
| Rooms | `GET/POST /v1/rooms`, `GET/DELETE /v1/rooms/{id}` |
| Persons | `GET/POST /v1/persons`, `GET /v1/persons/{id}` |
| Bookings | `GET/POST /v1/bookings`, `GET /v1/bookings/{id}`, `PATCH /v1/bookings/{id}` |

### Pagination & Filtering

All GET endpoints support pagination and sorting:
```
GET /v1/persons?page=0&size=10&sortBy=id&direction=ASC
GET /v1/bookings?page=0&size=10&personId=1&roomId=2
```

**Parameters:**
- `page` — page number (0-indexed, default: 0)
- `size` — page size (default: 10)
- `sortBy` — field to sort by (default: `id`)
- `direction` — `ASC` or `DESC` (default: `ASC`)
- `personId` — filter bookings by person (bookings only)
- `roomId` — filter bookings by room (bookings only)

### Booking a Room

- **By room ID** — specify `roomId` in the request body to book a specific room
- **By strategy** — omit `roomId` (or set to `0`) to auto-assign using configured strategy

### Booking Status

Bookings transition: `BOOKED` → `CANCELLED` or `ENDED` via `PATCH /v1/bookings/{id}`.

## Features

- **JDBC Template** — full SQL control without ORM overhead
- **Pessimistic Locking** — concurrent booking safety with database locks
- **Request/Response DTOs** — clean API contracts
- **Input Validation** — comprehensive validation with custom error handling
- **Structured Logging** — SLF4J throughout
- **Pagination & Filtering** — efficient data retrieval
- **API Versioning** — ready for future versions (v2, v3, etc.)
- **Concurrency-safe** — handles race conditions with pessimistic locking (FOR UPDATE)
- **Pluggable strategies** — room assignment strategies (TopToBottom, BottomToTop)
- **35+ Unit Tests** — 97% service coverage

## Architecture

```
controller/          — REST endpoints with validation & DTOs
service/            — business logic & transaction management
repository/         — JDBC-based data access
  ├── BaseRepository.java       (interface)
  ├── PersonJdbcRepository.java
  ├── HotelJdbcRepository.java
  ├── RoomJdbcRepository.java
  └── BookingJdbcRepository.java
model/              — POJO entities (no JPA annotations)
exception/          — custom exceptions & global handler
strategy/           — room assignment strategies
dto/                — request/response DTOs
schema.sql          — database schema
```

## Database Schema

Tables are automatically created from `schema.sql` on startup:

- **hotel** — hotels with name and city
- **person** — guests with name, age, email
- **room** — rooms with floor, status, and version (optimistic locking)
- **booking** — bookings linking person, room, and time range

All tables include `created_at` and `updated_at` timestamps and proper indexes for performance.

## Concurrency & Locking

- **Pessimistic Locking** — database-level locks (`SELECT ... FOR UPDATE`)
- **Prevents double-booking** — only one booking per room per time slot
- **Transactional** — all write operations are @Transactional

## Exception Handling

Global exception handler with custom exceptions:
- `ResourceNotFoundException` → HTTP 404
- `RoomNotAvailableException` → HTTP 409 (Conflict)
- `InvalidBookingException` → HTTP 400 (Bad Request)
- Validation errors → HTTP 400 with detailed messages

## Error Response Format

```json
{
  "error": "ROOM_NOT_AVAILABLE",
  "message": "Room is not available for the requested time period",
  "status": 409,
  "timestamp": 1720260000000
}
```

## Development

### Run Tests
```bash
mvn test
```

### View Coverage Report
```bash
open target/site/jacoco/index.html
```

### Build JAR
```bash
mvn clean package
```

## Future Development

- Integration with payment module
- Minimal user interface
- Priority queue for more efficient room assignment
- API authentication and authorization
- User login support
- Advanced analytics & reporting
