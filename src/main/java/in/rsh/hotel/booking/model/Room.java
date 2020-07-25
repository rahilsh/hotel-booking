package in.rsh.hotel.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Room {

  @Version protected int versionNumber = 0;

  @Id @GeneratedValue private int id;
  private int floorId;

  @ManyToOne(optional = false)
  private Hotel hotel;

  @Column(nullable = false)
  @Setter
  @Enumerated(EnumType.STRING)
  private RoomStatus status;

  public enum RoomStatus {
    AVAILABLE,
    OCCUPIED
  }
}
