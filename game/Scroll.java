package game;
public class Scroll extends Item {
    
    private String name;
    private int room;
    private int serial;

    public Scroll(String name) {
        super();
        System.out.println("Scroll class");
        this.name = name;
        this.setType('?');
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
