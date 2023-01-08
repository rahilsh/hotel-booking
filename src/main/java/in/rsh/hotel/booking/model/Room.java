package in.rsh.hotel.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Entity
@Audited
public class Room extends AbstractEntity {

  @Version
  protected int versionNumber = 0;

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
