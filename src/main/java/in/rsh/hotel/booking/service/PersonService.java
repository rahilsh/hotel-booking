package in.rsh.hotel.booking.service;

import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonRepository personRepository;

  @Autowired
  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public List<Person> getAllPersons() {
    List<Person> persons = new ArrayList<>();
    personRepository.findAll().forEach(persons::add);
    return persons;
  }

  public Person getPersonById(int id) {

    final Optional<Person> optionalPerson = personRepository.findById(id);
    if (!optionalPerson.isPresent()) {
      throw new IllegalArgumentException();
    }
    return optionalPerson.get();
  }

  public void saveOrUpdate(Person person) {
    personRepository.save(person);
  }

  public void delete(int id) {
    personRepository.deleteById(id);
  }
}
