package in.rsh.hotel.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health")
@Slf4j
public class HealthController {

  @GetMapping
  public ResponseEntity<HealthResponse> health() {
    return ResponseEntity.ok(
        HealthResponse.builder()
            .status("UP")
            .timestamp(System.currentTimeMillis())
            .message("Hotel Booking Service is running")
            .build());
  }

  public static class HealthResponse {
    private String status;
    private long timestamp;
    private String message;

    public HealthResponse(String status, long timestamp, String message) {
      this.status = status;
      this.timestamp = timestamp;
      this.message = message;
    }

    public static HealthResponseBuilder builder() {
      return new HealthResponseBuilder();
    }

    public String getStatus() {
      return status;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public String getMessage() {
      return message;
    }

    public static class HealthResponseBuilder {
      private String status;
      private long timestamp;
      private String message;

      public HealthResponseBuilder status(String status) {
        this.status = status;
        return this;
      }

      public HealthResponseBuilder timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
      }

      public HealthResponseBuilder message(String message) {
        this.message = message;
        return this;
      }

      public HealthResponse build() {
        return new HealthResponse(status, timestamp, message);
      }
    }
  }
}
