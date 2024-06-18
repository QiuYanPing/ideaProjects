import data.OrderDatas;
import data.RoomDatas;
import data.UserDatas;
import pojo.Order;
import pojo.Room;
import pojo.User;
import service.EmployeeService;
import service.ManagerService;
import service.PersonService;
import service.UserService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Action {
    private Scanner scanner = new Scanner(System.in);
    private Menu menu;

    private ManagerService managerService = new ManagerService();
    private PersonService personService = new PersonService();
    private EmployeeService employeeService = new EmployeeService();
    private UserService userService = new UserService();
    private User user; // 当前登录用户


    public void start() throws ParseException {
        while (true) {
            if (user == null) {
                // 显示登录前菜单
                menu.preLoginMenu();
                int item = scanner.nextInt();
                /*scanner.nextLine();*/ // 消耗换行符
                if (item == 1) {
                    personService.getDatas(); // 获取数据
                    boolean pass = personService.login(); // 用户登录
                    if (!pass)
                        continue;
                    User user = personService.users.get(personService.getCurPersonIndex());
                    int role = personService.x;
                    if (role == 1) { //管理员
                        managerAction();
                    } else if (role == 2) { // 客户
                        customerAction();
                    } else { // 员工
                        employeeAction();
                    }
                } else if (item == 2) {
                    personService.exit();
                    break;
                }
            }
        }
    }

    public void managerAction() throws ParseException {
        while (true) {
            Menu.displayAdminMenu(); // 显示系统管理员菜单项
            int item = scanner.nextInt();
            switch (item) {
                case 1:
                    personService.modifyPassword(); // 修改密码
                    break;
                case 2:
                    managerService.addEmployee(); // 添加员工
                    break;
                case 3:
                    managerService.removeEmployee(); // 移除员工
                    break;
                case 4:
                    managerService.setScheduling();
                    break;
                case 5:
                    managerService.showEmployees(); // 显示员工列表
                    break;
                case 6:
                    managerService.logout(); // 注销
                    return;
                case 7:
                    managerService.exit(); // 退出
                    break;
            }

        }
    }

    public void customerAction() throws ParseException {
        while (true) {
            Menu.displayCustomerMenu();
            int item = scanner.nextInt();
            User user = UserDatas.getUsers().get(personService.getCurPersonIndex());
            switch (item) {
                case 1:
                    personService.modifyPassword(); // 修改密码
                    break;

                case 2:
                    userService.bookRoom();//预定房间
                    break;
                case 3:
                    userService.showMyRoom(user.getId());
                    break;
                case 4:
                    userService.checkoutRoom(); // 退房
                    break;
                case 5:
                    personService.logout(); // 注销
                    return;
                case 6:
                    personService.exit();//预定房间
                    break;
            }
        }
    }

    public void employeeAction() throws ParseException {
        while (true) {
            Menu.displayEmployeeMenu();
            int item = scanner.nextInt();
            switch (item) {
                case 1:
                    personService.modifyPassword(); // 修改密码
                    break;
                case 2:
                    employeeService.showScheduling();//查看值班表
                    break;
                case 3:
                    userService.addUser();
                    break;
                case 4:
                    userService.bookRoom();
                    break;
                case 5:
                    userService.checkoutRoom();
                    break;
                case 6:
                    userService.showOrders();
                    break;
                case 7:
                    userService.showRooms();
                    break;
                case 8:
                    personService.logout();
                    return;
                case 9:
                    personService.exit(); // 退出
                    break;
            }
        }
    }
}
