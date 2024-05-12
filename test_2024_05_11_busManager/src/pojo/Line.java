package pojo;

import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;

public class Line {
    private static int MAXSIZE = 10;
    public ALGraph g = null;
    public VNode[] vexs;
    public String lineNo;
    public int size;

    public Line(ALGraph g) {
        this.g = g;
        this.vexs = new VNode[MAXSIZE];
        this.lineNo = null;
        this.size = 0;
    }

    /**
     * 功能：站点在线路中的位置
     *
     * @param staId 站点id
     * @return 位置索引，若该站点在线路不存在，则返回-1
     */
    public int inLine(int staId) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (staId == vexs[i].id) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 功能：可加入线路的候选站点
     *
     * @param staId 当前站点id
     * @return 候选站点
     */
    public ArrayList<VNode> candidateStation(int staId) {
        int index = g.findNode(staId);
        if (index < 0) return null;
        ArrayList<VNode> cands = new ArrayList<>();
        //候选站点必须与当前站点时邻居关系，且尚未加入到线路中
        for (int i = 0; i < g.vexNum; i++) {
            int candId = g.vexs[i].id;
            if (g.isAdjVertex(staId, candId)) {
                //判断是否加入到线路中
                int flag = 0;
                for (int j = 0; j < size; j++) {
                    if (vexs[j].id == candId) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    //没有加入线路中
                    cands.add(g.vexs[i]);
                }
            }
        }
        return cands;
    }

    /**
     * 功能：添加一个站点
     *
     * @param site  新增站点的位置
     * @param staId 站点id
     * @return 新增是否成功
     */
    public boolean addStation(int site, int staId) {
        if (size == MAXSIZE) {
            System.out.println("线路已达到最大站点数量，无法再填加站点！");
            return false;
        }
        if (site < 0 || site > size) {
            System.out.println("站点位置非法，无法增加站点！");
            return false;
        }
        if (g.findNode(staId) < 0) {
            System.out.println("站点非法，无法增加！");
            return false;
        }
        //线路还没有站点，则可以直接添加
        if (size == 0) {
            vexs[size++] = g.vexs[g.findNode(staId)];
            return true;
        }
        boolean yn = true;
        //如果线路中已经有若干站点，则需要判断新增占是否能够正常添加
        //判断新增站点与site-1位置站点是否为邻居关系
        if (site > 0) {
            yn = yn && g.isAdjVertex(vexs[site - 1].id, staId);
        }
        //判断新增站点与site位置站点是否为邻居关系
        if (site < size) {
            yn = yn && g.isAdjVertex(staId,vexs[site].id);
        }
        if(!yn){
            System.out.println("站点无效，无法增加！");
            return false;
        }else{
            //如果可以正常添加，则件site后面的站点后移
            for (int i = size-1;i>=site;i--){                      //修改部分
                vexs[i+1] = vexs[i];
            }
            vexs[site] = g.vexs[g.findNode(staId)];
            size++;
            return true;
        }
    }

    /**
     * 功能：修改线路中的站点
     * @param oldId 旧站点Id
     * @param newId 新站点Id
     * @return 修改是否成功
     */
    public boolean modifyStation(int oldId, int newId) {
        int index = inLine(oldId);
        if(index<0) return false;//旧站点不在线路中
        if(inLine(newId) >= 0 ) return false;//新站点已在线路中
        boolean yn = true;
        //修改站点时，新、旧站点需要有一致的前驱站点和后继站点
        //如果满足要求，则将旧站点替换为新站点，否则失败
        VNode beforeNode = null;
        VNode afterNode = null;
        if(index == 0){
            afterNode = vexs[index+1];
            yn = yn && g.isAdjVertex(newId,afterNode.id);
        }else if(index == size-1){
            beforeNode = vexs[index-1];
            yn = yn && g.isAdjVertex(newId,beforeNode.id);
        }else {
            afterNode = vexs[index+1];
            beforeNode = vexs[index-1];
            yn = yn && g.isAdjVertex(newId,afterNode.id);
            yn = yn && g.isAdjVertex(newId,beforeNode.id);
        }
        return yn;
    }
    //显示线路信息
    public void show(){
        System.out.print(lineNo+"线路的站点列表：");
        for (int i = 0; i < size; i++) {
            if(i == 0)
                System.out.print(vexs[i].id);
            else
                System.out.print("->"+vexs[i].id);
        }
        System.out.println();
    }

}
