package game;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Room extends Structure {
    
    private String name;
    private int room;

    public Room(String name) {
        super();
        System.out.println("Room class");
        this.setType('X'); //TODO: need to implement floors as '.'
    }

    public void setId(int room) {
        System.out.println("setId");
        this.room = room;
    }

}
