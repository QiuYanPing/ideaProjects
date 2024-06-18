package data;

import file.EmployeeFile;
import file.ManagerFile;
import pojo.Employee;
import pojo.Manager;

import java.util.ArrayList;

public class ManagerDatas {
    private static ArrayList<Manager> managers=null;
    public int findManager(String id){
        if (managers==null)
            return -1;
        if (id==null)
            return -1;
        int index=-1;
        for(int i=0; i<managers.size(); i++) {
            if(id.equals(managers.get(i).getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void addManager(Manager manager){
        managers.add(manager);
    }

    public void modifyManager(Manager manager){
        int index=findManager(manager.getId());
        managers.set(index,manager);
    }

    public void removeManager(Manager manager){
        int index=findManager(manager.getId());
        managers.remove(index);
    }

    public void saveManagers(){
        ManagerFile file=new ManagerFile();
        file.save(managers);
    }

    public static ArrayList<Manager> getManagers(){
        if (managers==null){
            ManagerFile file=new ManagerFile();
            managers=file.acquire();
        }
        return managers;
    }
    public static void clear(){
        managers=null;
    }
}
