package in.rsh.hotel.booking.store;

import com.google.inject.Singleton;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// TODO: Use in-memory DB
@Singleton
public class RoomStore {

  static Map<Integer, List<Room>> floorToAvailableRoomsMapping = new TreeMap<>();

  static Map<Integer, Room> allRooms = new HashMap<>();

  public Map<Integer, List<Room>> getFloorToAvailableRoomsMapping() {
    return floorToAvailableRoomsMapping;
  }

  public void markRoomAsBooked(Room room) {
    floorToAvailableRoomsMapping.get(room.getFloorId()).remove(room);
    if (floorToAvailableRoomsMapping.get(room.getFloorId()).isEmpty()) {
      floorToAvailableRoomsMapping.remove(room.getFloorId());
    }

    updateRoomStatus(room.getId(), RoomStatus.OCCUPIED);
  }

  public void updateRoomStatus(int id, RoomStatus status) {
    allRooms.get(id).setStatus(status);
  }

  public void addRoom(Room room) {
    addRoom(room, true);
  }

  public void addRoom(Room room, boolean isNew) {
    if (isNew) {
      allRooms.put(room.getId(), room);
    }
    if (floorToAvailableRoomsMapping.containsKey(room.getFloorId())) {
      floorToAvailableRoomsMapping.get(room.getFloorId()).add(room);
    } else {
      List<Room> rooms = new ArrayList<>();
      rooms.add(room);
      floorToAvailableRoomsMapping.put(room.getFloorId(), rooms);
    }
  }

  public Room getRoom(Integer roomId) {
    return allRooms.get(roomId);
  }

  public void addRooms(List<Room> rooms) {
    rooms.forEach(this::addRoom);
  }
}
