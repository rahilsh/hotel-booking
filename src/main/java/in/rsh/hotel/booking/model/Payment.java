package in.rsh.hotel.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
  public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    REFUNDED
  }

  private int id;
  private int bookingId;
  private double amount;
  private String currency; // USD, EUR, INR, etc.
  private PaymentStatus status;
  private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, NET_BANKING, UPI
  private String transactionId; // External payment gateway transaction ID
  private String failureReason; // Reason if payment failed
  private long createdAt;
  private long updatedAt;
}
