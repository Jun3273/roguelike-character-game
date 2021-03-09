package game;
import java.util.ArrayList;

public class Dungeon {

    private String name;
    private int width;
    private int gameHeight;
    private ArrayList<Creature> creatures;
    private ArrayList<Passage> passages;
    private ArrayList<Item> items;
    private ArrayList<Room> rooms;

    public Dungeon(){
        System.out.println("Dungeon class");
        creatures = new ArrayList<Creature>();
        passages = new ArrayList<Passage>();
        items = new ArrayList<Item>();
        rooms = new ArrayList<Room>();
    }

    // Works as a setter & getter (same implementation in ObjectDisplayGrid)
    public Dungeon getDungeon(String name, int width, int gameHeight) {
        System.out.println("getDungeon");
        if(this.width == 0 && this.gameHeight == 0) { // Initializing dungeon
            this.name = name;
            this.width = width;
            this.gameHeight = gameHeight;
        } 
        return this;
    }

    public void addCreature(Creature creature) {
        System.out.println("addCreature");
        creatures.add(creature);
    }

    public void addPassage(Passage passage) {
        System.out.println("addPassage");
        passages.add(passage);
    }

    public void addItem(Item item) {
        System.out.println("addItem");
        items.add(item);
    }

    public void addRoom(Room room) {
        System.out.println("addRoom");
        rooms.add(room);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Passage> getPassages() {
        return passages;
    }
    
}