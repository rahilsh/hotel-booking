package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoomService {

  private final RoomRepository roomRepository;

  @Autowired
  public RoomService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Iterable<Room> getAllRooms() {
    return roomRepository.findAll();
  }

  public Page<Room> getAllRooms(Pageable pageable) {
    log.debug("Fetching rooms with pagination");
    return roomRepository.findAll(pageable);
  }

  public Room getRoomById(int id) {
    log.debug("Fetching room with id: {}", id);
    return getIfPresentOrThrow(roomRepository.findById(id));
  }

  private Room getIfPresentOrThrow(Optional<Room> optionalRoom) {
    if (optionalRoom.isEmpty()) {
      log.warn("Room not found");
      throw new ResourceNotFoundException("Room not found");
    }
    return optionalRoom.get();
  }

  public Room getRoomByIdAndStatus(int id, RoomStatus status) {
    log.debug("Fetching room with id: {} and status: {} (with pessimistic write lock)", id, status);
    return getIfPresentOrThrow(roomRepository.findByIdAndStatus(id, status));
  }

  public Room getRoomByIdWithLock(int id) {
    log.debug("Fetching room with id: {} (with pessimistic write lock)", id);
    return getIfPresentOrThrow(roomRepository.findByIdWithPessimisticLock(id));
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
