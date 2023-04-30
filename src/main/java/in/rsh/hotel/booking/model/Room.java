package in.rsh.hotel.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Room extends AbstractEntity {

  @Version protected int versionNumber = 0;

  private int floorId;

  @ManyToOne(optional = false)
  private Hotel hotel;

  @Column(nullable = false)
  @Setter
  @Enumerated(EnumType.STRING)
  private RoomStatus status;

  public Room(int floorId, Hotel hotel, RoomStatus status) {
    this.floorId = floorId;
    this.hotel = hotel;
    this.status = status;
  }

  public Room() {}

  public enum RoomStatus {
    AVAILABLE,
    OCCUPIED
  }
}
