package in.rsh.hotel.booking;

import static in.rsh.hotel.booking.model.Room.RoomStatus.AVAILABLE;

import in.rsh.hotel.booking.model.Hotel;
import in.rsh.hotel.booking.model.Room;
import in.rsh.hotel.booking.repository.HotelRepository;
import in.rsh.hotel.booking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class H2Bootstrap implements CommandLineRunner {

  private final RoomRepository roomRepository;
  private final HotelRepository hotelRepository;

  @Autowired
  public H2Bootstrap(RoomRepository roomRepository, HotelRepository hotelRepository) {
    this.roomRepository = roomRepository;
    this.hotelRepository = hotelRepository;
  }

  @Override
  public void run(String... args) {

    System.out.println("Bootstrapping data: ");
    Hotel hotel = new Hotel("Ibis", "Mumbai");
    hotelRepository.save(hotel);

    roomRepository.save(new Room(1, hotel, AVAILABLE));
    roomRepository.save(new Room(2, hotel, AVAILABLE));
    roomRepository.save(new Room(3, hotel, AVAILABLE));

    Iterable<Room> itr = roomRepository.findAll();

    System.out.println("Printing out data: ");
    for (Room room : itr) {
      System.out.println(room.getId());
    }
  }
}
