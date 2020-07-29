package in.rsh.hotel.booking.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class AbstractEntity {

  @Id @GeneratedValue private int id;

  @Column(
      name = "created_at",
      insertable = false,
      updatable = false,
      columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
  protected LocalDateTime createdAt;

  @Column(
      name = "updated_at",
      insertable = false,
      updatable = false,
      columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  protected LocalDateTime updatedAt;
}
