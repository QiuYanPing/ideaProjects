package data;

import file.ManagerFile;
import file.SchedulingFile;
import pojo.Manager;
import pojo.Scheduling;

import java.text.ParseException;
import java.util.ArrayList;

public class SchedulingDatas {
    private static ArrayList<Scheduling> scheduling=null;

    public void saveSchedulings(){
        SchedulingFile file=new SchedulingFile();
        file.save(scheduling);
    }

    public static ArrayList<Scheduling> getSchedulings() throws ParseException {
        if (scheduling==null){
            SchedulingFile file=new SchedulingFile();
            scheduling=file.acquire();
        }
        return scheduling;
    }
    public static void clear(){
        scheduling=null;
    }
}
