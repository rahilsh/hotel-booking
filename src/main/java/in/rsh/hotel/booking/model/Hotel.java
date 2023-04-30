package in.rsh.hotel.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class Hotel extends AbstractEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;

  public Hotel(String name, String address) {
    this.name = name;
    this.address = address;
  }

  public Hotel() {}
}
