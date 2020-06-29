package in.rsh.hotel.booking.model;

public class Room {
  private int id;
  private int floorId;
  private Status status;

  public Room(int id, int floorId, Status status) {
    this.id = id;
    this.floorId = floorId;
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getFloorId() {
    return floorId;
  }

  public void setFloorId(int floorId) {
    this.floorId = floorId;
  }
}
