package in.rsh.hotel.booking.stats;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.store.PersonStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GroupByAgeStats implements Stats {

  private final PersonStore personStore;

  @Override
  public Map<String, Integer> compute(List<Booking> bookings) {
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
