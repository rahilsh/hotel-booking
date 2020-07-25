package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Integer> {}
