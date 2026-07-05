package in.rsh.hotel.booking.exception;

public class RoomNotAvailableException extends RuntimeException {

  public RoomNotAvailableException(String message) {
    super(message);
  }

  public RoomNotAvailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
