package in.rsh.hotel.booking.model;

public class Floor {
  private int floorNo;
  private int hotelId;

  public Floor(int floorNo, int hotelId) {
    this.floorNo = floorNo;
    this.hotelId = hotelId;
  }

  public int getFloorNo() {
    return floorNo;
  }

  public void setFloorNo(int floorNo) {
    this.floorNo = floorNo;
  }

  public int getHotelId() {
    return hotelId;
  }

  public void setHotelId(int hotelId) {
    this.hotelId = hotelId;
  }
}
