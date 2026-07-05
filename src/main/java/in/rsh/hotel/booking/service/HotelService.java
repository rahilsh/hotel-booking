package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.repository.HotelRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HotelService {

  private final HotelRepository hotelRepository;

  @Autowired
  public HotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  public Iterable<Hotel> getAllHotels() {
    log.debug("Fetching all hotels");
    return hotelRepository.findAll();
  }

  public Page<Hotel> getAllHotels(Pageable pageable) {
    log.debug("Fetching hotels with pagination");
    return hotelRepository.findAll(pageable);
  }

  public Hotel getHotelById(int id) {
    log.debug("Fetching hotel with id: {}", id);
    final Optional<Hotel> optionalHotel = hotelRepository.findById(id);
    if (optionalHotel.isEmpty()) {
      log.warn("Hotel not found with id: {}", id);
      throw new ResourceNotFoundException("Hotel not found with id: " + id);
    }
    return optionalHotel.get();
  }

  public Hotel saveOrUpdate(Hotel hotel) {
    log.debug("Saving or updating hotel: {}", hotel.getId());
    return hotelRepository.save(hotel);
  }

  public void delete(int id) {
    log.debug("Deleting hotel with id: {}", id);
    hotelRepository.deleteById(id);
  }
}
