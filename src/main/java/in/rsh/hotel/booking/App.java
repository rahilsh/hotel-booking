package in.rsh.hotel.booking;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.module.HotelBookingModule;
import in.rsh.hotel.booking.service.BookingService;
import in.rsh.hotel.booking.stats.GroupByAgeStats;
import in.rsh.hotel.booking.store.PersonStore;
import in.rsh.hotel.booking.store.RoomStore;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

// TODO: Move driver code to tests & Convert this app to a spring boot app
@Slf4j
public class App {
  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new HotelBookingModule());

    PersonStore personStore = injector.getInstance(PersonStore.class);

    addPersons(personStore);

    RoomStore roomStore = injector.getInstance(RoomStore.class);

    addRooms(roomStore);

    BookingService bookingService = injector.getInstance(BookingService.class);

    checkInAllRooms(personStore, bookingService);

    checkOutARoom(personStore, bookingService);

    List<Booking> bookings = bookingService.getBookings();

    printBookings(bookings, personStore);

    log.info("stats: {}", injector.getInstance(GroupByAgeStats.class).compute(bookings));
  }

  private static void checkOutARoom(PersonStore personStore, BookingService bookingService) {
    bookingService
        .getAnyActiveBooking()
        .ifPresent(
            booking -> {
              bookingService.checkOut(booking);
              bookingService.checkIn(personStore.get(booking.getPersonId()));
            });
  }

  private static void checkInAllRooms(PersonStore personStore, BookingService bookingService) {
    for (Person person : personStore.getAll()) {
      bookingService.checkIn(person);
    }
  }

  private static List<Booking> printBookings(List<Booking> bookings, PersonStore personStore) {

    for (Booking booking : bookings) {
      log.info(
          "Id: {}, Person Id: {}, Person Name: {}, RoomId: {}, status {}",
          booking.getId(),
          booking.getPersonId(),
          personStore.get(booking.getPersonId()).getName(),
          booking.getRoomId(),
          booking.getStatus().name());
    }
    return bookings;
  }

  private static void addRooms(RoomStore roomStore) {
    roomStore.addRooms(
        Lists.newArrayList(
            new Room(21, 11, RoomStatus.AVAILABLE),
            new Room(22, 11, RoomStatus.AVAILABLE),
            new Room(23, 12, RoomStatus.AVAILABLE),
            new Room(24, 12, RoomStatus.AVAILABLE)));
  }

  private static void addPersons(PersonStore personStore) {
    personStore.addMany(
        Lists.newArrayList(
            new Person(1, "a", 20),
            new Person(2, "b", 22),
            new Person(3, "c", 30),
            new Person(4, "d", 40)));
  }
}
