package in.rsh.hotel.booking;

import static in.rsh.hotel.booking.model.Room.RoomStatus.AVAILABLE;

import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.repository.HotelJdbcRepository;
import in.rsh.hotel.booking.repository.RoomJdbcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class H2Bootstrap implements CommandLineRunner {

  private final RoomJdbcRepository roomRepository;
  private final HotelJdbcRepository hotelRepository;
  private final Environment env;

  @Autowired
  public H2Bootstrap(
      RoomJdbcRepository roomRepository,
      HotelJdbcRepository hotelRepository,
      Environment env) {
    this.roomRepository = roomRepository;
    this.hotelRepository = hotelRepository;
    this.env = env;
  }

  @Override
  public void run(String... args) {
    // Skip bootstrapping in test profile
    String[] profiles = env.getActiveProfiles();
    for (String profile : profiles) {
      if ("test".equals(profile)) {
        log.info("Skipping bootstrap in test profile");
        return;
      }
    }

    log.info("Bootstrapping data");
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    hotelRepository.save(hotel);

    roomRepository.save(new Room(1, hotel, AVAILABLE, 0));
    roomRepository.save(new Room(2, hotel, AVAILABLE, 0));
    roomRepository.save(new Room(3, hotel, AVAILABLE, 0));

    log.info("Data bootstrapping complete. Hotel ID: {}", hotel.getId());
  }
}
