package game;

import java.util.ArrayList;

public class Passage extends Structure {
    
    private String name;
    private int room1;
    private int room2;
    private ArrayList<Integer> xEdges;
    private ArrayList<Integer> yEdges;

    public Passage() {
        super();
        System.out.println("Passage class");
        this.setType('#'); //TODO: need to implement junctions as '+'
        xEdges = new ArrayList<Integer>();
        yEdges = new ArrayList<Integer>();
    }

    public void setName(String name) {
        System.out.println("setName");
        this.name = name;
    }

    public void setID(int room1, int room2) {
        System.out.println("setID");
        this.room1 = room1;
        this.room2 = room2;
    }

    @Override
    public void setPosX(int x) {
        xEdges.add(x);
    }

    @Override
    public void setPosY(int y) {
        yEdges.add(y);
    }

    public ArrayList<Integer> getxEdges() {
        return xEdges;
    }

    public ArrayList<Integer> getyEdges() {
        return yEdges;
    }

}
