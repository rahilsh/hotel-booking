package in.rsh.hotel.booking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
import in.rsh.hotel.booking.repository.RoomJdbcRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

  @Mock private RoomJdbcRepository roomRepository;

  private RoomService roomService;

  @BeforeEach
  void setUp() {
    roomService = new RoomService(roomRepository);
  }

  @Test
  void testGetAllRooms() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    Room room1 = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room1.setId(1);
    Room room2 = new Room(2, hotel, RoomStatus.OCCUPIED, 0);
    room2.setId(2);

    List<Room> rooms = Arrays.asList(room1, room2);
    when(roomRepository.findAll()).thenReturn(rooms);

    Iterable<Room> result = roomService.getAllRooms();

    assertNotNull(result);
    verify(roomRepository, times(1)).findAll();
  }

  @Test
  void testGetRoomById_Success() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    Room room = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room.setId(1);

    when(roomRepository.findById(1)).thenReturn(Optional.of(room));

    Room result = roomService.getRoomById(1);

    assertNotNull(result);
    assertEquals(1, result.getFloorId());
    assertEquals(RoomStatus.AVAILABLE, result.getStatus());
    verify(roomRepository, times(1)).findById(1);
  }

  @Test
  void testGetRoomById_NotFound() {
    when(roomRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(999));
    verify(roomRepository, times(1)).findById(999);
  }

  @Test
  void testGetRoomByIdAndStatus_Success() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    Room room = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room.setId(1);

    when(roomRepository.findByIdAndStatus(1, RoomStatus.AVAILABLE)).thenReturn(Optional.of(room));

    Room result = roomService.getRoomByIdAndStatus(1, RoomStatus.AVAILABLE);

    assertNotNull(result);
    assertEquals(RoomStatus.AVAILABLE, result.getStatus());
    verify(roomRepository, times(1)).findByIdAndStatus(1, RoomStatus.AVAILABLE);
  }

  @Test
  void testGetRoomByIdAndStatus_NotFound() {
    when(roomRepository.findByIdAndStatus(999, RoomStatus.AVAILABLE))
        .thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> roomService.getRoomByIdAndStatus(999, RoomStatus.AVAILABLE));
  }

  @Test
  void testGetRoomByStatus() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    Room room1 = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room1.setId(1);
    Room room2 = new Room(2, hotel, RoomStatus.AVAILABLE, 0);
    room2.setId(2);

    List<Room> rooms = Arrays.asList(room1, room2);
    when(roomRepository.findByStatus(RoomStatus.AVAILABLE)).thenReturn(rooms);

    List<Room> result = roomService.getRoomByStatus(RoomStatus.AVAILABLE);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(roomRepository, times(1)).findByStatus(RoomStatus.AVAILABLE);
  }

  @Test
  void testSaveOrUpdate() {
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    Room room = new Room(1, hotel, RoomStatus.AVAILABLE, 0);
    room.setId(1);

    when(roomRepository.findById(1)).thenReturn(Optional.of(room));
    when(roomRepository.save(any(Room.class))).thenReturn(room);

    Room result = roomService.saveOrUpdate(room);

    assertNotNull(result);
    assertEquals(RoomStatus.AVAILABLE, result.getStatus());
    verify(roomRepository, times(1)).save(room);
  }

  @Test
  void testDelete() {
    roomService.delete(1);
    verify(roomRepository, times(1)).deleteById(1);
  }
}
