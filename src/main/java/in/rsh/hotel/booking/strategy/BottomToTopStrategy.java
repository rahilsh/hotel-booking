package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BottomToTopStrategy implements BookingStrategy {

  public Room getNextRoom(List<Room> availableRooms) {
    final Map<Integer, List<Room>> floorToAvailableRoomsMapping =
        availableRooms.stream().collect(Collectors.groupingBy(Room::getFloorId));
    return floorToAvailableRoomsMapping
        .get(new TreeMap<>(floorToAvailableRoomsMapping).firstKey())
        .get(0);
  }
}
