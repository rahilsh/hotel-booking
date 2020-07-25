package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.service.PersonService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/persons")
  public List<Person> getAllPersons() {
    return personService.getAllPersons();
  }

  @GetMapping("/persons/{id}")
  public Person getPerson(@PathVariable("id") int id) {
    return personService.getPersonById(id);
  }

  @DeleteMapping("/persons/{id}")
  public void deletePerson(@PathVariable("id") int id) {
    personService.delete(id);
  }

  @PostMapping("/persons")
  public int savePerson(@RequestBody Person person) {
    personService.saveOrUpdate(person);
    return person.getId();
  }
}
