package service;

import pojo.Scheduling;

import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeService extends PersonService{
    public void showScheduling(){
        if (schedulings.size()==0){
            System.out.println("排班表为空!");
            return;
        }
        System.out.println("员工编号 \t\t 工作时间");
        for (int i=0;i<schedulings.size();i++){
            Scheduling scheduling = schedulings.get(i);
            System.out.println(scheduling.getEmployeeId()+"\t\t"+sdf.format(scheduling.getWorkTime()));
        }
    }
}
