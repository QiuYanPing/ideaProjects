package service;

import pojo.Employee;
import pojo.Scheduling;

import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public class ManagerService extends PersonService{
    public boolean delete = false;
    Scanner scanner = new Scanner(System.in);
    public void addEmployee(){
        int index = -1;
        String id = null;
        while (true) {
            System.out.println("员工id：");
            id = scanner.next();
            index = employeeDatas.findEmployee(id);
            if(index < 0 ){
                break;
            }
            System.out.println("员工["+id+"]已存在，请重新输入");
        }
        System.out.println("登录密码：");
        String password = scanner.next();
        System.out.println("姓名：");
        String name = scanner.next();
        System.out.println("联系方式：");
        String phone = scanner.next();
        System.out.println("性别：");
        String gender = scanner.next();
        System.out.println("职位：");
        String post = scanner.next();
        Employee employee = new Employee(id, password, name, phone, gender, post);
        employees.add(employee);
        change = true;

    }
    public void removeEmployee(){
        String id;
        int index = -1;
        while(true){
            System.out.println("员工id:");
            id = scanner.next();
            index = employeeDatas.findEmployee(id);
            if(index >= 0)
                break;
            System.out.println("员工["+id+"]不存在，请重新输入");
        }
        Employee employee = employees.get(index);
        employeeDatas.remove(employee);
        change = true;
        //删除员工时，删除相应的排班表
        int schedulingIndex = schedulingDatas.findScheduling(id);
        schedulingDatas.remove(schedulings.get(schedulingIndex));
        schedulingDatas.saveSchedulings();
        delete = true;
    }
    public void setScheduling() throws ParseException {
        int index = -1;
        String id = null;
        while(true){
            System.out.println("员工id:");
            id = scanner.next();
            index = employeeDatas.findEmployee(id);
            if(index >= 0)
                break;
            System.out.println("员工["+id+"]不存在，请重新输入");
        }
        System.out.println("工作时间：");
        String time = scanner.next();
        Date workTime = sdf.parse(time);
        Scheduling scheduling = new Scheduling(id, workTime);
        schedulingDatas.addScheduling(scheduling);
        schedulingDatas.saveSchedulings();
    }
    public void selectEmployee(){
        for (int i = 0; i < employees.size(); i++) {
            System.out.println(employees.get(i));
        }
    }
    @Override
    public void saveDatas(){
        if(change || delete){
            employeeDatas.saveEmployees();
        }

    }
}
