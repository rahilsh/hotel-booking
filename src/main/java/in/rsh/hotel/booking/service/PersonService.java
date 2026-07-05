package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.repository.PersonJdbcRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonService {

  private final PersonJdbcRepository personRepository;

  @Autowired
  public PersonService(PersonJdbcRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Iterable<Person> getAllPersons() {
    log.debug("Fetching all persons");
    return personRepository.findAll();
  }

  public Page<Person> getAllPersons(Pageable pageable) {
    log.debug("Fetching persons with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
    return personRepository.findAll(pageable);
  }

  public Person getPersonById(int id) {
    log.debug("Fetching person with id: {}", id);
    final Optional<Person> optionalPerson = personRepository.findById(id);
    if (optionalPerson.isEmpty()) {
      log.warn("Person not found with id: {}", id);
      throw new ResourceNotFoundException("Person not found with id: " + id);
    }
    return optionalPerson.get();
  }

  public Person saveOrUpdate(Person person) {
    log.debug("Saving or updating person: {}", person.getId());
    return personRepository.save(person);
  }

  public void delete(int id) {
    log.debug("Deleting person with id: {}", id);
    personRepository.deleteById(id);
  }
}
