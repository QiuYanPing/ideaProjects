package file;

import pojo.Manager;

import java.util.ArrayList;

public class ManagerFile extends FileHander{
    public ManagerFile() {
        super("managers.dat");
    }
    // 读取文件数据保存在 managers 中
    public ArrayList<Manager> acquire() {
        datas.clear(); // 读取数据前先清除 datas
        read(); // 读取数据
        if(datas == null) return null;
        ArrayList<Manager> managers = new ArrayList<Manager>();
        for(int i=0; i<datas.size(); i++) {
            Manager manager = dataFromString(datas.get(i));
            if(manager != null)
                managers.add(manager);
        }
        return managers;
    }
    // 将 managers 中数据写入文件
    public void save(ArrayList<Manager> managers) {
        if(managers.isEmpty()) return ;
        datas.clear(); // 先清除
        for(int i=0; i<managers.size(); i++) {
            datas.add(dataToString(managers.get(i)));
        }
        write(); // 写入文件
    }
    // 将读取的字符串数据转换成管理员对象
    private Manager dataFromString(String line) {
        Manager manager = null;
        // 数据按照 separator 分隔符分隔
        String[] item = line.split(separator);

        manager = new Manager(item[0],item[1],item[2]);

        return manager;
    }
    // 将管理员对象数据转换成字符串
    private String dataToString(Manager manager) {
        String line=null;
        if(manager != null) {
            // 使用 separator 符号连接数据项
            line = manager.getId()+separator+manager.getName()+separator+manager.getPassword()
                    +separator+'\n';
        }
        return line;
    }
}
