import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    // 登录前菜单
    public static void preLoginMenu() {
        System.out.println("\n\n\t1. 登录");
        System.out.println("\t2. 退出");
        System.out.println("\n\t 请选择您的操作：");
    }

    public static void displayAdminMenu() {
        System.out.println("管理员菜单:");
        System.out.println("1. 更改密码");
        System.out.println("2. 添加员工");
        System.out.println("3. 移除员工");
        System.out.println("4. 设置排班表");
        System.out.println("5. 显示员工列表");
        System.out.println("6. 注销");
        System.out.println("7.退出程序");
        System.out.print("请输入您的选择 (1-7): ");
    }

    public static void displayEmployeeMenu() {
        System.out.println("员工菜单:");
        System.out.println("1. 更改密码");
        System.out.println("2. 查看值班表");
        System.out.println("———————————  业务  ——————————");
        System.out.println("3. 录入客户信息");
        System.out.println("4. 办理入房");
        System.out.println("5. 办理退房并结算");
        System.out.println("6. 查看所有订单信息");
        System.out.println("7. 查看房间信息");
        System.out.println("————————————————————————————————");
        System.out.println("8. 注销");
        System.out.println("9. 退出");
        System.out.print("请输入您的选择 (1-9): ");
    }

    public static void displayCustomerMenu() {
        System.out.println("客户菜单:");
        System.out.println("1. 更改密码");
        System.out.println("2. 预定房间");
        System.out.println("3. 我的房间");
        System.out.println("4. 退房");
        System.out.println("5. 注销");
        System.out.println("6. 退出");
        System.out.print("请输入您的选择 (1-6): ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }
}