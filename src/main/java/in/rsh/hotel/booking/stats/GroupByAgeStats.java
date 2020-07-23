package in.rsh.hotel.booking.stats;

import com.google.inject.Singleton;
import in.rsh.hotel.booking.model.Booking;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class GroupByAgeStats implements Stats {

  @Override
  public Map<String, Integer> compute(List<Booking> bookings) {
    Map<String, Integer> ageRangeToCountMap = new HashMap<>();
    for (Booking booking : bookings) {
      int bucketId = booking.getPerson().getAge() / 10;
      String range = getRange(bucketId);
      if (ageRangeToCountMap.containsKey(range)) {
        ageRangeToCountMap.put(range, ageRangeToCountMap.get(range) + 1);
      } else {
        ageRangeToCountMap.put(range, 1);
      }
    }
    return ageRangeToCountMap;
  }

  private String getRange(int bucketId) {
    switch (bucketId) {
      case 1:
        return "10-19";
      case 2:
        return "20-29";
      case 3:
        return "30-39";
      case 0:
      default:
        return "0-9";
    }
  }
}
