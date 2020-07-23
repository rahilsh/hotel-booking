package in.rsh.hotel.booking.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.store.BookingStore;
import in.rsh.hotel.booking.store.RoomStore;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BookingService {

  private final BookingStrategy defaultStrategy;
  private final BookingStore bookingStore;
  private final RoomStore roomStore;

  public Booking checkIn(Person person) {
    return checkIn(person, defaultStrategy);
  }

  public Booking checkIn(Person person, BookingStrategy bookingStrategy) {
    Room nextAvailableRoom =
        bookingStrategy.getNextAvailableRoom(roomStore.getFloorToAvailableRoomsMapping());
    if (nextAvailableRoom == null) {
      return null;
    }
    roomStore.markRoomAsBooked(nextAvailableRoom);

    Booking booking =
        new Booking(
            UUID.randomUUID().toString(),
            person.getId(),
            nextAvailableRoom.getId(),
            System.currentTimeMillis(),
            System.currentTimeMillis() + 3600L);
    bookingStore.add(booking);

    return booking;
  }

  public boolean checkOut(Booking booking) {
    booking.setStatus(BookingStatus.ENDED);
    bookingStore.updateStatus(booking.getId(), BookingStatus.ENDED);
    Room room = roomStore.getRoom(booking.getRoomId());
    room.setStatus(RoomStatus.AVAILABLE);
    roomStore.addRoom(room, false);
    roomStore.updateRoomStatus(booking.getRoomId(), RoomStatus.AVAILABLE);
    return true;
  }

  public Optional<Booking> getAnyActiveBooking() {
    return bookingStore
        .getBookings()
        .stream()
        .filter(booking -> booking.getStatus().equals(BookingStatus.BOOKED))
        .findFirst();
  }

  public List<Booking> getBookings() {
    return bookingStore.getBookings();
  }
}
