package file;

import pojo.Employee;

import java.util.ArrayList;

public class EmployeeFile extends FileHander{
    public EmployeeFile() {
        super("employees.dat");
    }
    // 读取文件数据保存在 employees 中

    public ArrayList<Employee> acquire() {
        datas.clear(); // 读取数据前先清除 datas
        read(); // 读取数据
        if(datas == null) return null;
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for(int i=0; i<datas.size(); i++) {
            Employee employee = dataFromString(datas.get(i));
            if(employee != null)
                employees.add(employee);
        }
        return employees;
    }
    // 将 employees 中数据写入文件
    public void save(ArrayList<Employee> employees) {
        if(employees.isEmpty()) return ;
        datas.clear(); // 先清除
        for(int i=0; i<employees.size(); i++) {
            datas.add(dataToString(employees.get(i)));
        }
        write(); // 写入文件
    }
    // 将读取的字符串数据转换成员工对象
    private Employee dataFromString(String line) {
        Employee employee = null;
        // 数据按照 separator 分隔符分隔
        String[] item = line.split(separator);

        employee = new Employee(item[0],item[1],item[2],item[3],item[4],item[5]);

        return employee;
    }
    // 将员工对象数据转换成字符串
    private String dataToString(Employee employee) {
        String line=null;
        if(employee != null) {
            // 使用 separator 符号连接数据项
            line = employee.getId()+separator+employee.getPassword()+separator
                    +employee.getName()+separator+employee.getPhone()+
                    employee.getGender()+separator+employee.getPost()+separator+'\n';
        }
        return line;
    }
}
