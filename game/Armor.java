package game;
public class Armor extends Item {

    private String name;
    private int room;
    private int serial;

    public Armor(String name) {
        super();
        System.out.println("Armor class");
        this.setName(name);
        this.setType(']');
    }

    public void setName(String name) {
        System.out.println("setName");
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
