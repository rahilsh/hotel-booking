package in.rsh.hotel.booking.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseRepository<T, ID> {
  T save(T entity);
  Optional<T> findById(ID id);
  List<T> findAll();
  Page<T> findAll(Pageable pageable);
  void deleteById(ID id);
  long count();
}
