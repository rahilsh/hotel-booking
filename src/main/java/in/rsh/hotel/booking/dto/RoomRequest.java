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
public class RoomRequest {

  @Min(value = 1, message = "Floor ID must be positive")
  private int floorId;

  @NotNull(message = "Hotel ID is required")
  @Min(value = 1, message = "Hotel ID must be positive")
  private int hotelId;
}
