package in.rsh.hotel.booking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Booking {

  private final String id;
  private final int personId;
  private final int roomId;
  private final long startTime;
  private final long endTime;
  @Setter private BookingStatus status;

  public Booking(String id, int personId, int roomId, long startTime, long endTime) {
    this.id = id;
    this.personId = personId;
    this.roomId = roomId;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
