package in.rsh.hotel.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
  @Min(value = 1, message = "Booking ID must be positive")
  private int bookingId;

  @Min(value = 1, message = "Amount must be greater than 0")
  private double amount;

  @NotBlank(message = "Currency is required")
  private String currency;

  @NotBlank(message = "Payment method is required")
  private String paymentMethod;
}
