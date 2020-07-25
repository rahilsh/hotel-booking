package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {}
