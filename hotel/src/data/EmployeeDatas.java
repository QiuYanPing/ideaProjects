package data;

import file.EmployeeFile;
import pojo.Employee;

import java.util.ArrayList;

public class EmployeeDatas {
    private static ArrayList<Employee> employees=null;
    public static int findEmployee(String id){
        if (employees==null)
            return -1;
        if (id==null)
            return -1;
        int index=-1;
        for(int i=0; i<employees.size(); i++) { //二分查找
            if(id.equals(employees.get(i).getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
    }

    public void modifyEmployee(Employee employee){
        int index=findEmployee(employee.getId());
        employees.set(index,employee);
    }

    public void removeEmployee(Employee employee){
        int index=findEmployee(employee.getId());
        employees.remove(index);
    }

    public void saveEmployees(){
        EmployeeFile file=new EmployeeFile();
        file.save(employees);
    }

    public static ArrayList<Employee> getEmployees(){
        if (employees==null){
            EmployeeFile file=new EmployeeFile();
            employees=file.acquire();
        }
        return employees;
    }
    public static void clear(){
        employees=null;
    }
}
