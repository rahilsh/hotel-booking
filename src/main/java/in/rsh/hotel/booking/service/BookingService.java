package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.BookingStatus;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Status;
import in.rsh.hotel.booking.store.BookingStore;
import in.rsh.hotel.booking.store.RoomStore;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import java.util.UUID;

public class BookingService {

  private final BookingStrategy defaultStrategy;

  public BookingService(BookingStrategy defaultStrategy) {
    this.defaultStrategy = defaultStrategy;
  }

  public Booking checkIn(Person person) {
    return checkIn(person, defaultStrategy);
  }

  public Booking checkIn(Person person, BookingStrategy bookingStrategy) {
    RoomStore roomStore = new RoomStore();
    Room nextAvailableRoom =
        bookingStrategy.getNextAvailableRoom(roomStore.getFloorToAvailableRoomsMapping());
    if (nextAvailableRoom == null) {
      return null;
    }
    roomStore.markRoomAsBooked(nextAvailableRoom);

    BookingStore bookingStore = new BookingStore();
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
    new BookingStore().updateStatus(booking.getId(), BookingStatus.ENDED);
    RoomStore roomStore = new RoomStore();
    Room room = roomStore.getRoom(booking.getRoomId());
    room.setStatus(Status.AVAILABLE);
    roomStore.addRoom(room, false);
    roomStore.updateRoomStatus(booking.getRoomId(), Status.AVAILABLE);
    return true;
  }
}
