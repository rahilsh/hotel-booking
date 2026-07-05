package in.rsh.hotel.booking.model;

import static org.junit.jupiter.api.Assertions.*;

import in.rsh.hotel.booking.model.Booking.BookingStatus;
import org.junit.jupiter.api.Test;

class BookingModelTest {

  @Test
  void testBuildBooking_Success() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    hotel.setId(1);
    Room room = new Room(1, hotel, Room.RoomStatus.AVAILABLE);
    room.setId(1);

    Person person = new Person();
    person.setId(1);
    person.setName("John");
    person.setAge(25);
    person.setEmailId("john@example.com");

    Booking booking = Booking.buildBooking(1000, 2000, room, person);

    assertNotNull(booking);
    assertEquals(BookingStatus.BOOKED, booking.getStatus());
    assertEquals(1000, booking.getStartTime());
    assertEquals(2000, booking.getEndTime());
    assertEquals(room, booking.getRoom());
    assertEquals(person, booking.getPerson());
  }

  @Test
  void testBuildBooking_InvalidTimeRange() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    hotel.setId(1);
    Room room = new Room(1, hotel, Room.RoomStatus.AVAILABLE);
    room.setId(1);

    Person person = new Person();
    person.setId(1);
    person.setName("John");
    person.setAge(25);
    person.setEmailId("john@example.com");

    assertThrows(
        IllegalArgumentException.class, () -> Booking.buildBooking(2000, 1000, room, person));
  }

  @Test
  void testValidateNewStatus_InvalidStatus() {
    assertThrows(
        IllegalArgumentException.class, () -> Booking.validateNewStatus(BookingStatus.BOOKED));
  }

  @Test
  void testValidateNewStatus_ValidStatus() {
    assertDoesNotThrow(() -> Booking.validateNewStatus(BookingStatus.CANCELLED));
  }

  @Test
  void testValidateStateTransition_Success() {
    assertDoesNotThrow(() -> Booking.validateStateTransition(BookingStatus.BOOKED, BookingStatus.CANCELLED));
  }

  @Test
  void testValidateStateTransition_Failure() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Booking.validateStateTransition(BookingStatus.CANCELLED, BookingStatus.ENDED));
  }
}
