package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BottomToTopStrategy implements BookingStrategy {

  public Room getNextAvailableRoom(Map<Integer, List<Room>> floorToAvailableRoomsMapping) {
    return floorToAvailableRoomsMapping
        .get(new TreeMap<>(floorToAvailableRoomsMapping).firstKey())
        .get(0);
  }
}
