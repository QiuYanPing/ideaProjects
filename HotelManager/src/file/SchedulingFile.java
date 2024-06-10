package file;

import pojo.Scheduling;

import java.text.ParseException;
import java.util.ArrayList;

public class SchedulingFile extends FileHander{
    public SchedulingFile() {
        super("schedulings.dat");
    }
    // 读取文件数据保存在 schedulings 中
    public ArrayList<Scheduling> acquire() throws ParseException {
        datas.clear(); // 读取数据前先清除 datas
        read(); // 读取数据
        if(datas == null) return null;
        ArrayList<Scheduling> schedulings = new ArrayList<Scheduling>();
        for(int i=0; i<datas.size(); i++) {
            Scheduling scheduling = dataFromString(datas.get(i));
            if(scheduling != null)
                schedulings.add(scheduling);
        }
        return schedulings;
    }
    // 将 schedulings 中数据写入文件
    public void save(ArrayList<Scheduling> schedulings) {
        if(schedulings.isEmpty()) return ;
        datas.clear(); // 先清除
        for(int i=0; i<schedulings.size(); i++) {
            datas.add(dataToString(schedulings.get(i)));
        }
        write(); // 写入文件
    }
    // 将读取的字符串数据转换成排班表对象
    private Scheduling dataFromString(String line) throws ParseException {
        Scheduling scheduling = null;
        // 数据按照 separator 分隔符分隔
        String[] item = line.split(separator);

        scheduling = new Scheduling(item[0],sdf.parse(item[1]));

        return scheduling;
    }
    // 将排班表对象数据转换成字符串
    private String dataToString(Scheduling scheduling) {
        String line=null;
        if(scheduling != null) {
            // 使用 separator 符号连接数据项
            line = scheduling.getEmployeeId()+separator+scheduling.getWorkTime()
                    +separator+'\n';
        }
        return line;
    }
}
