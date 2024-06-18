import file.*;
import pojo.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws ParseException {
        //系统首次运行时进行一次初始化，后续不要再重复运行此函数
        //init();
        Action action = new Action();
        action.start();
    }
    // 首次使用系统时初始化数据
    public static void init() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = new User("user01", "user01", "张三", "111");
        Manager manager = new Manager("manager01","李四","manager01");
        Employee employee = new Employee("employee01","employee01","王五","1111","男","123@qq.com");
        Room room1 = new Room("room01","单人间",128,true,"user01");
        room1.setBooked(true);
        room1.setBookedBy("user01");
        Room room2 = new Room("room02","单人间",128,false,"null");
        Room room3 = new Room("room03","单人间",128,false,"null");
        Room room4 = new Room("room04","单人间",128,false,"null");
        Room room5 = new Room("room05","双人房",218,false,"null");
        Room room6 = new Room("room06","双人房",218,false,"null");
        Room room7 = new Room("room07","双人房",218,false,"null");
        Room room8 = new Room("room08","双人房",218,false,"null");




        Order order = new Order("user01","room01",sdf.parse("2024-05-16 00:00:00"),sdf.parse("2024-05-16 00:00:00"));
        Scheduling scheduling = new Scheduling("employee01",new Date(System.currentTimeMillis()));
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        UserFile userFile = new UserFile();
        userFile.save(users);

        ArrayList<Manager> managers = new ArrayList<>();
        managers.add(manager);
        ManagerFile managerFile = new ManagerFile();
        managerFile.save(managers);

        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(employee);
        EmployeeFile employeeFile = new EmployeeFile();
        employeeFile.save(employees);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);
        rooms.add(room5);
        rooms.add(room6);
        rooms.add(room7);
        rooms.add(room8);


        RoomFile roomFile = new RoomFile();
        roomFile.save(rooms);

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        OrderFile orderFile = new OrderFile();
        orderFile.save(orders);

        ArrayList<Scheduling> schedulings = new ArrayList<>();
        schedulings.add(scheduling);
        SchedulingFile schedulingFile = new SchedulingFile();
        schedulingFile.save(schedulings);
    }
}