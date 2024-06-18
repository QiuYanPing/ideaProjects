package file;

import pojo.Room;

import java.util.ArrayList;

public class RoomFile extends FileHander{
    public RoomFile() {
        super("rooms.dat");
    }
    // 读取文件数据保存在 rooms 中

    public ArrayList<Room> acquire() {
        datas.clear(); // 读取数据前先清除 datas
        read(); // 读取数据
        if(datas == null) return null;
        ArrayList<Room> rooms = new ArrayList<Room>();
        for(int i=0; i<datas.size(); i++) {
            Room room = dataFromString(datas.get(i));
            if(room != null)
                rooms.add(room);
        }
        return rooms;
    }
    // 将 rooms 中数据写入文件
    public void save(ArrayList<Room> rooms) {
        if(rooms.isEmpty()) return ;
        datas.clear(); // 先清除
        for(int i=0; i<rooms.size(); i++) {
            datas.add(dataToString(rooms.get(i)));
        }
        write(); // 写入文件
    }
    // 将读取的字符串数据转换成房间对象
    private Room dataFromString(String line) {
        Room room = null;
        // 数据按照 separator 分隔符分隔
        String[] item = line.split(separator);
        boolean isBooked= false;
        if(item[3].equals("true")){
            isBooked = true;
        }
        room = new Room(item[0],item[1],Integer.parseInt(item[2]),isBooked,item[4]);

        return room;
    }
    // 将房间对象数据转换成字符串
    private String dataToString(Room room) {
        String line=null;
        if(room != null) {
            // 使用 separator 符号连接数据项
            line = room.getId()+separator+room.getType()+separator
                    +room.getPrice()+separator+room.isBooked()+separator
                    +room.getBookedBy()+separator+'\n';
        }
        return line;
    }
}
