package in.rsh.hotel.booking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person extends AbstractEntity {
  private String name;
  private int age;
  private String emailId;
}
