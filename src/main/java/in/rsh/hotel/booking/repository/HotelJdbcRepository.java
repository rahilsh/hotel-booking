package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Hotel;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class HotelJdbcRepository implements BaseRepository<Hotel, Integer> {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public HotelJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Hotel save(Hotel hotel) {
    if (hotel.getId() == 0) {
      log.debug("Inserting new hotel: {}", hotel.getName());
      String sql = "INSERT INTO hotel (name, city) VALUES (?, ?)";
      jdbcTemplate.update(sql, hotel.getName(), hotel.getCity());
    } else {
      log.debug("Updating hotel: {}", hotel.getId());
      String sql = "UPDATE hotel SET name = ?, city = ? WHERE id = ?";
      jdbcTemplate.update(sql, hotel.getName(), hotel.getCity(), hotel.getId());
    }
    return hotel;
  }

  @Override
  public Optional<Hotel> findById(Integer id) {
    log.debug("Finding hotel by id: {}", id);
    String sql = "SELECT * FROM hotel WHERE id = ?";
    List<Hotel> hotels =
        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Hotel.class), id);
    return hotels.isEmpty() ? Optional.empty() : Optional.of(hotels.get(0));
  }

  @Override
  public List<Hotel> findAll() {
    log.debug("Finding all hotels");
    String sql = "SELECT * FROM hotel";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Hotel.class));
  }

  @Override
  public Page<Hotel> findAll(Pageable pageable) {
    log.debug("Finding all hotels with pagination: page={}, size={}", pageable.getPageNumber(),
        pageable.getPageSize());
    String sql = "SELECT * FROM hotel ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Hotel> hotels = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Hotel.class),
        pageable.getPageSize(), pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM hotel";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class);

    return new PageImpl<>(hotels, pageable, total);
  }

  @Override
  public void deleteById(Integer id) {
    log.debug("Deleting hotel: {}", id);
    String sql = "DELETE FROM hotel WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public long count() {
    String sql = "SELECT COUNT(*) FROM hotel";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteAll() {
    log.debug("Deleting all hotels");
    String sql = "DELETE FROM hotel";
    jdbcTemplate.update(sql);
  }
}
