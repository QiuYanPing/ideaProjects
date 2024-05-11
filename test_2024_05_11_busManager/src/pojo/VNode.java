package pojo;

public class VNode {
    public int id;
    public String info;
    public ArcNode firstArc;

    public VNode(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public VNode(int id, String info, ArcNode firstArc) {
        this.id = id;
        this.info = info;
        this.firstArc = firstArc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArcNode getFirstArc() {
        return firstArc;
    }

    public void setFirstArc(ArcNode firstArc) {
        this.firstArc = firstArc;
    }
}
