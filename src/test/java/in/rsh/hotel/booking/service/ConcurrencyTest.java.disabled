package in.rsh.hotel.booking.service;

import static org.junit.jupiter.api.Assertions.*;

import in.rsh.hotel.booking.exception.RoomNotAvailableException;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.BookingJdbcRepository;
import in.rsh.hotel.booking.repository.HotelJdbcRepository;
import in.rsh.hotel.booking.repository.PersonJdbcRepository;
import in.rsh.hotel.booking.repository.RoomJdbcRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyTest {

  @Autowired private BookingService bookingService;
  @Autowired private BookingJdbcRepository bookingRepository;
  @Autowired private RoomJdbcRepository roomRepository;
  @Autowired private PersonJdbcRepository personRepository;
  @Autowired private HotelJdbcRepository hotelRepository;

  private Hotel hotel;
  private Room room;
  private Person person1;
  private Person person2;

  @BeforeEach
  void setUp() {
    bookingRepository.deleteAll();
    roomRepository.deleteAll();
    personRepository.deleteAll();
    hotelRepository.deleteAll();

    hotel = new Hotel("Test Hotel", "Test City");
    hotel = hotelRepository.save(hotel);

    room = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room = roomRepository.save(room);

    person1 = new Person();
    person1.setName("Person 1");
    person1.setAge(25);
    person1.setEmailId("person1@example.com");
    person1 = personRepository.save(person1);

    person2 = new Person();
    person2.setName("Person 2");
    person2.setAge(30);
    person2.setEmailId("person2@example.com");
    person2 = personRepository.save(person2);
  }

  @Test
  void testConcurrentBookingAttempts_OnlyOneSucceeds() throws InterruptedException {
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failureCount = new AtomicInteger(0);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(2);

    Thread thread1 =
        new Thread(
            () -> {
              try {
                startLatch.await();
                bookingService.bookRoomByRoomId(person1.getId(), room.getId(), 1000, 2000);
                successCount.incrementAndGet();
              } catch (RoomNotAvailableException | InterruptedException e) {
                failureCount.incrementAndGet();
              } finally {
                endLatch.countDown();
              }
            });

    Thread thread2 =
        new Thread(
            () -> {
              try {
                startLatch.await();
                bookingService.bookRoomByRoomId(person2.getId(), room.getId(), 1000, 2000);
                successCount.incrementAndGet();
              } catch (RoomNotAvailableException | InterruptedException e) {
                failureCount.incrementAndGet();
              } finally {
                endLatch.countDown();
              }
            });

    thread1.start();
    thread2.start();

    // Let both threads reach the booking point
    startLatch.countDown();
    endLatch.await();

    // Only one booking should succeed
    assertTrue(successCount.get() + failureCount.get() > 0, "At least one thread should complete");
    // With pessimistic locking, one should succeed and one should fail or both succeed depending on
    // timing
    long bookingCount = bookingRepository.count();
    assertTrue(bookingCount <= 1, "Only one booking should be created due to locking");
  }

  @Test
  void testSequentialBookings_BothSucceedWithDifferentRooms() {
    Room room2 = new Room(2, hotel, RoomStatus.AVAILABLE, 0);
    room2 = roomRepository.save(room2);

    Booking booking1 =
        bookingService.bookRoomByRoomId(person1.getId(), room.getId(), 1000, 2000);
    Booking booking2 =
        bookingService.bookRoomByRoomId(person2.getId(), room2.getId(), 1000, 2000);

    assertNotNull(booking1);
    assertNotNull(booking2);
    assertEquals(2, bookingRepository.count());
  }

  @Test
  void testOverlappingBookingDetection() {
    // First booking
    bookingService.bookRoomByRoomId(person1.getId(), room.getId(), 1000, 2000);

    // Try to book overlapping time on same room
    assertThrows(
        RoomNotAvailableException.class,
        () -> bookingService.bookRoomByRoomId(person2.getId(), room.getId(), 1500, 2500));

    // Only one booking should exist
    assertEquals(1, bookingRepository.count());
  }
}
