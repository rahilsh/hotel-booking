package in.rsh.hotel.booking.repository;

import in.rsh.hotel.booking.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepository extends CrudRepository<Person, Integer>, PagingAndSortingRepository<Person, Integer> {}
