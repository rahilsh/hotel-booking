package in.rsh.hotel.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import org.hibernate.envers.Audited;

@Getter
@Entity
@Audited
public class Hotel extends AbstractEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;
}
