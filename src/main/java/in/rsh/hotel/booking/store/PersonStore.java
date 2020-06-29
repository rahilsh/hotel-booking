package in.rsh.hotel.booking.store;

import in.rsh.hotel.booking.model.Person;
import java.util.HashMap;
import java.util.Map;

public class PersonStore {

  static Map<Integer, Person> persons = new HashMap<>();

  public void add(Person person) {
    persons.put(person.getId(), person);
  }

  public void remove(Person person) {
    persons.remove(person);
  }

  public Person get(Integer personId) {
    return persons.get(personId);
  }
}
