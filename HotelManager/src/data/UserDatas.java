package data;

import file.UserFile;
import pojo.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserDatas {
    public static ArrayList<User> users = null;

    public int findUser(String id) {

        /**
         * 二分查找
         */
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        int index = -1;
        int left = 0;
        int right = users.size() - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            int i = id.compareTo(users.get(mid).getId());
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

    public void addUser(User user) {
        users.add(user);
    }

    public void modifyUser(User user){
        int index = findUser(user.getId());
        users.set(index,user);
    }
    public void remove(User user){
        int index = findUser(user.getId());
        users.remove(index);
    }
    public void saveUsers(){
        UserFile file = new UserFile();
        file.save(users);
    }
    public static ArrayList<User> getUsers(){
        if(users == null){
            UserFile file = new UserFile();
            users = file.acquire();
        }
        return users;
    }
    public static void clear(){
        users = null;
    }
}
