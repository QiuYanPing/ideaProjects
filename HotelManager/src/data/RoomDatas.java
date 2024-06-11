package data;

import file.RoomFile;
import pojo.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RoomDatas {
    public static ArrayList<Room> rooms = null;

    public int findRoom(String id) {


        /**
         * 二分查找
         */
        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        int index = -1;
        int left = 0;
        int right = rooms.size() - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            int i = id.compareTo(rooms.get(mid).getId());
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

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void modifyRoom(Room room){
        int index = findRoom(room.getId());
        rooms.set(index,room);
    }
    public void remove(Room room){
        int index = findRoom(room.getId());
        rooms.remove(index);
    }
    public void saveRooms(){
        RoomFile file = new RoomFile();
        file.save(rooms);
    }
    public static ArrayList<Room> getRooms(){
        if(rooms == null){
            RoomFile file = new RoomFile();
            rooms = file.acquire();
        }
        return rooms;
    }
    public static void clear(){
        rooms = null;
    }


}
