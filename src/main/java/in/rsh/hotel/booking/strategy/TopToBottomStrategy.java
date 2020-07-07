package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopToBottomStrategy implements BookingStrategy {

  public Room getNextAvailableRoom(Map<Integer, List<Room>> floorToAvailableRoomsMapping) {
    if (floorToAvailableRoomsMapping.size() == 0) {
      log.warn("No Free Rooms");
      return null;
    }
    return floorToAvailableRoomsMapping
        .get(((TreeMap) floorToAvailableRoomsMapping).lastKey())
        .get(0);
  }
}
