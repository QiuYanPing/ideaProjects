package pojo;

public class Room {
    private String id;
    private String type;
    private int price;

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

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }

    public Room(String id, String type, int price) {
        this.id = id;
        this.type = type;
        this.price = price;
    }
}
