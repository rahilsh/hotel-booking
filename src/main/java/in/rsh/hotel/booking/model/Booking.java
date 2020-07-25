package in.rsh.hotel.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class Booking {

  @Id @GeneratedValue private int id;

  @ManyToOne(optional = false)
  private Person person;

  @ManyToOne private Room room;

  @Column(nullable = false)
  private long startTime;

  @Column(nullable = false)
  private long endTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private BookingStatus status;

  public static Booking buildBooking(long startTime, long endTime, Room room, Person person) {
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
          String.format("State transition from %s to %s not allowed", oldStatus, newStatus));
    }
  }

  public enum BookingStatus {
    BOOKED,
    CANCELLED,
    ENDED
  }
}
