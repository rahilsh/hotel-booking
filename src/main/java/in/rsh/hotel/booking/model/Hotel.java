package in.rsh.hotel.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Hotel extends AbstractEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String city;

  public Hotel(String name, String city) {
    this.name = name;
    this.city = city;
  }

  public Hotel() {}
}
