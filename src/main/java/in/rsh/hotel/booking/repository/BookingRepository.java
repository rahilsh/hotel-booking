package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
  List<Booking> findByRoomIdAndStatus(int roomId, BookingStatus status);
}
