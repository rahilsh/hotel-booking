package in.rsh.hotel.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private String error;
  private String message;
  private int status;
  private long timestamp;
}
