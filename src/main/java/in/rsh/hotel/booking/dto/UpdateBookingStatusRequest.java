package in.rsh.hotel.booking.dto;

import in.rsh.hotel.booking.model.Booking.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingStatusRequest {
  @NotNull(message = "Status is required")
  private BookingStatus status;
}
