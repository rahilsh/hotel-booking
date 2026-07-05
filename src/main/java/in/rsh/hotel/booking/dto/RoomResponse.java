package in.rsh.hotel.booking.dto;

import in.rsh.hotel.booking.model.Room.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
  private int id;
  private int floorId;
  private int hotelId;
  private RoomStatus status;
}
