package pojo;

import java.util.ArrayList;
import java.util.Scanner;

public class Lines {
    private static int MAXSIZE = 10;
    public ALGraph g;
    public Line[] routes;
    public int size;
    private Scanner scanner = new Scanner(System.in);
    public Lines(ALGraph g){
        this.routes = new Line[MAXSIZE];
        this.size = 0;
        this.g = g;
    }

    /**
     * 功能：在公交线路网中查找特定编号的公交线路
     * @param lineNo 线路号
     * @return 位置索引，若该线路在公交线路网中不存在，则返回-1
     */
    public int findLineNo(String lineNo){
        int index = -1;
        for(int i  = 0 ;i<size;i++){
            if(lineNo.equals(routes[i].lineNo)){
                index = i;
                break;
            }
        }
        return index;
    }
    //显示公交线路信息
    public void showLines(){
        System.out.println("所有公交线路信息：");
        for (int i = 0; i < size; i++) {
            System.out.print("  "+(i+1)+"  ");
            routes[i].show();
        }
    }

    /**
     * 功能：创建一条新线路
     * @return 线路
     */
    public Line createLine(){
        Line line = new Line(g);
        System.out.println("请输入新线路编号：");
        while(true){
            String lineNo = scanner.next();
            if(findLineNo(lineNo)<0){
                line.lineNo = lineNo;
                break;
            }
            System.out.println("线路编号已存在，请重新输入新线路编号：");
        }
        int id;
        //循环添加站点，每次都是在当前线路的末尾进行添加
        while(true){
            if(line.size > 0){
                //显示出当前可加入线路的候选站点
                ArrayList<VNode> candsNode = line.candidateStation(line.vexs[line.size - 1].id);//这里的size是line.size，不是size，一定要注意
                System.out.println("可选站点列表：");
                for (VNode cand:
                     candsNode) {
                    System.out.print(cand.id+"  ");
                }
                System.out.println();
            }
            System.out.println("请输入站点编号（-1表示终止在线路中增加新站点）：");
            id = scanner.nextInt();
            if(id == -1) break;
            //若添加站点失败，则提示用户添加失败，若添加站点成功，则显示线路的当前所有站点
            boolean yn = line.addStation(line.size, id);
            if(yn){
                line.show();
            }else{
                System.out.println("添加站点失败");
            }
        }
        return line;
    }

    /**
     * 功能：添加公交线路，线路站点数量不少于5个
     * @return 添加线路是否成功
     */
    public boolean addLine(){
        if(size == MAXSIZE) return false;
        Line line = createLine();
        if(line.size < 5){
            System.out.println("线路站点数少于5个！");
            return false;
        }
        routes[size++] = line;
        return true;
    }

    /**
     * 功能：修改公交线路的某个站点
     * @param index 线路索引
     * @param oldId 旧站点Id
     * @param newId 新站点Id
     * @return 如果修改成功，则返回1，否则根据不同情况返回不同的错误码
     * -1  线路不存在
     * -2  旧站点不在线路中
     * -3  新站点不存在
     * 0   修改失败
     */
    public int modifyLine(int index,int oldId,int newId){
        if(index<0 || index>=size) return -1;
        if(routes[index].inLine(oldId) < 0) return -2;
        if(g.findNode(newId) < 0) return -3;
        if(routes[index].modifyStation(oldId, newId))
            return 1;
        else
            return 0;
    }

    /**
     * 功能：删除公交线路
     * @param index 线路位置索引
     * @return 删除线路是否成功
     */
    public boolean removeLine(int index){
        if(size == 0) return false;
        if(index<0 || index>=size) return false;
        for(int j = index+1;j<size;j++){
            routes[j-1] = routes[j];
        }
        size--;
        return true;
    }

    /**
     *功能：查询线路，查询从起始站点-->目的站点的线路
     * @param begin 起始站点Id
     * @param end   目的站点Id
     * @return  查询是否成功
     */
    public boolean queryLine(int begin,int end){
        if(begin == end) return false;//起始站点和目的站点不能是同一站点
        boolean yn = false;
        for (int i = 0; i < size; i++) {
            Line line = routes[i];
            //查询公交线路上是否存在这两个站点
            //如果存在，则输入起始站点->目的站点的线路，否则返回false
            int beginIndex = line.inLine(begin);
            int endIndex = line.inLine(end);
            if(beginIndex == -1 || endIndex == -1){
                continue;
            }
            //存在
            if(beginIndex<endIndex){
                System.out.print("线路："+line.lineNo+" ");
                for (int j = beginIndex; j <= endIndex; j++) {
                    if(j == endIndex){
                        System.out.println(line.vexs[j].id);
                    }else{
                        System.out.print(line.vexs[j].id+"->");
                    }
                }
            }else{
                System.out.print("线路："+line.lineNo+" ");
                for (int j = beginIndex; j >= endIndex; j--) {
                    if(j == endIndex){
                        System.out.println(line.vexs[j].id);
                    }else{
                        System.out.print(line.vexs[j].id+"->");
                    }
                }
            }
            yn = true;
        }
        return yn;
    }
}
