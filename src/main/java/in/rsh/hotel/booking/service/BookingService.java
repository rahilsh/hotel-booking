package in.rsh.hotel.booking.service;

import static in.rsh.hotel.booking.model.Booking.buildBooking;
import static in.rsh.hotel.booking.model.Booking.validateNewStatus;
import static in.rsh.hotel.booking.model.Booking.validateStateTransition;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.BookingRepository;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;
  private final RoomService roomService;
  private final PersonService personService;
  private final BookingStrategy defaultStrategy;

  @Autowired
  public BookingService(
      BookingRepository bookingRepository,
      RoomService roomService,
      PersonService personService,
      BookingStrategy defaultStrategy) {
    this.bookingRepository = bookingRepository;
    this.roomService = roomService;
    this.personService = personService;
    this.defaultStrategy = defaultStrategy;
  }

  public Iterable<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  public Booking getBookingById(int id) {
    final Optional<Booking> optionalBooking = bookingRepository.findById(id);
    if (optionalBooking.isEmpty()) {
      throw new IllegalArgumentException("Booking not found: " + id);
    }
    return optionalBooking.get();
  }

  @Transactional
  public Booking updateBookingStatus(int bookingId, BookingStatus status) {

    validateNewStatus(status);

    Booking booking = getBookingById(bookingId);

    validateStateTransition(booking.getStatus(), status);

    markRoomAsAvailable(booking);

    return updateBookingStatus(status, booking);
  }

  @Transactional
  public Booking bookRoomByRoomId(int personId, int roomId, long startTime, long endTime) {
    Room room = roomService.getRoomByIdAndStatus(roomId, RoomStatus.AVAILABLE);

    markRoomAsOccupied(room);

    return bookRoom(personId, startTime, endTime, room);
  }

  @Transactional
  public Booking bookRoomByStrategy(int personId, long startTime, long endTime) {

    final Room nextAvailableRoom = getNextAvailableRoom();

    markRoomAsOccupied(nextAvailableRoom);

    return bookRoom(personId, startTime, endTime, nextAvailableRoom);
  }

  private Booking updateBookingStatus(BookingStatus status, Booking booking) {
    booking.setStatus(status);
    return bookingRepository.save(booking);
  }

  private void markRoomAsAvailable(Booking booking) {
    final Room room = booking.getRoom();
    room.setStatus(RoomStatus.AVAILABLE);
  }

  private Booking bookRoom(int personId, long startTime, long endTime, Room room) {
    return bookingRepository.save(
        buildBooking(startTime, endTime, room, personService.getPersonById(personId)));
  }

  private void markRoomAsOccupied(Room room) {
    room.setStatus(RoomStatus.OCCUPIED);
  }

  // TODO: Use priority queue
  private Room getNextAvailableRoom() {
    List<Room> availableRooms = roomService.getRoomByStatus(RoomStatus.AVAILABLE);
    if (availableRooms.isEmpty()) {
      throw new IllegalArgumentException("No rooms available");
    }
    return defaultStrategy.getNextRoom(availableRooms);
  }
}
