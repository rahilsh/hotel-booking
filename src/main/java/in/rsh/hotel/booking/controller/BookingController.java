package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.model.Booking.BookingStatus;
import in.rsh.hotel.booking.service.BookingService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

  private final BookingService bookingService;

  @Autowired
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping
  public List<Booking> getAllBookings() {
    return bookingService.getAllBookings();
  }

  @GetMapping("/{id}")
  public Booking getBooking(@PathVariable("id") int id) {
    return bookingService.getBookingById(id);
  }

  @PostMapping
  public Booking createBooking(@RequestBody CreateBookingRequest request) {
    if (request.getRoomId() != 0) {
      return bookingService.bookRoomByRoomId(
          request.getPersonId(), request.getRoomId(), request.getStartTime(), request.getEndTime());
    }
    return bookingService.bookRoomByStrategy(request.getPersonId(), request.getStartTime(), request.getEndTime());
  }

  @PatchMapping("/{id}")
  public Booking updateStatus(
      @PathVariable("id") int id, @RequestBody UpdateBookingStatusRequest request) {
    return bookingService.updateBookingStatus(id, request.getStatus());
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  private static class UpdateBookingStatusRequest {
    private BookingStatus status;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  private static class CreateBookingRequest {
    private int personId;
    private int roomId;
    private long startTime;
    private long endTime;
  }
}
