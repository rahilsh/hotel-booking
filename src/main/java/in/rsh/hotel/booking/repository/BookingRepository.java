package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookingRepository extends CrudRepository<Booking, Integer>, PagingAndSortingRepository<Booking, Integer> {
  
  @EntityGraph(attributePaths = {"person", "room", "room.hotel"})
  List<Booking> findByRoomIdAndStatus(int roomId, BookingStatus status);

  @EntityGraph(attributePaths = {"person", "room", "room.hotel"})
  @Query("SELECT b FROM Booking b WHERE b.person.id = ?1")
  Page<Booking> findByPersonId(int personId, Pageable pageable);

  @EntityGraph(attributePaths = {"person", "room", "room.hotel"})
  @Query("SELECT b FROM Booking b WHERE b.status = ?1")
  Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

  @EntityGraph(attributePaths = {"person", "room", "room.hotel"})
  @Query("SELECT b FROM Booking b WHERE b.room.id = ?1")
  Page<Booking> findByRoomId(int roomId, Pageable pageable);

  @EntityGraph(attributePaths = {"person", "room", "room.hotel"})
  @Override
  Page<Booking> findAll(Pageable pageable);
}
