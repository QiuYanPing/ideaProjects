package service;

import data.RoomDatas;
import data.OrderDatas;
import data.UserDatas;
import pojo.Room;
import pojo.User;
import pojo.Order;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

//import static service.PersonService.users;

public class UserService extends PersonService{
    private Scanner scanner=new Scanner(System.in);
    public void addUser(){
        String userId;
        int index=-1;
        while(true){
            System.out.println("输入顾客编号：");
            userId=scanner.next();
            index= UserDatas.findUser(userId);
            if(index<0){
                break;
            }
            System.out.println("顾客"+userId+"已登入，请重新输入");
        }
        System.out.println("登录密码:");
        String password=scanner.next();
        System.out.println("顾客名字：");
        String userName=scanner.next();
        System.out.println("顾客电话：");
        String userPhone=scanner.next();
        User user=new User(userId,password,userName,userPhone);
        UserDatas.addUser(user);
        System.out.println("客户信息录入成功！");
        change = true;
    }
    public void removeUser(){
        if (users.size()==1){
            return;
        }
        String id;
        int index=-1;
        while (true){
            System.out.println("顾客编号：");
            id=scanner.next();
            index=UserDatas.findUser(id);
            if (index>=0)
                break;
            System.out.println("顾客"+id+"不存在，请重新输入");
        }
        User user=users.get(index);
        users.remove(user);
        change = true;
    }
    public void showUsers(){
        System.out.println("\n 编号\t\t 密码\t\t 名字\t\t 电话\n");
        for (int i=0;i<users.size();i++){
            User user=users.get(i);
            System.out.println(user.getId()+"\t\t"+user.getPassword()+"\t\t"+user.getName()+"\t\t"+user.getPhone());
        }
    }
    /*@Override
    public void saveDatas() {
        if(!change) return ;
        System.out.println("员工信息已经被修改，是否保存？(Y/N)");
        String yn = scanner.next();
        if (yn.equalsIgnoreCase("Y")) {
            UserDatas.saveUsers();
        }
        change = false;
    }*/
    public void bookRoom() {
        System.out.println("输入顾客编号：");
        String userId = scanner.next();
        int userIndex = UserDatas.findUser(userId);
        if (userIndex < 0) {
            System.out.println("顾客不存在！");
            return;
        }

        System.out.println("输入房间编号：");
        String roomId = scanner.next();
        int roomIndex = RoomDatas.findRoom(roomId);
        if (roomIndex < 0) {
            System.out.println("房间不存在！");
            return;
        }

        Room room = RoomDatas.getRooms().get(roomIndex);
        if (room.isBooked()) {
            System.out.println("房间已被预定！");
            return;
        }

        System.out.println("输入入住日期 (yyyy-MM-dd HH:mm:ss)：");
        String next = scanner.nextLine();//读取换行符
        String checkInDateStr = scanner.nextLine();
        Date checkInDate;
        try {
            checkInDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(checkInDateStr);
        } catch (ParseException e) {
            System.out.println("日期格式错误！");
            return;
        }

        System.out.println("输入退房日期 (yyyy-MM-dd HH:mm:ss)：");
        /*scanner.nextLine();*/
        String checkOutDateStr = scanner.nextLine();
        Date checkOutDate;
        try {
            checkOutDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(checkOutDateStr);
        } catch (ParseException e) {
            System.out.println("日期格式错误！");
            return;
        }

        // 创建订单
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(userId, roomId, checkInDate, checkOutDate);
        OrderDatas.addOrder(order);

        // 更新房间状态
        RoomDatas.bookRoom(roomId, userId);

        System.out.println("房间预定成功！");
    }

    public void checkoutRoom() {
        System.out.println("输入顾客编号：");
        String userId = scanner.next();
        int userIndex = UserDatas.findUser(userId);
        if (userIndex < 0) {
            System.out.println("顾客不存在！");
            return;
        }

        System.out.println("输入房间编号：");
        String roomId = scanner.next();
        int roomIndex = RoomDatas.findRoom(roomId);
        if (roomIndex < 0) {
            System.out.println("房间不存在！");
            return;
        }

        Room room = RoomDatas.getRooms().get(roomIndex);
        if (!room.isBooked() || !userId.equals(room.getBookedBy())) {
            System.out.println("该顾客没有预订此房间！");
            return;
        }

        // 查找并删除订单
        ArrayList<Order> orders;
        try {
            orders = OrderDatas.getOrders();
        } catch (ParseException e) {
            System.out.println("读取订单数据失败！");
            return;
        }

        Order orderToRemove = null;
        for (Order order : orders) {
            if (order.getUserId().equals(userId) && order.getRoomId().equals(roomId)) {
                orderToRemove = order;
                break;
            }
        }
        //结算
        long enterTime = orderToRemove.getEnterTime().getTime();
        long leaveTime = orderToRemove.getLeaveTime().getTime();
        long time = leaveTime - enterTime;
        int day = (int) (time / 1000 * 60 * 60 * 24);
        int money = 0;
        if(day == 0){
            day =1;
        }
        money = day*room.getPrice();

        if (orderToRemove != null) {
            OrderDatas.removeOrder(orderToRemove);
        }

        // 更新房间状态
        RoomDatas.checkoutRoom(roomId);



        System.out.println("退房成功！");
        System.out.println("住房费用："+money+"元（不满一天，按一天计算）");
    }

    public void showMyRoom(String userId) throws ParseException {
        ArrayList<Order> orders = OrderDatas.getOrders();
        for (Order order:orders) {
            if(order.getUserId().equals(userId)){
                //找到房间
                String roomId = order.getRoomId();
                int index = RoomDatas.findRoom(roomId);
                Room room = RoomDatas.getRooms().get(index);
                System.out.println("房间号\t\t房间类型\t\t价格");
                System.out.println(room.getId()+"\t\t"+room.getType()+"\t\t"+room.getPrice());
                return;
            }
        }
    }
    public void showRooms() throws ParseException {
        ArrayList<Room> rooms = RoomDatas.getRooms();
        System.out.println("房间号\t\t房间类型\t\t价格\t\t是否被预定\t住房客户");
        for (Room room: rooms) {
            System.out.println(room.getId()+"\t\t"+room.getType()+"\t\t"+room.getPrice()+"\t\t"+room.isBooked()+"\t\t"+room.getBookedBy());
        }
    }

    public void showOrders() throws ParseException {
        ArrayList<Order> orders = OrderDatas.getOrders();
        System.out.println("客户号\t\t房间号\t\t入住时间\t\t\t\t离开时间");
        for (Order order: orders) {
            System.out.println(order.getUserId()+"\t\t"+order.getRoomId()+"\t\t"+sdf.format(order.getEnterTime())+"\t\t"+sdf.format(order.getLeaveTime()));
        }
    }
}
