package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.repository.HotelRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

  private final HotelRepository hotelRepository;

  @Autowired
  public HotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  public List<Hotel> getAllHotels() {
    List<Hotel> hotels = new ArrayList<>();
    hotelRepository.findAll().forEach(hotels::add);
    return hotels;
  }

  public Hotel getHotelById(int id) {
    final Optional<Hotel> optionalHotel = hotelRepository.findById(id);
    if (!optionalHotel.isPresent()) {
      throw new IllegalArgumentException("Hotel not found: " + id);
    }
    return optionalHotel.get();
  }

  public void saveOrUpdate(Hotel hotel) {
    hotelRepository.save(hotel);
  }

  public void delete(int id) {
    hotelRepository.deleteById(id);
  }
}
