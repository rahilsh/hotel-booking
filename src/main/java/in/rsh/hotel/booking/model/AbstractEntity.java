package in.rsh.hotel.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class AbstractEntity {

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
  @Id
  @GeneratedValue
  private int id;
}
