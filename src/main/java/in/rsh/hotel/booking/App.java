package in.rsh.hotel.booking;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Status;
import in.rsh.hotel.booking.service.BookingService;
import in.rsh.hotel.booking.stats.GroupByAgeStats;
import in.rsh.hotel.booking.stats.Stats;
import in.rsh.hotel.booking.store.BookingStore;
import in.rsh.hotel.booking.store.PersonStore;
import in.rsh.hotel.booking.store.RoomStore;
import in.rsh.hotel.booking.strategy.TopToBottomStrategy;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

// TODO: Move driver code to tests & Convert this app to a spring boot app
@Slf4j
public class App {
  public static void main(String[] args) {
    Person p1 = new Person(1, "a", 20);
    Person p2 = new Person(2, "b", 22);
    Person p3 = new Person(3, "c", 30);
    Person p4 = new Person(4, "d", 40);

    List<Person> persons = new ArrayList<>();
    PersonStore personStore = new PersonStore();
    persons.add(p1);
    personStore.add(p1);
    persons.add(p2);
    personStore.add(p2);
    persons.add(p3);
    personStore.add(p3);
    persons.add(p4);
    personStore.add(p4);

    Room room1 = new Room(21, 11, Status.AVAILABLE);
    Room room2 = new Room(22, 11, Status.AVAILABLE);
    Room room3 = new Room(23, 12, Status.AVAILABLE);

    RoomStore roomStore = new RoomStore();
    roomStore.addRoom(room1);
    roomStore.addRoom(room2);
    roomStore.addRoom(room3);

    BookingService bookingService = new BookingService(new TopToBottomStrategy());
    Booking currentBooking = null;
    for (Person person : persons) {
      Booking booking1 = bookingService.checkIn(person);
      if (booking1 != null) {
        currentBooking = booking1;
      }
    }

    bookingService.checkOut(currentBooking);
    bookingService.checkIn(p4);

    BookingStore bookingStore = new BookingStore();
    List<Booking> bookings = bookingStore.getBookings();
    for (Booking booking : bookings) {
      log.info(
          "Id: {}, Person Id: {}, Person Name: {}, RoomId: {}",
          booking.getId(),
          booking.getPersonId(),
          getPersonName(booking.getPersonId(), persons),
          booking.getRoomId());
    }

    Stats stats = new GroupByAgeStats();
    log.info("stats: {}", stats.compute(bookings));
  }

  private static String getPersonName(int personId, List<Person> persons) {
    for (Person person : persons) {
      if (person.getId() == personId) {
        return person.getName();
      }
    }
    return "";
  }
}
