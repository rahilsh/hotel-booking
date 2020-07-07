package in.rsh.hotel.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Person {

  private final int id;
  private final String name;
  private final int age;
}
