package in.rsh.hotel.booking.service;

import static in.rsh.hotel.booking.model.Booking.buildBooking;
import static in.rsh.hotel.booking.model.Booking.validateNewStatus;
import static in.rsh.hotel.booking.model.Booking.validateStateTransition;

import in.rsh.hotel.booking.dto.BookingRequest;
import in.rsh.hotel.booking.exception.InvalidBookingException;
import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.exception.RoomNotAvailableException;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.BookingRepository;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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

  public List<Booking> getAllBookings(Pageable pageable, Integer personId, Integer roomId) {
    log.debug("Fetching bookings with pagination and filters");
    if (personId != null) {
      return bookingRepository.findByPersonId(personId, pageable).stream()
          .collect(Collectors.toList());
    } else if (roomId != null) {
      return bookingRepository.findByRoomId(roomId, pageable).stream()
          .collect(Collectors.toList());
    } else {
      return bookingRepository.findAll(pageable).stream()
          .collect(Collectors.toList());
    }
  }

  public Booking getBookingById(int id) {
    log.debug("Fetching booking with id: {}", id);
    final Optional<Booking> optionalBooking = bookingRepository.findById(id);
    if (optionalBooking.isEmpty()) {
      log.warn("Booking not found with id: {}", id);
      throw new ResourceNotFoundException("Booking not found with id: " + id);
    }
    return optionalBooking.get();
  }

  @Transactional
  public Booking updateBookingStatus(int bookingId, BookingStatus status) {
    log.info("Updating booking {} status to {}", bookingId, status);
    try {
      validateNewStatus(status);

      Booking booking = getBookingById(bookingId);

      validateStateTransition(booking.getStatus(), status);

      markRoomAsAvailable(booking);

      return updateBookingStatus(status, booking);
    } catch (Exception e) {
      log.error("Error updating booking status for id: {}", bookingId, e);
      throw e;
    }
  }

  @Transactional
  public Booking bookRoomByRoomId(int personId, int roomId, long startTime, long endTime) {
    log.info("Booking room {} for person {} from {} to {}", roomId, personId, startTime, endTime);
    try {
      if (startTime >= endTime) {
        throw new InvalidBookingException("Start time must be before end time");
      }

      if (hasOverlappingBooking(roomId, startTime, endTime)) {
        log.warn(
            "Room {} has overlapping booking for time range {} to {}", roomId, startTime, endTime);
        throw new RoomNotAvailableException(
            "Room is not available for the requested time period");
      }

      Room room = roomService.getRoomByIdAndStatus(roomId, RoomStatus.AVAILABLE);

      markRoomAsOccupied(room);

      return bookRoom(personId, startTime, endTime, room);
    } catch (Exception e) {
      log.error("Error booking room {} for person {}", roomId, personId, e);
      throw e;
    }
  }

  @Transactional
  public Booking bookRoomByStrategy(int personId, long startTime, long endTime) {
    log.info("Booking room by strategy for person {} from {} to {}", personId, startTime, endTime);
    try {
      if (startTime >= endTime) {
        throw new InvalidBookingException("Start time must be before end time");
      }

      final Room nextAvailableRoom = getNextAvailableRoom();

      markRoomAsOccupied(nextAvailableRoom);

      return bookRoom(personId, startTime, endTime, nextAvailableRoom);
    } catch (Exception e) {
      log.error("Error booking room by strategy for person {}", personId, e);
      throw e;
    }
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
    log.debug("Fetching next available room using strategy: {}", defaultStrategy.getClass().getSimpleName());
    List<Room> availableRooms = roomService.getRoomByStatus(RoomStatus.AVAILABLE);
    if (availableRooms.isEmpty()) {
      log.warn("No rooms available for booking");
      throw new RoomNotAvailableException("No rooms available for booking");
    }
    Room selectedRoom = defaultStrategy.getNextRoom(availableRooms);
    log.debug("Selected room {} using strategy", selectedRoom.getId());
    return selectedRoom;
  }

  private boolean hasOverlappingBooking(int roomId, long startTime, long endTime) {
    List<Booking> bookings = bookingRepository.findByRoomIdAndStatus(roomId, BookingStatus.BOOKED);
    return bookings.stream()
        .anyMatch(
            b ->
                (startTime >= b.getStartTime() && startTime < b.getEndTime())
                    || (endTime > b.getStartTime() && endTime <= b.getEndTime())
                    || (startTime <= b.getStartTime() && endTime >= b.getEndTime()));
  }
}
