package in.rsh.hotel.booking.service;

import static in.rsh.hotel.booking.model.Booking.buildBooking;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.BookingRepository;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public List<Booking> getAllBookings() {
    List<Booking> bookings = new ArrayList<>();
    bookingRepository.findAll().forEach(bookings::add);
    return bookings;
  }

  public Booking getBookingById(int id) {
    final Optional<Booking> optionalBooking = bookingRepository.findById(id);
    if (!optionalBooking.isPresent()) {
      throw new IllegalArgumentException("Booking not found: " + id);
    }
    return optionalBooking.get();
  }

  @Transactional
  public Booking updateBookingStatus(int bookingId, BookingStatus status) {
    if (status.equals(BookingStatus.BOOKED)) {
      throw new IllegalArgumentException();
    }
    final Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
    if (!optionalBooking.isPresent()) {
      throw new IllegalArgumentException();
    }
    Booking booking = optionalBooking.get();
    if (!booking.getStatus().equals(BookingStatus.BOOKED)) {
      throw new IllegalArgumentException();
    }
    final Room room = booking.getRoom();
    room.setStatus(RoomStatus.AVAILABLE);
    roomService.saveOrUpdate(room);
    booking.setStatus(status);
    return bookingRepository.save(booking);
  }

  @Transactional
  public Booking bookRoomByRoomId(int personId, int roomId, long startTime, long endTime) {
    Room room = roomService.getRoomById(roomId);

    if (!room.getStatus().equals(RoomStatus.AVAILABLE)) {
      throw new IllegalArgumentException();
    }
    room.setStatus(RoomStatus.OCCUPIED);

    final Booking booking =
        buildBooking(startTime, endTime, room, personService.getPersonById(personId));

    roomService.saveOrUpdate(room);
    return bookingRepository.save(booking);
  }

  @Transactional
  public Booking bookRoomByStrategy(int personId, long startTime, long endTime) {

    final Room nextAvailableRoom = getNextAvailableRoom();
    nextAvailableRoom.setStatus(RoomStatus.OCCUPIED);

    Booking booking =
        buildBooking(startTime, endTime, nextAvailableRoom, personService.getPersonById(personId));
    roomService.saveOrUpdate(nextAvailableRoom);
    return bookingRepository.save(booking);
  }

  // TODO: Use priority queue
  private Room getNextAvailableRoom() {
    List<Room> availableRooms = roomService.getRoomByStatus(RoomStatus.AVAILABLE);
    if (availableRooms.isEmpty()) {
      throw new IllegalArgumentException("No rooms available");
    }
    return defaultStrategy.getNextAvailableRoom(
        availableRooms.stream().collect(Collectors.groupingBy(Room::getFloorId)));
  }
}
