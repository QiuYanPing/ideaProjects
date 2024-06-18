package pojo;

import java.util.Date;

public class Order {
    private String UserId;
    private String RoomId;
    private Date enterTime;
    private Date leaveTime;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "UserId='" + UserId + '\'' +
                ", RoomId='" + RoomId + '\'' +
                ", enterTime=" + enterTime +
                ", leaveTime=" + leaveTime +
                '}';
    }

    public Order(String userId, String roomId, Date enterTime, Date leaveTime) {
        UserId = userId;
        RoomId = roomId;
        this.enterTime = enterTime;
        this.leaveTime = leaveTime;
    }

    public Order(String userId, String roomId, Date enterTime) {
        UserId = userId;
        RoomId = roomId;
        this.enterTime = enterTime;
    }
}
