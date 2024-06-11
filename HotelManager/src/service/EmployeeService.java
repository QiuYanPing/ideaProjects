package service;

import data.OrderDatas;
import data.RoomDatas;
import data.SchedulingDatas;
import pojo.Employee;
import pojo.Order;
import pojo.Room;
import pojo.Scheduling;

import java.util.Date;
import java.util.Scanner;

public class EmployeeService extends PersonService{
    public boolean changeOrd =false;
    public boolean changeRoom = false;
    Scanner scanner =new Scanner(System.in);
    public void selectScheduling(){
        for (int i = 0; i < SchedulingDatas.schedulings.size(); i++) {
            Scheduling scheduling = SchedulingDatas.schedulings.get(i);
            System.out.println(scheduling);
        }
    }
    public void addOrder(){
        int index = -1;
        String userId = null;
        String roomId = null;
        while (true) {
            System.out.println("客户id：");
            userId = scanner.next();
            index = userDatas.findUser(userId);
            if(index >= 0 ){
                break;
            }
            System.out.println("客户["+userId+"]不存在，请重新输入");
        }
        while (true) {
            System.out.println("房间号：");
            roomId = scanner.next();
            index = roomDatas.findRoom(roomId);
            if(index >= 0 ){
                break;
            }
            System.out.println("房间["+roomId+"]不存在，请重新输入");
        }
        Date enterTime = new Date(System.currentTimeMillis());
        Order order = new Order(userId,roomId,enterTime);
        orderDatas.addOrder(order);
        changeOrd = true;
    }
    public void Leave(){
        //办理退房
        int index = -1;
        String userId = null;
        while (true) {
            System.out.println("客户id：");
            userId = scanner.next();
            index = userDatas.findUser(userId);
            if(index >= 0 ){
                break;
            }
            System.out.println("客户["+userId+"]不存在，请重新输入");
        }
        int orderIndex = orderDatas.findOrder(userId);
        Order order = OrderDatas.orders.get(orderIndex);
        order.setLeaveTime(new Date(System.currentTimeMillis()));


        Date enterTime = order.getEnterTime();
        Date leaveTime = order.getLeaveTime();
        int i = leaveTime.getDay() - enterTime.getDay();


        String roomId = order.getRoomId();
        int roomIndex = roomDatas.findRoom(roomId);
        Room room = RoomDatas.rooms.get(roomIndex);


        System.out.println("居住"+i+"天");
        System.out.println("费用："+i*room.getPrice());

        orderDatas.modifyOrder(order);
        changeOrd = true;
    }
    public void selectOrder(){
        for (int i = 0; i < OrderDatas.orders.size(); i++) {
            Order order = OrderDatas.orders.get(i);
            System.out.println(order);
        }
    }
    public void addRoom(){
        int index = -1;
        String id = null;
        while (true) {
            System.out.println("房间id：");
            id = scanner.next();
            index = roomDatas.findRoom(id);
            if(index < 0 ){
                break;
            }
            System.out.println("房间["+id+"]已存在，请重新输入");
        }
        System.out.println("类型：");
        String type = scanner.next();
        System.out.println("价格：");
        int price = scanner.nextInt();

        Room room = new Room(id,type,price);
        //employees.add(employee);
        roomDatas.addRoom(room);
        changeRoom = true;
    }
    public void removeRoom(){
        int index = -1;
        String id = null;
        while (true) {
            System.out.println("房间id：");
            id = scanner.next();
            index = roomDatas.findRoom(id);
            if(index >= 0 ){
                break;
            }
            System.out.println("房间["+id+"]不存在，请重新输入");
        }

        //employees.add(employee);
        Room room = RoomDatas.rooms.get(index);
        roomDatas.remove(room);
        changeRoom = true;
    }
    public void selectRoom(){
        for (int i = 0; i < RoomDatas.rooms.size(); i++) {
            Room room = RoomDatas.rooms.get(i);
            System.out.println(room);
        }
    }
    @Override
    public void saveDatas(){
        if(changeRoom ){
            roomDatas.saveRooms();
        }
        if(changeOrd){
            orderDatas.saveOrders();
        }
        changeRoom = false;
        changeOrd = false;
    }
}
