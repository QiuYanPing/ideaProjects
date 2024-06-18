package file;

import pojo.User;

import java.util.ArrayList;

public class UserFile extends FileHander{
    public UserFile() {
        super("users.dat");
    }
    // 读取文件数据保存在 users 中
    public ArrayList<User> acquire() {
        datas.clear(); // 读取数据前先清除 datas
        read(); // 读取数据
        if(datas == null) return null;
        ArrayList<User> users = new ArrayList<User>();
        for(int i=0; i<datas.size(); i++) {
            User user = dataFromString(datas.get(i));
            if(user != null)
                users.add(user);
        }
        return users;
    }
    // 将 users 中数据写入文件
    public void save(ArrayList<User> users) {
        if(users.isEmpty()) return ;
        datas.clear(); // 先清除
        for(int i=0; i<users.size(); i++) {
            datas.add(dataToString(users.get(i)));
        }
        write(); // 写入文件
    }
    // 将读取的字符串数据转换成用户对象
    private User dataFromString(String line) {
        User user = null;
        // 数据按照 separator 分隔符分隔
        String[] item = line.split(separator);

        user = new User(item[0],item[1],item[2],item[3]);

        return user;
    }
    // 将用户对象数据转换成字符串
    private String dataToString(User user) {
        String line=null;
        if(user != null) {
            // 使用 separator 符号连接数据项
            line = user.getId()+separator+user.getPassword()+separator
                    +user.getName()+separator+user.getPhone()+separator+'\n';
        }
        return line;
    }
}
