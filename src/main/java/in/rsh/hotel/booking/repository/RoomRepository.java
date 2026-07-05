package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Integer> {

  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  Optional<Room> findById(int id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Room> findByIdAndStatus(int id, RoomStatus status);

  List<Room> findByStatus(RoomStatus status);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT r FROM Room r WHERE r.id = ?1")
  Optional<Room> findByIdWithPessimisticLock(Integer id);
}
