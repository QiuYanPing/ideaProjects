package file;

import pojo.Order;

import java.text.ParseException;
import java.util.ArrayList;

public class OrderFile extends FileHander{
    public OrderFile() {
        super("orders.dat");
    }
    // 读取文件数据保存在 orders 中
    public ArrayList<Order> acquire() throws ParseException {
        datas.clear(); // 读取数据前先清除 datas
        read(); // 读取数据
        if(datas == null) return null;
        ArrayList<Order> orders = new ArrayList<Order>();
        for(int i=0; i<datas.size(); i++) {
            Order order = dataFromString(datas.get(i));
            if(order != null)
                orders.add(order);
        }
        return orders;
    }
    // 将 orders 中数据写入文件
    public void save(ArrayList<Order> orders) {
        if(orders.isEmpty()) return ;
        datas.clear(); // 先清除
        for(int i=0; i<orders.size(); i++) {
            datas.add(dataToString(orders.get(i)));
        }
        write(); // 写入文件
    }
    // 将读取的字符串数据转换成订单对象
    private Order dataFromString(String line) throws ParseException {
        Order order = null;
        // 数据按照 separator 分隔符分隔
        String[] item = line.split(separator);

        order = new Order(item[0],item[1],sdf.parse(item[2]),sdf.parse(item[3]));

        return order;
    }
    // 将订单对象数据转换成字符串
    private String dataToString(Order order) {
        String line=null;
        if(order != null) {
            // 使用 separator 符号连接数据项
            line = order.getUserId()+separator+order.getRoomId()+separator
                    +order.getEnterTime()+separator+order.getLeaveTime()+separator+'\n';
        }
        return line;
    }
}
