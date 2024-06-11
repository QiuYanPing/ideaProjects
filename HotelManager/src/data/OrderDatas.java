package data;

import file.OrderFile;
import pojo.Order;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderDatas {
    public static ArrayList<Order> orders = null;

    public int findOrder(String id) {
        int index = -1;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if(order.getUserId().equals(id) && order.getLeaveTime() == null){
                index = i;
                break;
            }
        }

        return index;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void modifyOrder(Order order){
        int index = findOrder(order.getUserId());
        orders.set(index,order);
    }
    public void remove(Order order){
        int index = findOrder(order.getUserId());
        orders.remove(index);
    }
    public void saveOrders(){
        OrderFile file = new OrderFile();
        file.save(orders);
    }
    public static ArrayList<Order> getOrders() throws ParseException {
        if(orders == null){
            OrderFile file = new OrderFile();
            orders = file.acquire();
        }
        return orders;
    }
    public static void clear(){
        orders = null;
    }
}
