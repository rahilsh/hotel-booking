package in.rsh.hotel.booking.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEntity {
  protected int id;
  protected LocalDateTime createdAt;
  protected LocalDateTime updatedAt;
}
