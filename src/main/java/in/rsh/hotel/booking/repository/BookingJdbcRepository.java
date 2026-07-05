package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.model.Room;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class BookingJdbcRepository implements BaseRepository<Booking, Integer> {

  private final JdbcTemplate jdbcTemplate;
  private final PersonJdbcRepository personRepository;
  private final RoomJdbcRepository roomRepository;

  @Autowired
  public BookingJdbcRepository(JdbcTemplate jdbcTemplate, PersonJdbcRepository personRepository,
      RoomJdbcRepository roomRepository) {
    this.jdbcTemplate = jdbcTemplate;
    this.personRepository = personRepository;
    this.roomRepository = roomRepository;
  }

  private RowMapper<Booking> bookingRowMapper() {
    return new RowMapper<Booking>() {
      @Override
      public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setStartTime(rs.getLong("start_time"));
        booking.setEndTime(rs.getLong("end_time"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));

        Optional<Person> person = personRepository.findById(rs.getInt("person_id"));
        person.ifPresent(booking::setPerson);

        Optional<Room> room = roomRepository.findById(rs.getInt("room_id"));
        room.ifPresent(booking::setRoom);

        return booking;
      }
    };
  }

  @Override
  public Booking save(Booking booking) {
    if (booking.getId() == 0) {
      log.debug("Inserting new booking");
      String sql =
          "INSERT INTO booking (person_id, room_id, start_time, end_time, status) VALUES (?, ?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(con -> {
        var ps = con.prepareStatement(sql, new String[]{"id"});
        ps.setInt(1, booking.getPerson().getId());
        ps.setInt(2, booking.getRoom().getId());
        ps.setLong(3, booking.getStartTime());
        ps.setLong(4, booking.getEndTime());
        ps.setString(5, booking.getStatus().toString());
        return ps;
      }, keyHolder);
      booking.setId(keyHolder.getKey().intValue());
    } else {
      log.debug("Updating booking: {}", booking.getId());
      String sql =
          "UPDATE booking SET person_id = ?, room_id = ?, start_time = ?, end_time = ?, status = ? WHERE id = ?";
      jdbcTemplate.update(sql, booking.getPerson().getId(), booking.getRoom().getId(),
          booking.getStartTime(), booking.getEndTime(), booking.getStatus().toString(),
          booking.getId());
    }
    return booking;
  }

  @Override
  public Optional<Booking> findById(Integer id) {
    log.debug("Fetching booking with id: {}", id);
    String sql = "SELECT * FROM booking WHERE id = ?";
    List<Booking> bookings = jdbcTemplate.query(sql, bookingRowMapper(), id);
    return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.get(0));
  }

  @Override
  public List<Booking> findAll() {
    String sql = "SELECT * FROM booking";
    return jdbcTemplate.query(sql, bookingRowMapper());
  }

  @Override
  public Page<Booking> findAll(Pageable pageable) {
    log.debug("Fetching bookings with pagination");
    String sql = "SELECT * FROM booking ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Booking> bookings = jdbcTemplate.query(sql, bookingRowMapper(),
        pageable.getPageSize(), pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM booking";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class);

    return new PageImpl<>(bookings, pageable, total);
  }

  public List<Booking> findByRoomIdAndStatus(int roomId, BookingStatus status) {
    log.debug("Finding bookings by room {} and status: {}", roomId, status);
    String sql = "SELECT * FROM booking WHERE room_id = ? AND status = ?";
    return jdbcTemplate.query(sql, bookingRowMapper(), roomId, status.toString());
  }

  public Page<Booking> findByPersonId(int personId, Pageable pageable) {
    log.debug("Finding bookings by person {}", personId);
    String sql = "SELECT * FROM booking WHERE person_id = ? ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Booking> bookings = jdbcTemplate.query(sql, bookingRowMapper(), personId,
        pageable.getPageSize(), pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM booking WHERE person_id = ?";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class, personId);

    return new PageImpl<>(bookings, pageable, total);
  }

  public Page<Booking> findByStatus(BookingStatus status, Pageable pageable) {
    log.debug("Finding bookings by status: {}", status);
    String sql = "SELECT * FROM booking WHERE status = ? ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Booking> bookings = jdbcTemplate.query(sql, bookingRowMapper(), status.toString(),
        pageable.getPageSize(), pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM booking WHERE status = ?";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class, status.toString());

    return new PageImpl<>(bookings, pageable, total);
  }

  public Page<Booking> findByRoomId(int roomId, Pageable pageable) {
    log.debug("Finding bookings by room {}", roomId);
    String sql = "SELECT * FROM booking WHERE room_id = ? ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Booking> bookings = jdbcTemplate.query(sql, bookingRowMapper(), roomId,
        pageable.getPageSize(), pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM booking WHERE room_id = ?";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class, roomId);

    return new PageImpl<>(bookings, pageable, total);
  }

  @Override
  public void deleteById(Integer id) {
    log.debug("Deleting booking: {}", id);
    String sql = "DELETE FROM booking WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public long count() {
    String sql = "SELECT COUNT(*) FROM booking";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteAll() {
    log.debug("Deleting all bookings");
    String sql = "DELETE FROM booking";
    jdbcTemplate.update(sql);
  }
}
