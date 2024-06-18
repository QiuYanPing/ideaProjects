package file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileHander {
    protected static String separator = "\t";// 数据项之间的分隔符
    protected String filename = null; // 数据保存的文件名
    protected ArrayList<String> datas; // 每条记录数据以字符串的形式保存
    // 用于格式化时间
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public FileHander(String filename) {
        this.filename = filename;
        this.datas = new ArrayList<String>();
    }
    // 从文件中读取数据
    public void read() {
        BufferedReader reader = null;
        // 读取文件数据前，清除 datas 中的值
        datas.clear();
        try {
            // 以缓冲流方式读取文件中数据
            reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                datas.add(line); // 每条记录（每行数据）保存为一个字符串对象
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(reader != null)
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
    // 将数据保存到文件中
    public void write() {
        // 文件内容
        String content = listToString(); // datas 对象数据转换成字符串保存
        if(content == null) return;
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // datas 对象数据转换成字符串
    public String listToString() {
        if(datas == null) return null;
        String content = "";
        for(int i=0; i<datas.size(); i++) {
            content += datas.get(i);
        }
        return content;
    }
}
