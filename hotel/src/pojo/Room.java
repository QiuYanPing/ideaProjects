package pojo;

public class Room {
    private String id;
    private String type;
    private int price;
    private boolean booked;
    private String bookedBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }


    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }

    public Room(String id, String type, int price,boolean booked,String bookedBy) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.booked = booked;
        this.bookedBy = bookedBy;
    }
}
