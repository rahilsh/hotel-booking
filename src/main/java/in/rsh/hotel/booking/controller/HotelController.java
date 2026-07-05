package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.dto.HotelRequest;
import in.rsh.hotel.booking.dto.HotelResponse;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.service.HotelService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
public class HotelController {

  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  public ResponseEntity<?> getAllHotels() {
    Iterable<Hotel> hotels = hotelService.getAllHotels();
    var responses =
        StreamSupport.stream(hotels.spliterator(), false)
            .map(this::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HotelResponse> getHotel(@PathVariable("id") int id) {
    Hotel hotel = hotelService.getHotelById(id);
    return ResponseEntity.ok(toResponse(hotel));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHotel(@PathVariable("id") int id) {
    hotelService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<HotelResponse> saveHotel(@Valid @RequestBody HotelRequest request) {
    Hotel hotel = fromRequest(request);
    Hotel savedHotel = hotelService.saveOrUpdate(hotel);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedHotel));
  }

  private HotelResponse toResponse(Hotel hotel) {
    return new HotelResponse(hotel.getId(), hotel.getName(), hotel.getCity());
  }

  private Hotel fromRequest(HotelRequest request) {
    Hotel hotel = new Hotel();
    hotel.setName(request.getName());
    hotel.setCity(request.getCity());
    return hotel;
  }
}
