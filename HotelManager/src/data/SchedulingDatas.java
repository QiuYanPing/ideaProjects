package data;


import file.SchedulingFile;

import pojo.Scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SchedulingDatas {
    public static ArrayList<Scheduling> schedulings = null;

    public int findScheduling(String id) {

        /**
         *
         * 二分查找
         */
        Collections.sort(schedulings, new Comparator<Scheduling>() {
            @Override
            public int compare(Scheduling o1, Scheduling o2) {
                return o1.getEmployeeId().compareTo(o2.getEmployeeId());
            }
        });
        int index = -1;
        int left = 0;
        int right = schedulings.size() - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            int i = id.compareTo(schedulings.get(mid).getEmployeeId());
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

    public void addScheduling(Scheduling scheduling) {
        schedulings.add(scheduling);
    }

    public void modifyScheduling(Scheduling scheduling){
        int index = findScheduling(scheduling.getEmployeeId());
        schedulings.set(index,scheduling);
    }
    public void remove(Scheduling scheduling){
        int index = findScheduling(scheduling.getEmployeeId());
        schedulings.remove(index);
    }
    public void saveSchedulings(){
        SchedulingFile file = new SchedulingFile();
        file.save(schedulings);
    }
    public static ArrayList<Scheduling> getSchedulings(){
        if(schedulings == null){
            SchedulingFile file = new SchedulingFile();
            schedulings = file.acquire();
        }
        return schedulings;
    }
    public static void clear(){
        schedulings = null;
    }
}
