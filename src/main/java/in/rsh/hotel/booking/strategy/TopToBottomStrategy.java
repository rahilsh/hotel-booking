package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TopToBottomStrategy implements BookingStrategy {

  public Room getNextAvailableRoom(Map<Integer, List<Room>> floorToAvailableRoomsMapping) {
    if (floorToAvailableRoomsMapping.size() == 0) {
      System.out.println("No Free Rooms");
      return null;
    }
    return floorToAvailableRoomsMapping
        .get(((TreeMap) floorToAvailableRoomsMapping).lastKey())
        .get(0);
  }
}
