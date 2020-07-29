package in.rsh.hotel.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.envers.Audited;

@Getter
@Entity
@Audited
public class Hotel {
  @Id @GeneratedValue private int id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;
}
