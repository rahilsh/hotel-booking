package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Payment;
import in.rsh.hotel.booking.model.Payment.PaymentStatus;
import in.rsh.hotel.booking.repository.BookingJdbcRepository;
import in.rsh.hotel.booking.repository.PaymentJdbcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PaymentService {

  private final PaymentJdbcRepository paymentRepository;
  private final BookingJdbcRepository bookingRepository;

  @Autowired
  public PaymentService(
      PaymentJdbcRepository paymentRepository, BookingJdbcRepository bookingRepository) {
    this.paymentRepository = paymentRepository;
    this.bookingRepository = bookingRepository;
  }

  @Transactional
  public Payment initializePayment(
      int bookingId, double amount, String currency, String paymentMethod) {
    log.info(
        "Initializing payment for booking: {}, amount: {} {}", bookingId, amount, currency);

    Booking booking =
        bookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

    Payment payment =
        Payment.builder()
            .bookingId(bookingId)
            .amount(amount)
            .currency(currency)
            .status(PaymentStatus.PENDING)
            .paymentMethod(paymentMethod)
            .build();

    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment processPayment(int paymentId, String transactionId) {
    log.info("Processing payment: {}, transactionId: {}", paymentId, transactionId);

    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

    payment.setStatus(PaymentStatus.PROCESSING);
    payment.setTransactionId(transactionId);
    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment markPaymentSuccess(int paymentId) {
    log.info("Marking payment as success: {}", paymentId);

    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

    if (payment.getStatus() != PaymentStatus.PROCESSING
        && payment.getStatus() != PaymentStatus.PENDING) {
      throw new IllegalArgumentException("Payment cannot be marked as success in current state");
    }

    payment.setStatus(PaymentStatus.SUCCESS);
    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment markPaymentFailed(int paymentId, String failureReason) {
    log.warn("Marking payment as failed: {}, reason: {}", paymentId, failureReason);

    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

    payment.setStatus(PaymentStatus.FAILED);
    payment.setFailureReason(failureReason);
    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment refundPayment(int paymentId) {
    log.info("Refunding payment: {}", paymentId);

    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

    if (payment.getStatus() != PaymentStatus.SUCCESS) {
      throw new IllegalArgumentException("Only successful payments can be refunded");
    }

    payment.setStatus(PaymentStatus.REFUNDED);
    return paymentRepository.save(payment);
  }

  public Payment getPaymentByBookingId(int bookingId) {
    log.debug("Fetching payment for booking: {}", bookingId);
    return paymentRepository
        .findByBookingId(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));
  }

  public Payment getPaymentById(int paymentId) {
    log.debug("Fetching payment: {}", paymentId);
    return paymentRepository
        .findById(paymentId)
        .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
  }
}
