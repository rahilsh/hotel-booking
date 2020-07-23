package in.rsh.hotel.booking.store;

import com.google.inject.Singleton;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO: Use in-memory DB
@Singleton
public class BookingStore {

  static List<Booking> bookings = new ArrayList<>();
  static Map<String, Booking> bookingsMap = new HashMap<>();

  public List<Booking> getBookings() {
    return bookings;
  }

  public void add(Booking booking) {
    bookings.add(booking);
    bookingsMap.put(booking.getId(), booking);
  }

  public void updateStatus(String bookingId, BookingStatus status) {
    bookingsMap.get(bookingId).setStatus(status);
  }
}
