package in.rsh.hotel.booking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking extends AbstractEntity {
  private Person person;
  private Room room;
  private long startTime;
  private long endTime;
  private BookingStatus status;

  public static Booking buildBooking(long startTime, long endTime, Room room, Person person) {
    if (startTime >= endTime) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
    final Booking booking = new Booking();
    booking.setRoom(room);
    booking.setPerson(person);
    booking.setStartTime(startTime);
    booking.setEndTime(endTime);
    booking.setStatus(BookingStatus.BOOKED);
    return booking;
  }

  public static void validateNewStatus(BookingStatus status) {
    if (status.equals(BookingStatus.BOOKED)) {
      throw new IllegalArgumentException("Cannot update status as BOOKED");
    }
  }

  public static void validateStateTransition(BookingStatus oldStatus, BookingStatus newStatus) {
    if (!oldStatus.equals(BookingStatus.BOOKED)) {
      throw new IllegalArgumentException(
          "State transition from %s to %s not allowed".formatted(oldStatus, newStatus));
    }
  }

  public enum BookingStatus {
    PENDING,
    BOOKED,
    CANCELLED,
    ENDED
  }
}
