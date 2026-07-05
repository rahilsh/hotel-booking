package in.rsh.hotel.booking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.repository.HotelJdbcRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

  @Mock private HotelJdbcRepository hotelRepository;

  private HotelService hotelService;

  @BeforeEach
  void setUp() {
    hotelService = new HotelService(hotelRepository);
  }

  @Test
  void testGetAllHotels() {
    Hotel hotel1 = new Hotel("Ibis", "Mumbai");
    hotel1.setId(1);
    Hotel hotel2 = new Hotel("Marriott", "Delhi");
    hotel2.setId(2);

    List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
    when(hotelRepository.findAll()).thenReturn(hotels);

    Iterable<Hotel> result = hotelService.getAllHotels();

    assertNotNull(result);
    verify(hotelRepository, times(1)).findAll();
  }

  @Test
  void testGetHotelById_Success() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    hotel.setId(1);

    when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

    Hotel result = hotelService.getHotelById(1);

    assertNotNull(result);
    assertEquals("Ibis", result.getName());
    assertEquals("Mumbai", result.getCity());
    verify(hotelRepository, times(1)).findById(1);
  }

  @Test
  void testGetHotelById_NotFound() {
    when(hotelRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> hotelService.getHotelById(999));
    verify(hotelRepository, times(1)).findById(999);
  }

  @Test
  void testSaveOrUpdate() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    hotel.setId(1);

    when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

    Hotel result = hotelService.saveOrUpdate(hotel);

    assertNotNull(result);
    assertEquals("Ibis", result.getName());
    verify(hotelRepository, times(1)).save(hotel);
  }

  @Test
  void testDelete() {
    hotelService.delete(1);
    verify(hotelRepository, times(1)).deleteById(1);
  }
}
