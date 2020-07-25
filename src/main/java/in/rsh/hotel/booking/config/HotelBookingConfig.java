package in.rsh.hotel.booking.config;

import in.rsh.hotel.booking.strategy.BookingStrategy;
import in.rsh.hotel.booking.strategy.TopToBottomStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelBookingConfig {

  @Bean
  public BookingStrategy provideBookingStrategy() {
    return new TopToBottomStrategy();
  }
}
