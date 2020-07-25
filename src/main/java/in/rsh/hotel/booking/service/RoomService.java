package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

  private final RoomRepository roomRepository;

  @Autowired
  public RoomService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Iterable<Room> getAllRooms() {
    return roomRepository.findAll();
  }

  public Room getRoomById(int id) {
    return getIfPresentOrThrow(roomRepository.findById(id));
  }

  private Room getIfPresentOrThrow(Optional<Room> optionalRoom) {
    if (!optionalRoom.isPresent()) {
      throw new IllegalArgumentException();
    }
    return optionalRoom.get();
  }

  public Room getRoomByIdAndStatus(int id, RoomStatus status) {
    return getIfPresentOrThrow(roomRepository.findByIdAndStatus(id, status));
  }

  public Room saveOrUpdate(Room room) {
    final Optional<Room> optionalRoom = roomRepository.findById(room.getId());
    optionalRoom.ifPresent(value -> room.setStatus(value.getStatus()));
    return roomRepository.save(room);
  }

  public void delete(int id) {
    roomRepository.deleteById(id);
  }

  public List<Room> getRoomByStatus(RoomStatus status) {
    return roomRepository.findByStatus(status);
  }
}
