package data;

import file.ManagerFile;
import pojo.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManagerDatas {
    public static ArrayList<Manager> managers = null;

    public int findManager(String id) {

        /**
         * 二分查找
         */
        Collections.sort(managers, new Comparator<Manager>() {
            @Override
            public int compare(Manager o1, Manager o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        int index = -1;
        int left = 0;
        int right = managers.size() - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            int i = id.compareTo(managers.get(mid).getId());
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

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public void modifyManager(Manager manager){
        int index = findManager(manager.getId());
        managers.set(index,manager);
    }
    public void remove(Manager manager){
        int index = findManager(manager.getId());
        managers.remove(index);
    }
    public void saveManagers(){
        ManagerFile file = new ManagerFile();
        file.save(managers);
    }
    public static ArrayList<Manager> getManagers(){
        if(managers == null){
            ManagerFile file = new ManagerFile();
            managers = file.acquire();
        }
        return managers;
    }
    public static void clear(){
        managers = null;
    }
}
