package in.rsh.hotel.booking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import in.rsh.hotel.booking.exception.InvalidBookingException;
import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.exception.RoomNotAvailableException;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.BookingJdbcRepository;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock private BookingJdbcRepository bookingRepository;
  @Mock private RoomService roomService;
  @Mock private PersonService personService;
  @Mock private BookingStrategy bookingStrategy;

  private BookingService bookingService;
  private Hotel hotel;
  private Person person;
  private Room room;

  @BeforeEach
  void setUp() {
    bookingService =
        new BookingService(bookingRepository, roomService, personService, bookingStrategy);

    hotel = new Hotel("Ibis", "Mumbai");
    hotel.setId(1);

    person = new Person();
    person.setId(1);
    person.setName("John");
    person.setAge(25);
    person.setEmailId("john@example.com");

    room = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room.setId(1);
  }

  @Test
  void testGetAllBookings() {
    Booking booking = new Booking();
    booking.setId(1);
    booking.setPerson(person);
    booking.setRoom(room);
    booking.setStartTime(1000);
    booking.setEndTime(2000);
    booking.setStatus(BookingStatus.BOOKED);

    List<Booking> bookings = Arrays.asList(booking);
    when(bookingRepository.findAll()).thenReturn(bookings);

    Iterable<Booking> result = bookingService.getAllBookings();

    assertNotNull(result);
    verify(bookingRepository, times(1)).findAll();
  }

  @Test
  void testGetBookingById_Success() {
    Booking booking = new Booking();
    booking.setId(1);
    booking.setPerson(person);
    booking.setRoom(room);
    booking.setStartTime(1000);
    booking.setEndTime(2000);
    booking.setStatus(BookingStatus.BOOKED);

    when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

    Booking result = bookingService.getBookingById(1);

    assertNotNull(result);
    assertEquals(BookingStatus.BOOKED, result.getStatus());
    verify(bookingRepository, times(1)).findById(1);
  }

  @Test
  void testGetBookingById_NotFound() {
    when(bookingRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(999));
  }

  @Test
  void testBookRoomByRoomId_Success() {
    when(roomService.getRoomByIdAndStatus(1, RoomStatus.AVAILABLE)).thenReturn(room);
    when(personService.getPersonById(1)).thenReturn(person);

    Booking savedBooking = new Booking();
    savedBooking.setId(1);
    savedBooking.setPerson(person);
    savedBooking.setRoom(room);
    savedBooking.setStartTime(1000);
    savedBooking.setEndTime(2000);
    savedBooking.setStatus(BookingStatus.BOOKED);

    when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
    when(bookingRepository.findByRoomIdAndStatus(1, BookingStatus.BOOKED))
        .thenReturn(Collections.emptyList());

    Booking result = bookingService.bookRoomByRoomId(1, 1, 1000, 2000);

    assertNotNull(result);
    assertEquals(BookingStatus.BOOKED, result.getStatus());
    assertEquals(RoomStatus.OCCUPIED, room.getStatus());
  }

  @Test
  void testBookRoomByRoomId_InvalidTimeRange() {
    assertThrows(
        InvalidBookingException.class, () -> bookingService.bookRoomByRoomId(1, 1, 2000, 1000));
  }

  @Test
  void testBookRoomByRoomId_OverlappingBooking() {
    Booking existingBooking = new Booking();
    existingBooking.setId(1);
    existingBooking.setPerson(person);
    existingBooking.setRoom(room);
    existingBooking.setStartTime(1500);
    existingBooking.setEndTime(2500);
    existingBooking.setStatus(BookingStatus.BOOKED);

    when(bookingRepository.findByRoomIdAndStatus(1, BookingStatus.BOOKED))
        .thenReturn(Arrays.asList(existingBooking));

    assertThrows(
        RoomNotAvailableException.class, () -> bookingService.bookRoomByRoomId(1, 1, 1000, 2000));
  }

  @Test
  void testBookRoomByStrategy_Success() {
    when(roomService.getRoomByStatus(RoomStatus.AVAILABLE)).thenReturn(Arrays.asList(room));
    when(bookingStrategy.getNextRoom(Arrays.asList(room))).thenReturn(room);
    when(personService.getPersonById(1)).thenReturn(person);

    Booking savedBooking = new Booking();
    savedBooking.setId(1);
    savedBooking.setPerson(person);
    savedBooking.setRoom(room);
    savedBooking.setStartTime(1000);
    savedBooking.setEndTime(2000);
    savedBooking.setStatus(BookingStatus.BOOKED);

    when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

    Booking result = bookingService.bookRoomByStrategy(1, 1000, 2000);

    assertNotNull(result);
    assertEquals(BookingStatus.BOOKED, result.getStatus());
  }

  @Test
  void testBookRoomByStrategy_InvalidTimeRange() {
    assertThrows(
        InvalidBookingException.class, () -> bookingService.bookRoomByStrategy(1, 2000, 1000));
  }

  @Test
  void testBookRoomByStrategy_NoRoomsAvailable() {
    when(roomService.getRoomByStatus(RoomStatus.AVAILABLE))
        .thenReturn(Collections.emptyList());

    assertThrows(
        RoomNotAvailableException.class, () -> bookingService.bookRoomByStrategy(1, 1000, 2000));
  }

  @Test
  void testUpdateBookingStatus_Success() {
    Booking booking = new Booking();
    booking.setId(1);
    booking.setPerson(person);
    booking.setRoom(room);
    booking.setStartTime(1000);
    booking.setEndTime(2000);
    booking.setStatus(BookingStatus.BOOKED);

    when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
    when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

    Booking result = bookingService.updateBookingStatus(1, BookingStatus.CANCELLED);

    assertNotNull(result);
    assertEquals(RoomStatus.AVAILABLE, room.getStatus());
  }

  @Test
  void testUpdateBookingStatus_InvalidStatusTransition() {
    Booking booking = new Booking();
    booking.setId(1);
    booking.setPerson(person);
    booking.setRoom(room);
    booking.setStartTime(1000);
    booking.setEndTime(2000);
    booking.setStatus(BookingStatus.CANCELLED);

    when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

    assertThrows(
        IllegalArgumentException.class,
        () -> bookingService.updateBookingStatus(1, BookingStatus.ENDED));
  }
}
