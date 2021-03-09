package game;
public class Sword extends Item {
    
    private String name;
    private int room;
    private int serial;

    public Sword(String name) {
        super();
        System.out.println("Sword class");
        this.name = name;
        this.setType(')');
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int room, int serial) {
        System.out.println("setID");
        this.room = room;
        this.serial = serial;
    }

    @Override
    public String toString() {
        return name;
    }

}