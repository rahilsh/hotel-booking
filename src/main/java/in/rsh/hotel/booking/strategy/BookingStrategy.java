package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BookingStrategy {

  Room getNextRoom(List<Room> availableRooms);

  default Map<Integer, List<Room>> getFloorToAvailableRoomsMapping(List<Room> availableRooms) {
    return availableRooms.stream().collect(Collectors.groupingBy(Room::getFloorId));
  }
}
