package test;

import org.junit.Test;
import pojo.ALGraph;

public class BusManager {
    @Test
    public void printTest(){
        System.out.println("你好");
    }
    @Test
    public void addNodeTest(){
        ALGraph alGraph = new ALGraph();
        alGraph.addNode(11,"A");
        alGraph.addNode(12,"B");
        alGraph.addNode(13,"C");
        alGraph.addNode(14,"D");
        alGraph.addNode(15,"E");
        alGraph.showGraph();

    }
    @Test
    public void addEdgeTest(){
        ALGraph alGraph = new ALGraph();
        alGraph.addNode(11,"A");
        alGraph.addNode(12,"B");
        alGraph.addNode(13,"C");
        alGraph.addNode(14,"D");
        alGraph.addNode(15,"E");
        alGraph.addEdge(11,12);
        alGraph.addEdge(11,14);
        alGraph.addEdge(12,13);
        alGraph.addEdge(12,15);
        alGraph.addEdge(13,14);
        alGraph.addEdge(13,15);
        alGraph.showGraph();
    }
    @Test
    public void removeEdgeTest(){
        ALGraph alGraph = new ALGraph();
        alGraph.addNode(11,"A");
        alGraph.addNode(12,"B");
        alGraph.addNode(13,"C");
        alGraph.addNode(14,"D");
        alGraph.addNode(15,"E");
        alGraph.addEdge(11,12);
        alGraph.addEdge(11,14);
        alGraph.addEdge(12,13);
        alGraph.addEdge(12,15);
        alGraph.addEdge(13,14);
        alGraph.addEdge(13,15);
        alGraph.removeEdge(11,14);
        alGraph.showGraph();
    }
    @Test
    public void removeNodeTest(){
        ALGraph alGraph = new ALGraph();
        alGraph.addNode(11,"A");
        alGraph.addNode(12,"B");
        alGraph.addNode(13,"C");
        alGraph.addNode(14,"D");
        alGraph.addNode(15,"E");
        alGraph.addEdge(11,12);
        alGraph.addEdge(11,14);
        alGraph.addEdge(12,13);
        alGraph.addEdge(12,15);
        alGraph.addEdge(13,14);
        alGraph.addEdge(13,15);
        alGraph.removeEdge(11,14);
        alGraph.removeNode(14);
        alGraph.showGraph();
    }
}
