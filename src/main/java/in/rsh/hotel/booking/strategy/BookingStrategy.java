package in.rsh.hotel.booking.strategy;

import in.rsh.hotel.booking.model.Room;
import java.util.List;

public interface BookingStrategy {

  Room getNextRoom(List<Room> availableRooms);
}
