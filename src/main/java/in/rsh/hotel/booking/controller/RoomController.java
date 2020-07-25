package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.service.RoomService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

  private final RoomService roomService;

  @Autowired
  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @GetMapping
  public List<Room> getAllRooms() {
    return roomService.getAllRooms();
  }

  @GetMapping("/{id}")
  public Room getRoom(@PathVariable("id") int id) {
    return roomService.getRoomById(id);
  }

  @DeleteMapping("/{id}")
  public void deleteRoom(@PathVariable("id") int id) {
    roomService.delete(id);
  }

  @PostMapping
  public Room saveRoom(@RequestBody Room room) {
    return roomService.saveOrUpdate(room);
  }
}
