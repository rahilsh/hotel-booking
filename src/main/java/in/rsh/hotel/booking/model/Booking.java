package in.rsh.hotel.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Entity
@Audited
public class Booking extends AbstractEntity {

  @ManyToOne(optional = false)
  @Setter
  private Person person;

  @ManyToOne @Setter private Room room;

  @Column(nullable = false)
  @Setter
  private long startTime;

  @Column(nullable = false)
  @Setter
  private long endTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Setter
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
