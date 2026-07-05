package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.model.Room.RoomStatus;
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
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RoomJdbcRepository implements BaseRepository<Room, Integer> {

  private final JdbcTemplate jdbcTemplate;
  private final HotelJdbcRepository hotelRepository;

  @Autowired
  public RoomJdbcRepository(JdbcTemplate jdbcTemplate, HotelJdbcRepository hotelRepository) {
    this.jdbcTemplate = jdbcTemplate;
    this.hotelRepository = hotelRepository;
  }

  private RowMapper<Room> roomRowMapper() {
    return new RowMapper<Room>() {
      @Override
      public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setFloorId(rs.getInt("floor_id"));
        room.setStatus(RoomStatus.valueOf(rs.getString("status")));
        room.setVersionNumber(rs.getInt("version_number"));

        Optional<Hotel> hotel = hotelRepository.findById(rs.getInt("hotel_id"));
        hotel.ifPresent(room::setHotel);

        return room;
      }
    };
  }

  @Override
  public Room save(Room room) {
    if (room.getId() == 0) {
      log.debug("Inserting new room: floor={}", room.getFloorId());
      String sql = "INSERT INTO room (floor_id, hotel_id, status, version_number) VALUES (?, ?, ?, ?)";
      jdbcTemplate.update(sql, room.getFloorId(), room.getHotel().getId(),
          room.getStatus().toString(), 0);
    } else {
      log.debug("Updating room: {}", room.getId());
      String sql = "UPDATE room SET floor_id = ?, hotel_id = ?, status = ?, version_number = ? WHERE id = ?";
      jdbcTemplate.update(sql, room.getFloorId(), room.getHotel().getId(),
          room.getStatus().toString(), room.getVersionNumber(), room.getId());
    }
    return room;
  }

  @Override
  public Optional<Room> findById(Integer id) {
    log.debug("Finding room by id: {}", id);
    String sql = "SELECT * FROM room WHERE id = ? FOR UPDATE";
    List<Room> rooms = jdbcTemplate.query(sql, roomRowMapper(), id);
    return rooms.isEmpty() ? Optional.empty() : Optional.of(rooms.get(0));
  }

  @Override
  public List<Room> findAll() {
    log.debug("Finding all rooms");
    String sql = "SELECT * FROM room";
    return jdbcTemplate.query(sql, roomRowMapper());
  }

  @Override
  public Page<Room> findAll(Pageable pageable) {
    log.debug("Finding all rooms with pagination");
    String sql = "SELECT * FROM room ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Room> rooms = jdbcTemplate.query(sql, roomRowMapper(), pageable.getPageSize(),
        pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM room";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class);

    return new PageImpl<>(rooms, pageable, total);
  }

  public Optional<Room> findByIdAndStatus(int id, RoomStatus status) {
    log.debug("Finding room by id: {} and status: {} (with pessimistic write lock)", id, status);
    String sql = "SELECT * FROM room WHERE id = ? AND status = ? FOR UPDATE";
    List<Room> rooms = jdbcTemplate.query(sql, roomRowMapper(), id, status.toString());
    return rooms.isEmpty() ? Optional.empty() : Optional.of(rooms.get(0));
  }

  public List<Room> findByStatus(RoomStatus status) {
    log.debug("Finding rooms by status: {}", status);
    String sql = "SELECT * FROM room WHERE status = ?";
    return jdbcTemplate.query(sql, roomRowMapper(), status.toString());
  }

  public Optional<Room> findByIdWithPessimisticLock(Integer id) {
    log.debug("Finding room by id: {} (with pessimistic write lock)", id);
    String sql = "SELECT * FROM room WHERE id = ? FOR UPDATE";
    List<Room> rooms = jdbcTemplate.query(sql, roomRowMapper(), id);
    return rooms.isEmpty() ? Optional.empty() : Optional.of(rooms.get(0));
  }

  @Override
  public void deleteById(Integer id) {
    log.debug("Deleting room: {}", id);
    String sql = "DELETE FROM room WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public long count() {
    String sql = "SELECT COUNT(*) FROM room";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteAll() {
    log.debug("Deleting all rooms");
    String sql = "DELETE FROM room";
    jdbcTemplate.update(sql);
  }
}
