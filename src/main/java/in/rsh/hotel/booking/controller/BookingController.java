package in.rsh.hotel.booking.controller;

import in.rsh.hotel.booking.dto.BookingRequest;
import in.rsh.hotel.booking.dto.BookingResponse;
import in.rsh.hotel.booking.dto.UpdateBookingStatusRequest;
import in.rsh.hotel.booking.model.Booking;
import in.rsh.hotel.booking.service.BookingService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/bookings")
public class BookingController {

  private final BookingService bookingService;

  @Autowired
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping
  public ResponseEntity<?> getAllBookings(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") Sort.Direction direction,
      @RequestParam(required = false) Integer personId,
      @RequestParam(required = false) Integer roomId) {
    var pageable = PageRequest.of(page, size, direction, sortBy);
    var bookings = bookingService.getAllBookings(pageable, personId, roomId);
    var responses = bookings.stream().map(this::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookingResponse> getBooking(@PathVariable("id") int id) {
    Booking booking = bookingService.getBookingById(id);
    return ResponseEntity.ok(toResponse(booking));
  }

  @PostMapping
  public ResponseEntity<BookingResponse> createBooking(
      @Valid @RequestBody BookingRequest request) {
    request.validate();
    Booking booking;
    if (request.getRoomId() > 0) {
      booking =
          bookingService.bookRoomByRoomId(
              request.getPersonId(),
              request.getRoomId(),
              request.getStartTime(),
              request.getEndTime());
    } else {
      booking =
          bookingService.bookRoomByStrategy(
              request.getPersonId(), request.getStartTime(), request.getEndTime());
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(booking));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BookingResponse> updateStatus(
      @PathVariable("id") int id, @Valid @RequestBody UpdateBookingStatusRequest request) {
    Booking booking = bookingService.updateBookingStatus(id, request.getStatus());
    return ResponseEntity.ok(toResponse(booking));
  }

  private BookingResponse toResponse(Booking booking) {
    return new BookingResponse(
        booking.getId(),
        booking.getPerson().getId(),
        booking.getRoom().getId(),
        booking.getStartTime(),
        booking.getEndTime(),
        booking.getStatus());
  }
}
