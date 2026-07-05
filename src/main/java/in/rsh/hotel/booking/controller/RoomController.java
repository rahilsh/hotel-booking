package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.dto.RoomRequest;
import in.rsh.hotel.booking.dto.RoomResponse;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.service.HotelService;
import in.rsh.hotel.booking.service.RoomService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class RoomController {

  private final RoomService roomService;
  private final HotelService hotelService;

  @Autowired
  public RoomController(RoomService roomService, HotelService hotelService) {
    this.roomService = roomService;
    this.hotelService = hotelService;
  }

  @GetMapping
  public ResponseEntity<?> getAllRooms(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
    var pageable = PageRequest.of(page, size, direction, sortBy);
    var rooms = roomService.getAllRooms(pageable);
    var responses = rooms.stream().map(this::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoomResponse> getRoom(@PathVariable("id") int id) {
    Room room = roomService.getRoomById(id);
    return ResponseEntity.ok(toResponse(room));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRoom(@PathVariable("id") int id) {
    roomService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<RoomResponse> saveRoom(@Valid @RequestBody RoomRequest request) {
    Hotel hotel = hotelService.getHotelById(request.getHotelId());
    Room room = fromRequest(request, hotel);
    Room savedRoom = roomService.saveOrUpdate(room);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedRoom));
  }

  private RoomResponse toResponse(Room room) {
    return new RoomResponse(room.getId(), room.getFloorId(), room.getHotel().getId(),
        room.getStatus());
  }

  private Room fromRequest(RoomRequest request, Hotel hotel) {
    Room room = new Room();
    room.setFloorId(request.getFloorId());
    room.setHotel(hotel);
    room.setStatus(Room.RoomStatus.AVAILABLE);
    return room;
  }
}
