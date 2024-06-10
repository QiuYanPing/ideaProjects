package data;

import file.EmployeeFile;
import pojo.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EmployeeDatas {
    public static ArrayList<Employee> employees = null;

    public int findEmployee(String id) {

        /**
         *
         * 二分查找
         */
        Collections.sort(employees, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        int index = -1;
        int left = 0;
        int right = employees.size() - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            int i = id.compareTo(employees.get(mid).getId());
            //找到了
            if(i == 0){
                index = mid;
                break;
            }else if(i < 0 ){
                right = mid -1;
            }else{
                left = mid +1;
            }
        }
        return index;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void modifyEmployee(Employee employee){
        int index = findEmployee(employee.getId());
        employees.set(index,employee);
    }
    public void remove(Employee employee){
        int index = findEmployee(employee.getId());
        employees.remove(index);
    }
    public void saveEmployees(){
        EmployeeFile file = new EmployeeFile();
        file.save(employees);
    }
    public static ArrayList<Employee> getEmployees(){
        if(employees == null){
            EmployeeFile file = new EmployeeFile();
            employees = file.acquire();
        }
        return employees;
    }
    public static void clear(){
        employees = null;
    }
}
