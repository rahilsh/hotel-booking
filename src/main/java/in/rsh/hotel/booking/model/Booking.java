package in.rsh.hotel.booking.model;

public class Booking {

  private String id;
  private int personId;
  private int roomId;
  private long startTime;
  private long endTime;
  private BookingStatus status;

  public Booking(String id, int personId, int roomId, long startTime, long endTime) {
    this.id = id;
    this.personId = personId;
    this.roomId = roomId;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BookingStatus getStatus() {
    return status;
  }

  public void setStatus(BookingStatus status) {
    this.status = status;
  }

  public int getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
}
