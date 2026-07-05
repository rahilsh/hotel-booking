package in.rsh.hotel.booking.dto;

import in.rsh.hotel.booking.model.Payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
  private int id;
  private int bookingId;
  private double amount;
  private String currency;
  private PaymentStatus status;
  private String paymentMethod;
  private String transactionId;
  private String failureReason;
  private long createdAt;
  private long updatedAt;
}
