package in.rsh.hotel.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Audited
public class Person extends AbstractEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int age;

  @Column(nullable = false, unique = true)
  private String emailId;
}
