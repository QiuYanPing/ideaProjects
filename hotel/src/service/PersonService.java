package service;

import data.*;
import pojo.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class PersonService {
    public static ArrayList<User> users = null;
    public static ArrayList<Manager> managers = null;
    public static ArrayList<Employee> employees = null;
    public static ArrayList<Room> rooms = null;
    public static ArrayList<Order> orders = null;
    public static ArrayList<Scheduling> schedulings = null;
    public static int x = -1;
    /**
     * x代表
     * 1.管理员
     * 2.客户
     * 3.员工
     */
    public static int curPersonIndex = -1;
    public boolean change = false;
    private Scanner scanner =new Scanner(System.in);
    public UserDatas userDatas = new UserDatas();
    public ManagerDatas managerDatas = new ManagerDatas();
    public EmployeeDatas employeeDatas = new EmployeeDatas();
    public RoomDatas roomDatas = new RoomDatas();
    public OrderDatas orderDatas = new OrderDatas();
    public SchedulingDatas schedulingDatas = new SchedulingDatas();
    public DecimalFormat df = new DecimalFormat("#.00");
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void getDatas() throws ParseException {
        users = UserDatas.getUsers();
        managers = ManagerDatas.getManagers();
        employees = EmployeeDatas.getEmployees();
        rooms = RoomDatas.getRooms();
        orders = OrderDatas.getOrders();
        schedulings = SchedulingDatas.getSchedulings();

    }
    public int getCurPersonIndex(){
        return curPersonIndex;
    }

    //登录
    public boolean login(){
        System.out.println("id:");
        String id = scanner.next();
        System.out.println("password:");
        String password = scanner.next();
        int index = -1;
        if(managerDatas.findManager(id)>=0){
            index = managerDatas.findManager(id);
            x = 1;
        }else if(userDatas.findUser(id)>=0){
            index = userDatas.findUser(id);
            x = 2;
        }else{
            index = employeeDatas.findEmployee(id);
            x = 3;
        }
        if(index < 0){
            System.out.println("用户不存在！");
            return false;

        }
        boolean pass = false;
        switch (x){
            case 1:
                Manager manager = managers.get(index);
                pass = manager.getPassword().equals(password);
                break;
            case 2:
                User user = users.get(index);
                pass = user.getPassword().equals(password);
                break;
            case 3:
                Employee employee = employees.get(index);
                pass = employee.getPassword().equals(password);
                break;
        }
        if(!pass){
            System.out.println("用户密码错误！");
            return false;
        }
        curPersonIndex = index;
        return true;

    }

    //修改密码
    public boolean modifyPassword(){
        String oldPwd = null;
        String newPwd = null;
        String newPwd2 = null;
        switch (x){
            case 1:
                Manager manager = managers.get(curPersonIndex);
                System.out.println("请输入原密码：");
                oldPwd = scanner.next();
                if(!oldPwd.equals(manager.getPassword())) {
                    System.out.println("原密码不正确！");
                    return false;
                }
                System.out.println("请输入新密码：");
                newPwd = scanner.next();
                System.out.println("再次确认密码：");
                newPwd2 = scanner.next();
                if(!newPwd.equals(newPwd2)) {
                    System.out.println("两个输入的密码不同，修改密码失败\n");
                    return false;
                }
                manager.setPassword(newPwd);
                //managers.set(curPersonIndex, manager);
                managerDatas.modifyManager(manager);
                System.out.println("密码修改成功！");
                change = true; // 数据更改
                break;
            case 2:
                User user = users.get(curPersonIndex);
                System.out.println("请输入原密码：");
                oldPwd = scanner.next();
                if(!oldPwd.equals(user.getPassword())) {
                    System.out.println("原密码不正确！");
                    return false;
                }
                System.out.println("请输入新密码：");
                newPwd = scanner.next();
                System.out.println("再次确认密码：");
                newPwd2 = scanner.next();
                if(!newPwd.equals(newPwd2)) {
                    System.out.println("两个输入的密码不同，修改密码失败\n");
                    return false;
                }
                user.setPassword(newPwd);
                //users.set(curPersonIndex, user);
                userDatas.modifyUser(user);
                System.out.println("密码修改成功！");
                change = true; // 数据更改
                break;
            case 3:
                Employee employee = employees.get(curPersonIndex);
                System.out.println("请输入原密码：");
                oldPwd = scanner.next();
                if(!oldPwd.equals(employee.getPassword())) {
                    System.out.println("原密码不正确！");
                    return false;
                }
                System.out.println("请输入新密码：");
                newPwd = scanner.next();
                System.out.println("再次确认密码：");
                newPwd2 = scanner.next();
                if(!newPwd.equals(newPwd2)) {
                    System.out.println("两个输入的密码不同，修改密码失败\n");
                    return false;
                }
                employee.setPassword(newPwd);
                //employees.set(curPersonIndex, employee);
                employeeDatas.modifyEmployee(employee);
                System.out.println("密码修改成功！");
                change = true; // 数据更改
                break;
        }
        return true;
    }
    //注销
    public void logout(){
        saveDatas();
        UserDatas.clear();
        ManagerDatas.clear();
        EmployeeDatas.clear();
        OrderDatas.clear();
        RoomDatas.clear();
        SchedulingDatas.clear();
        curPersonIndex = -1;
    }
    //退出
    public void exit(){
        saveDatas();
        System.exit(0);
    }
    //保存数据
    public void saveDatas(){
        userDatas.saveUsers();
        managerDatas.saveManagers();
        employeeDatas.saveEmployees();
        orderDatas.saveOrders();
        roomDatas.saveRooms();
        schedulingDatas.saveSchedulings();
    }
}
