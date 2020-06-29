package in.rsh.hotel.booking.stats;

import in.rsh.hotel.booking.model.Booking;
import java.util.List;
import java.util.Map;

public interface Stats {

  Map<String, Integer> compute(List<Booking> bookings);
}
