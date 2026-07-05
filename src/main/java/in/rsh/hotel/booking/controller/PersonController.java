package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.dto.PersonRequest;
import in.rsh.hotel.booking.dto.PersonResponse;
import in.rsh.hotel.booking.model.Person;
import in.rsh.hotel.booking.service.PersonService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping
  public ResponseEntity<?> getAllPersons(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
    var pageable = PageRequest.of(page, size, direction, sortBy);
    var persons = personService.getAllPersons(pageable);
    var responses = persons.stream().map(this::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonResponse> getPerson(@PathVariable("id") int id) {
    Person person = personService.getPersonById(id);
    return ResponseEntity.ok(toResponse(person));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePerson(@PathVariable("id") int id) {
    personService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<PersonResponse> savePerson(@Valid @RequestBody PersonRequest request) {
    Person person = fromRequest(request);
    Person savedPerson = personService.saveOrUpdate(person);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedPerson));
  }

  private PersonResponse toResponse(Person person) {
    return new PersonResponse(person.getId(), person.getName(), person.getAge(),
        person.getEmailId());
  }

  private Person fromRequest(PersonRequest request) {
    Person person = new Person();
    person.setName(request.getName());
    person.setAge(request.getAge());
    person.setEmailId(request.getEmailId());
    return person;
  }
}
