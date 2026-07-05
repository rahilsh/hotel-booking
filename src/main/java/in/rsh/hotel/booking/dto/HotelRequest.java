package in.rsh.hotel.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

  @NotBlank(message = "Hotel name is required")
  private String name;

  @NotBlank(message = "City is required")
  private String city;
}
