package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Hotel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HotelRepository extends CrudRepository<Hotel, Integer>, PagingAndSortingRepository<Hotel, Integer> {}
