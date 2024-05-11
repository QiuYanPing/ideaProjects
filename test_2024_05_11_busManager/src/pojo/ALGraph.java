package pojo;

public class ALGraph {
    private static int MAXSIZE = 20;
    public VNode[] vexs;
    public int vexNum;
    public int edgeNum;

    public ALGraph() {
        this.vexs = new VNode[MAXSIZE];
        this.vexNum = 0;
        this.edgeNum = 0;
    }

    //返回顶点存放在vexs中的索引位置
    public int findNode(int id) {
        int index = -1;
        for (int i = 0; i < vexNum; i++) {
            if (id == vexs[i].id) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 功能：判断两个顶点是否相邻
     *
     * @param u 顶点id
     * @param v 顶点id
     * @return true 相邻
     * false 不相邻
     */
    public boolean isAdjVertex(int u, int v) {
        if (u == v) {
            return false;
        }
        int uIndex = findNode(u);
        int vIndex = findNode(v);
        if (uIndex < 0 || vIndex < 0) {
            return false;//其中一个顶点不存在
        }
        boolean yn = false;
        ArcNode p = vexs[uIndex].firstArc;
        //以u为顶点，遍历所有的边节点
        while (p != null) {
            if (p.adjVex == vIndex) {
                yn = true;
                break;
            }
            p = p.nextArc;
        }
        return yn;
    }

    /**
     * 功能：在图中添加节点
     *
     * @param id   顶点id
     * @param info 顶点信息
     * @return true 添加成功
     * false 添加失败
     */
    public boolean addNode(int id, String info) {
        if (findNode(id) >= 0) {
            return false;
            //节点已存在
        }
        VNode node = new VNode(id, info);
        vexs[vexNum++] = node;
        return true;
    }

    /**
     * 功能：在图中添加无向边
     *
     * @param u 顶点id
     * @param v 顶点id
     * @return true 添加成功
     * false 添加失败
     */
    public boolean addEdge(int u, int v) {
        if (u == v) {
            return false;
        }
        int uIndex = findNode(u);
        int vIndex = findNode(v);
        if (uIndex < 0 || vIndex < 0) {
            return false;
        }
        if (isAdjVertex(u, v)) {
            return false;
        }
        //添加边时，在邻接表的顶点表对应的两个顶点都需要添加相应的边节点
        ArcNode uArc = new ArcNode(uIndex);
        ArcNode vArc = new ArcNode(vIndex);
        ArcNode p = vexs[uIndex].firstArc;
        if(p == null)
            vexs[uIndex].firstArc = vArc;
        else{
            while (p.nextArc != null) {
                p = p.nextArc;
            }
            p.nextArc = vArc;
        }


        ArcNode q = vexs[vIndex].firstArc;
        if(q == null)
            vexs[vIndex].firstArc = uArc;
        else{
            while (q.nextArc != null) {
                q = q.nextArc;
            }
            q.nextArc = uArc;
        }
        edgeNum++;
        return true;
    }

    /**
     * 功能：删除图中顶点
     *
     * @param u 顶点id
     * @return
     */
    public boolean removeNode(int u) {
        int uIndex = findNode(u);
        if (uIndex < 0) {
            return false;
        }
        //删除顶点表中的节点
        for (int i = uIndex + 1; i < vexNum; i++) {
            vexs[i - 1] = vexs[i];
        }
        vexNum--;
        //删除节点时，还需要在每个顶点的边节点链表中查找是否有改节点并删除
        for (int i = 0; i < vexNum; i++) {
            /*ArcNode p = vexs[i].firstArc;
            if(p == null)
                continue;
            if(p.adjVex == uIndex){
                vexs[i].firstArc = p.nextArc;
                edgeNum--;
                continue;
            }
            while(p.nextArc !=null){
                if (p.nextArc.adjVex == uIndex) {
                    p.nextArc = p.nextArc.nextArc;
                    edgeNum--;
                    break;
                }
                p = p.nextArc;
            }*/
            int v = vexs[i].id;
            if (isAdjVertex(u, v)) {
                //为邻接点，一定有边要删除
                ArcNode p = vexs[i].firstArc;

                if (p.adjVex == uIndex) {
                    vexs[i].firstArc = p.nextArc;
                    edgeNum--;
                    continue;
                }
                while (p.nextArc != null) {
                    if (p.nextArc.adjVex == uIndex) {
                        p.nextArc = p.nextArc.nextArc;
                        edgeNum--;
                        break;
                    }
                    p = p.nextArc;
                }
            }

        }
        return true;
    }

    /**
     * 功能：删除图中的无向边
     *
     * @param u 顶点id
     * @param v 顶点id
     * @return
     */
    public boolean removeEdge(int u, int v) {
        if (u == v) return false;
        int uIndex = findNode(u);
        int vIndex = findNode(v);
        if (uIndex < 0 || vIndex < 0) return false;


        ArcNode pre, p;
        pre = vexs[uIndex].firstArc;
        p = pre;
        while (p != null) {
            if (p.adjVex == vIndex)
                break;
            pre = p;
            p = p.nextArc;
        }
        if (p == null) return false;
        //删除边
        if (p == vexs[uIndex].firstArc)
            vexs[uIndex].firstArc = p.nextArc;
        else
            pre.nextArc = p.nextArc;
        //顶点v中的边u
        pre = vexs[vIndex].firstArc;
        p = pre;
        while (p != null) {
            if (p.adjVex == uIndex)
                break;
            pre = p;
            p = p.nextArc;
        }
        if(p == null) return false;
        if(p == vexs[vIndex].firstArc)
            vexs[vIndex].firstArc = p.nextArc;
        else
            pre.nextArc = p.nextArc;
        edgeNum--;
        return  true;

    }
    //显示邻接表
    public void showGraph(){
        System.out.println("\n无向图有"+vexNum+"顶点和"+edgeNum+"边\n");
        System.out.println("\t结点\t相邻边");
        ArcNode p;
        for (int i = 0; i < vexNum; i++) {
            System.out.println("\t"+vexs[i].id+"("+vexs[i].info+")");
            p = vexs[i].firstArc;
            while(p!=null){
                VNode v = vexs[p.adjVex];
                System.out.println("\t"+v.id+"("+v.info+")");
                p = p.nextArc;
            }
            System.out.println();
        }
    }
}
