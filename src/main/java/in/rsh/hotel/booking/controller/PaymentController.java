package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.dto.PaymentRequest;
import in.rsh.hotel.booking.dto.PaymentResponse;
import in.rsh.hotel.booking.model.Payment;
import in.rsh.hotel.booking.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping
  public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
    log.info("Creating payment for booking: {}", request.getBookingId());

    Payment payment =
        paymentService.initializePayment(
            request.getBookingId(),
            request.getAmount(),
            request.getCurrency(),
            request.getPaymentMethod());

    PaymentResponse response = toPaymentResponse(payment);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<PaymentResponse> getPayment(@PathVariable int paymentId) {
    log.info("Fetching payment: {}", paymentId);
    Payment payment = paymentService.getPaymentById(paymentId);
    return ResponseEntity.ok(toPaymentResponse(payment));
  }

  @GetMapping("/booking/{bookingId}")
  public ResponseEntity<PaymentResponse> getPaymentByBookingId(@PathVariable int bookingId) {
    log.info("Fetching payment for booking: {}", bookingId);
    Payment payment = paymentService.getPaymentByBookingId(bookingId);
    return ResponseEntity.ok(toPaymentResponse(payment));
  }

  @PostMapping("/{paymentId}/process")
  public ResponseEntity<PaymentResponse> processPayment(
      @PathVariable int paymentId, @RequestBody ProcessPaymentRequest request) {
    log.info("Processing payment: {}", paymentId);

    Payment payment = paymentService.processPayment(paymentId, request.getTransactionId());
    return ResponseEntity.ok(toPaymentResponse(payment));
  }

  @PostMapping("/{paymentId}/success")
  public ResponseEntity<PaymentResponse> markPaymentSuccess(@PathVariable int paymentId) {
    log.info("Marking payment as success: {}", paymentId);

    Payment payment = paymentService.markPaymentSuccess(paymentId);
    return ResponseEntity.ok(toPaymentResponse(payment));
  }

  @PostMapping("/{paymentId}/refund")
  public ResponseEntity<PaymentResponse> refundPayment(@PathVariable int paymentId) {
    log.info("Refunding payment: {}", paymentId);

    Payment payment = paymentService.refundPayment(paymentId);
    return ResponseEntity.ok(toPaymentResponse(payment));
  }

  private PaymentResponse toPaymentResponse(Payment payment) {
    return PaymentResponse.builder()
        .id(payment.getId())
        .bookingId(payment.getBookingId())
        .amount(payment.getAmount())
        .currency(payment.getCurrency())
        .status(payment.getStatus())
        .paymentMethod(payment.getPaymentMethod())
        .transactionId(payment.getTransactionId())
        .failureReason(payment.getFailureReason())
        .createdAt(payment.getCreatedAt())
        .updatedAt(payment.getUpdatedAt())
        .build();
  }

  public static class ProcessPaymentRequest {
    private String transactionId;

    public String getTransactionId() {
      return transactionId;
    }

    public void setTransactionId(String transactionId) {
      this.transactionId = transactionId;
    }
  }
}
