package in.rsh.hotel.booking.model;

import static in.rsh.hotel.booking.model.Booking.BookingStatus.BOOKED;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Booking {

  private final String id;
  private final Person person;
  private final Room room;
  private final long startTime;
  private final long endTime;
  @Setter private BookingStatus status;

  public Booking(String id, Person person, Room room, long startTime, long endTime) {
    this.id = id;
    this.person = person;
    this.room = room;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = BOOKED;
  }

  public enum BookingStatus {
    BOOKED,
    ENDED
  }
}
