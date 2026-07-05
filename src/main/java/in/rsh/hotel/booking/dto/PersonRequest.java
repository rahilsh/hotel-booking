package in.rsh.hotel.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @Min(value = 18, message = "Age must be at least 18")
  private int age;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email is required")
  private String emailId;
}
