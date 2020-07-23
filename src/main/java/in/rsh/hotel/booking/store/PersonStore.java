package in.rsh.hotel.booking.store;

import com.google.inject.Singleton;
import in.rsh.hotel.booking.model.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Use in-memory DB
@Singleton
public class PersonStore {

  static Map<Integer, Person> persons = new HashMap<>();

  public void add(Person person) {
    persons.put(person.getId(), person);
  }

  public void remove(Person person) {
    persons.remove(person.getId());
  }

  public Person get(Integer personId) {
    return persons.get(personId);
  }

  public List<Person> getAll() {
    return new ArrayList<>(persons.values());
  }

  public void addMany(List<Person> persons) {
    persons.forEach(this::add );
  }
}
