package in.rsh.hotel.booking.stats;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.store.PersonStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupByAgeStats implements Stats {

  @Override
  public Map<String, Integer> compute(List<Booking> bookings) {
    PersonStore personStore = new PersonStore();
    Map<String, Integer> ageRangeToCountMap = new HashMap<>();
    for (Booking booking : bookings) {
      Person person = personStore.get(booking.getPersonId());
      int bucketId = person.getAge() / 10;
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
      case 0:
        return "0-9";
      case 1:
        return "10-19";
      case 2:
        return "20-29";
      case 3:
        return "30-39";
      default:
        return "0-9";
    }
  }
}
