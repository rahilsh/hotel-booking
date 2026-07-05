package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Payment;
import in.rsh.hotel.booking.model.Payment.PaymentStatus;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PaymentJdbcRepository implements BaseRepository<Payment, Integer> {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PaymentJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Payment save(Payment payment) {
    if (payment.getId() == 0) {
      log.debug("Inserting new payment for booking: {}", payment.getBookingId());
      String sql =
          "INSERT INTO payment (booking_id, amount, currency, status, payment_method, transaction_id, failure_reason) VALUES (?, ?, ?, ?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(
          con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, payment.getBookingId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getCurrency());
            ps.setString(4, payment.getStatus().toString());
            ps.setString(5, payment.getPaymentMethod());
            ps.setString(6, payment.getTransactionId());
            ps.setString(7, payment.getFailureReason());
            return ps;
          },
          keyHolder);
      payment.setId(keyHolder.getKey().intValue());
    } else {
      log.debug("Updating payment: {}", payment.getId());
      String sql =
          "UPDATE payment SET booking_id = ?, amount = ?, currency = ?, status = ?, payment_method = ?, transaction_id = ?, failure_reason = ? WHERE id = ?";
      jdbcTemplate.update(
          sql,
          payment.getBookingId(),
          payment.getAmount(),
          payment.getCurrency(),
          payment.getStatus().toString(),
          payment.getPaymentMethod(),
          payment.getTransactionId(),
          payment.getFailureReason(),
          payment.getId());
    }
    return payment;
  }

  @Override
  public Optional<Payment> findById(Integer id) {
    log.debug("Finding payment by id: {}", id);
    String sql = "SELECT * FROM payment WHERE id = ?";
    List<Payment> payments =
        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), id);
    return payments.isEmpty() ? Optional.empty() : Optional.of(payments.get(0));
  }

  @Override
  public List<Payment> findAll() {
    log.debug("Finding all payments");
    String sql = "SELECT * FROM payment";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class));
  }

  @Override
  public Page<Payment> findAll(Pageable pageable) {
    log.debug("Finding all payments with pagination");
    String sql = "SELECT * FROM payment ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Payment> payments =
        jdbcTemplate.query(
            sql,
            new BeanPropertyRowMapper<>(Payment.class),
            pageable.getPageSize(),
            pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM payment";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class);

    return new PageImpl<>(payments, pageable, total);
  }

  @Override
  public void deleteById(Integer id) {
    log.debug("Deleting payment: {}", id);
    String sql = "DELETE FROM payment WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public long count() {
    String sql = "SELECT COUNT(*) FROM payment";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteAll() {
    log.debug("Deleting all payments");
    String sql = "DELETE FROM payment";
    jdbcTemplate.update(sql);
  }

  public Optional<Payment> findByBookingId(int bookingId) {
    log.debug("Finding payment by booking id: {}", bookingId);
    String sql = "SELECT * FROM payment WHERE booking_id = ?";
    List<Payment> payments =
        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), bookingId);
    return payments.isEmpty() ? Optional.empty() : Optional.of(payments.get(0));
  }

  public List<Payment> findByBookingIdAndStatus(int bookingId, PaymentStatus status) {
    log.debug("Finding payments by booking id: {} and status: {}", bookingId, status);
    String sql = "SELECT * FROM payment WHERE booking_id = ? AND status = ?";
    return jdbcTemplate.query(
        sql, new BeanPropertyRowMapper<>(Payment.class), bookingId, status.toString());
  }

  public Optional<Payment> findByTransactionId(String transactionId) {
    log.debug("Finding payment by transaction id: {}", transactionId);
    String sql = "SELECT * FROM payment WHERE transaction_id = ?";
    List<Payment> payments =
        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), transactionId);
    return payments.isEmpty() ? Optional.empty() : Optional.of(payments.get(0));
  }

  public List<Payment> findByStatus(PaymentStatus status) {
    log.debug("Finding payments by status: {}", status);
    String sql = "SELECT * FROM payment WHERE status = ?";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), status.toString());
  }
}
