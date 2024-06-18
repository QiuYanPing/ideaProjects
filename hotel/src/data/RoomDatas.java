package data;

import file.RoomFile;
import pojo.Room;

import java.util.ArrayList;

public class RoomDatas {
    private static ArrayList<Room>rooms=null;
    public static int findRoom(String id){
        if(rooms==null)return -1;
        if(id==null)return -1;
        int index=-1;
        for(int i=0;i< rooms.size();i++){
            if(id.equals(rooms.get(i).getId())){
                index =i;
                break;
            }
        }
        return index;
    }



    //新增一个用户
    public void addRoom(Room room){
        rooms.add(room);
    }
    //修改用户信息
    public void modifyRoom(Room room){
        int index=findRoom(room.getId());
        rooms.set(index,room);
    }
    //删除用户
    public void removeRoom(Room room){
        int index=findRoom(room.getId());
        rooms.remove(index);
    }
    //user对象数据保存到文件
    public void saveRooms(){
        RoomFile file=new RoomFile();
        file.save(rooms);
    }
    //从文件读取所有的用户信息到user对象
    public static ArrayList<Room>getRooms(){
        if(rooms==null){
            RoomFile file=new RoomFile();
            rooms=file.acquire();
        }
        return rooms;
    }
    public static void bookRoom(String roomId, String userId) {
        int index = findRoom(roomId);
        if (index >= 0) {
            Room room = rooms.get(index);
            room.setBooked(true);
            room.setBookedBy(userId);
        }
    }
    public static void checkoutRoom(String roomId) {
        int index = findRoom(roomId);
        if (index >= 0) {
            Room room = rooms.get(index);
            room.setBooked(false);
            room.setBookedBy(null);
        }
    }


    //清除数据
    public static void clear(){
        rooms=null;
    }

}
