package service;

import data.EmployeeDatas;
import pojo.Employee;
import pojo.Scheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ManagerService extends PersonService{
    private Scanner scanner=new Scanner(System.in);
    public void addEmployee(){
        String employeeId;
        int index=-1;
        while (true){
            System.out.println("输入员工编号：");
            employeeId=scanner.next();
            index=employeeDatas.findEmployee(employeeId);
            if (index<0)
                break;
            System.out.println("员工"+employeeId+"已在岗，请重新输入");
        }
        System.out.println("登录密码:");
        String password=scanner.next();
        System.out.println("员工名字：");
        String employeeName=scanner.next();
        System.out.println("员工电话：");
        String employeePhone=scanner.next();
        System.out.println("员工性别：");
        String employeeSex=scanner.next();
        System.out.println("员工邮箱：");
        String employeePost=scanner.next();
        Employee employee=new Employee(employeeId,password,employeeName,employeePhone,employeeSex,employeePost);
        employeeDatas.addEmployee(employee);
        System.out.println("员工："+employee.getId()+"添加成功！");
        change=true;
    }
    public void removeEmployee(){
        if (employees.size()==1){
            return;
        }
        String id;
        int index=-1;
        while (true){
            System.out.println("员工编号：");
            id=scanner.next();
            index=employeeDatas.findEmployee(id);
            if (index>=0)
                break;
            System.out.println("员工"+id+"不存在，请重新输入");
        }
        Employee employee=employees.get(index);
        employeeDatas.removeEmployee(employee);
        System.out.println("员工："+employee.getId()+"删除成功！");
        change=true;
    }

    public void showEmployees(){
        System.out.println("\n 编号\t\t 密码\t\t 名字\t\t 电话\t\t 性别\t\t 邮箱\n");
        for (int i=0;i<employees.size();i++){
            Employee employee=employees.get(i);
            System.out.println(employee.getId()+"\t\t"+employee.getPassword()+"\t\t"+employee.getName()+"\t\t"+employee.getPhone()+"\t\t"+employee.getGender()+"\t\t"+employee.getPost());
        }
    }

    public void setScheduling() throws ParseException {
        String employeeId;
        Date workTime;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        System.out.println("输入员工编号：");
        employeeId=scanner.next();
        int index = EmployeeDatas.findEmployee(employeeId);
        if(index<0){
            System.out.println("员工{"+employeeId+"}不存在！");
            return;
        }
        System.out.println("输入员工工作时间：");
        scanner.nextLine();//读取换行符
        workTime=sdf.parse(scanner.nextLine());
        schedulings.add(new Scheduling(employeeId,workTime));
        System.out.println("排班成功！");
        change = true;
    }

    /*@Override
    public void saveDatas() {
        if(!change) return ;
        System.out.println("员工信息已经被修改，是否保存？(Y/N)");
        String yn = scanner.next();
        if (yn.equalsIgnoreCase("Y")) {
            employeeDatas.saveEmployees();
        }
        change = false;
    }*/

}
