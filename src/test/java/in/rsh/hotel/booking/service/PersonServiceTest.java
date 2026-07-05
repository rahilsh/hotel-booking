package in.rsh.hotel.booking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import in.rsh.hotel.booking.exception.ResourceNotFoundException;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.repository.PersonRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @Mock private PersonRepository personRepository;

  private PersonService personService;

  @BeforeEach
  void setUp() {
    personService = new PersonService(personRepository);
  }

  @Test
  void testGetAllPersons() {
    Person person1 = new Person();
    person1.setId(1);
    person1.setName("John");
    person1.setAge(25);
    person1.setEmailId("john@example.com");

    Person person2 = new Person();
    person2.setId(2);
    person2.setName("Jane");
    person2.setAge(30);
    person2.setEmailId("jane@example.com");

    List<Person> persons = Arrays.asList(person1, person2);
    when(personRepository.findAll()).thenReturn(persons);

    Iterable<Person> result = personService.getAllPersons();

    assertNotNull(result);
    verify(personRepository, times(1)).findAll();
  }

  @Test
  void testGetPersonById_Success() {
    Person person = new Person();
    person.setId(1);
    person.setName("John");
    person.setAge(25);
    person.setEmailId("john@example.com");

    when(personRepository.findById(1)).thenReturn(Optional.of(person));

    Person result = personService.getPersonById(1);

    assertNotNull(result);
    assertEquals("John", result.getName());
    assertEquals(25, result.getAge());
    verify(personRepository, times(1)).findById(1);
  }

  @Test
  void testGetPersonById_NotFound() {
    when(personRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> personService.getPersonById(999));
    verify(personRepository, times(1)).findById(999);
  }

  @Test
  void testSaveOrUpdate() {
    Person person = new Person();
    person.setId(1);
    person.setName("John");
    person.setAge(25);
    person.setEmailId("john@example.com");

    when(personRepository.save(any(Person.class))).thenReturn(person);

    Person result = personService.saveOrUpdate(person);

    assertNotNull(result);
    assertEquals("John", result.getName());
    verify(personRepository, times(1)).save(person);
  }

  @Test
  void testDelete() {
    personService.delete(1);
    verify(personRepository, times(1)).deleteById(1);
  }
}
