package in.rsh.hotel.booking.dto;

import in.rsh.hotel.booking.model.Booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
  private int id;
  private int personId;
  private int roomId;
  private long startTime;
  private long endTime;
  private BookingStatus status;
}
