package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Person;
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
public class PersonJdbcRepository implements BaseRepository<Person, Integer> {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PersonJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Person save(Person person) {
    if (person.getId() == 0) {
      log.debug("Inserting new person: {}", person.getEmailId());
      String sql = "INSERT INTO person (name, age, email_id) VALUES (?, ?, ?)";
      jdbcTemplate.update(sql, person.getName(), person.getAge(), person.getEmailId());
    } else {
      log.debug("Updating person: {}", person.getId());
      String sql = "UPDATE person SET name = ?, age = ?, email_id = ? WHERE id = ?";
      jdbcTemplate.update(sql, person.getName(), person.getAge(), person.getEmailId(),
          person.getId());
    }
    return person;
  }

  @Override
  public Optional<Person> findById(Integer id) {
    log.debug("Finding person by id: {}", id);
    String sql = "SELECT * FROM person WHERE id = ?";
    List<Person> persons =
        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Person.class), id);
    return persons.isEmpty() ? Optional.empty() : Optional.of(persons.get(0));
  }

  @Override
  public List<Person> findAll() {
    log.debug("Finding all persons");
    String sql = "SELECT * FROM person";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Person.class));
  }

  @Override
  public Page<Person> findAll(Pageable pageable) {
    log.debug("Finding all persons with pagination: page={}, size={}", pageable.getPageNumber(),
        pageable.getPageSize());
    String sql = "SELECT * FROM person ORDER BY id ASC LIMIT ? OFFSET ?";
    List<Person> persons = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Person.class),
        pageable.getPageSize(), pageable.getOffset());

    String countSql = "SELECT COUNT(*) FROM person";
    Long total = jdbcTemplate.queryForObject(countSql, Long.class);

    return new PageImpl<>(persons, pageable, total);
  }

  @Override
  public void deleteById(Integer id) {
    log.debug("Deleting person: {}", id);
    String sql = "DELETE FROM person WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public long count() {
    String sql = "SELECT COUNT(*) FROM person";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteAll() {
    log.debug("Deleting all persons");
    String sql = "DELETE FROM person";
    jdbcTemplate.update(sql);
  }
}
