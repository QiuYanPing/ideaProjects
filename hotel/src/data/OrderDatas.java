package data;

import file.OrderFile;
import pojo.Order;

import java.text.ParseException;
import java.util.ArrayList;

public class OrderDatas {
    private static ArrayList<Order>orders=null;
    public static int findOrder(String id){
        if(orders==null)return -1;
        if(id==null)return -1;
        int index=-1;
        for(int i=0;i< orders.size();i++){
            if(id.equals(orders.get(i).getUserId())){
                index =i;
                break;
            }
        }
        return index;
    }
    //新增一个订单
    public static void addOrder(Order order){
        orders.add(order);
    }
    //修改订单信息
    public void modifyOrder(Order order){
        int index=findOrder(order.getUserId());
        orders.set(index,order);
    }
    //删除订单
    public static void removeOrder(Order order){
        int index=findOrder(order.getUserId());
        orders.remove(index);
    }
    //order对象数据保存到文件
    public void saveOrders(){
        OrderFile file=new OrderFile();
        file.save(orders);
    }
    //从文件读取所有的用户信息到order对象
    public static ArrayList<Order>getOrders() throws ParseException {
        if(orders==null){
            OrderFile file=new OrderFile();
            orders=file.acquire();
        }
        return orders;
    }
    //清除数据
    public static void clear(){
        orders=null;
    }

}
