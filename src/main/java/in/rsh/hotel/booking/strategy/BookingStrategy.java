package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;
import java.util.Map;

public interface BookingStrategy {

  Room getNextAvailableRoom(Map<Integer, List<Room>> floorToAvailableRoomsMapping);
}
