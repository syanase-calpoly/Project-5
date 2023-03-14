import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node implements Comparable<Node> {


    private int g;
    private int h;
    private int f;
    private Node prev;
    private Point point;

    public Node(int g, int h, int f, Node prev, Point point) {
        this.g = g;
        this.h = h;
        this.f = f;
        this.prev = prev;
        this.point = point;
    }



    public boolean contains(Point t) {
        return this.point.equals(t);
    }

    public Node getPrev() {
        return prev;
    }

    public void setG(int g) {
        this.g = g;
    }
    public void setF(int f) {
        this.f = f;
    }
    public void setH(int h) {
        this.h = h;
    }


    public Point getPoint() { return point; }
    public int getG() { return g; }
    public int getH() { return h; }
    public int getF() { return f; }
    public int compareTo(Node o) {
        return this.f > o.f ? -1 : 1;
    }
}


