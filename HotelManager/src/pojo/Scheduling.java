package pojo;

import java.util.Date;

public class Scheduling {
    private String EmployeeId;
    private Date workTime;

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public Date getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Date workTime) {
        this.workTime = workTime;
    }

    @Override
    public String toString() {
        return "Scheduling{" +
                "EmployeeId='" + EmployeeId + '\'' +
                ", workTime=" + workTime +
                '}';
    }

    public Scheduling(String employeeId, Date workTime) {
        EmployeeId = employeeId;
        this.workTime = workTime;
    }
}
