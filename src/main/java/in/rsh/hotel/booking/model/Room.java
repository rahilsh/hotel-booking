package in.rsh.hotel.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends AbstractEntity {
  private int floorId;
  private Hotel hotel;
  private RoomStatus status;
  private int versionNumber;

  public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE
  }
}
