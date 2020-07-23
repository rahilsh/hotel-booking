package in.rsh.hotel.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Room {
  private final int id;
  private final int floorId;
  @Setter private RoomStatus status;

  public enum RoomStatus {
    AVAILABLE,
    OCCUPIED
  }
}
