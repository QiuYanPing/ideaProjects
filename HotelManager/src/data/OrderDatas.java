package data;

import file.OrderFile;
import pojo.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderDatas {
    public static ArrayList<Order> orders = null;

    public int findOrder(String id) {

        /**
         *
         * 二分查找
         */
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getUserId().compareTo(o2.getUserId());
            }
        });
        int index = -1;
        int left = 0;
        int right = orders.size() - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            int i = id.compareTo(orders.get(mid).getUserId());
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
    public static ArrayList<Order> getOrders(){
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
