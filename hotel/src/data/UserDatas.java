package data;

import file.UserFile;
import pojo.User;

import java.util.ArrayList;

public class UserDatas {
    private static ArrayList<User>users=null;
    public static int findUser(String id){
        if(users==null)return -1;
        if(id==null)return -1;
        int index=-1;
        for(int i=0;i< users.size();i++){
            if(id.equals(users.get(i).getId())){
                index =i;
                break;
            }
        }
        return index;
    }
    //新增一个用户
    public static void addUser(User user){
        users.add(user);
    }
    //修改用户信息
    public void modifyUser(User user){
        int index=findUser(user.getId());
        users.set(index,user);
    }
    //删除用户
    public void removeUser(User user){
        int index=findUser(user.getId());
        users.remove(index);
    }
    //user对象数据保存到文件
    public static void saveUsers(){
        UserFile file=new UserFile();
        file.save(users);
    }
    //从文件读取所有的用户信息到user对象
    public static ArrayList<User>getUsers(){
        if(users==null){
            UserFile file=new UserFile();
            users=file.acquire();
        }
        return users;
    }
    //清除数据
    public static void clear(){
        users=null;
    }

}
