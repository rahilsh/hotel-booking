package in.rsh.hotel.booking.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import in.rsh.hotel.booking.strategy.BookingStrategy;
import in.rsh.hotel.booking.strategy.TopToBottomStrategy;

public class HotelBookingModule extends AbstractModule {

  @Provides
  @Singleton
  public BookingStrategy provideDefaultStrategy() {
    return new TopToBottomStrategy();
  }
}
