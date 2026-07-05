package in.rsh.hotel.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

  @NotNull(message = "Person ID is required")
  @Min(value = 1, message = "Person ID must be positive")
  private int personId;

  @Min(value = 0, message = "Room ID must be non-negative (0 for auto-assignment)")
  private int roomId;

  @NotNull(message = "Start time is required")
  @Min(value = 0, message = "Start time must be non-negative")
  private long startTime;

  @NotNull(message = "End time is required")
  @Min(value = 0, message = "End time must be non-negative")
  private long endTime;

  public void validate() {
    if (startTime >= endTime) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
  }
}
