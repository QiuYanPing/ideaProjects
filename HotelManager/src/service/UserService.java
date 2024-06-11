package service;

import data.OrderDatas;
import data.RoomDatas;
import data.UserDatas;
import pojo.Order;
import pojo.Room;
import pojo.User;

public class UserService extends PersonService{
    public void selectRoom(){
        User user = UserDatas.users.get(curPersonIndex);
        String id = user.getId();


        int orderIndex = orderDatas.findOrder(id);
        Order order = OrderDatas.orders.get(orderIndex);
        String roomId = order.getRoomId();


        int roomIndex = roomDatas.findRoom(roomId);
        Room room = RoomDatas.rooms.get(roomIndex);

        System.out.println("订单信息："+order);
        System.out.println("房间信息："+room);
    }
    public void selectOrder(){
        User user = UserDatas.users.get(curPersonIndex);
        String id = user.getId();
        System.out.println("历史订单：");
        for (int i = 0; i < OrderDatas.orders.size(); i++) {
            Order order = OrderDatas.orders.get(i);
            if(order.getUserId().equals(id) ){
                System.out.println(order);
            }
        }
    }
}
