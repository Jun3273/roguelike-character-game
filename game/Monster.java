package game;
public class Monster extends Creature {
    
    private String name;
    private int room;
    private int serial;

    public Monster() {
        super();
        System.out.println("Monster class");
    }

    public void setName(String name) {
        System.out.println("setName");
        this.name = name;
        this.setType(selectType());
    }

    public void setID(int room, int serial) {
        System.out.println("setID");
        this.room = room;
        this.serial = serial;
    }

    public void setArmor(Item armor) {
        System.out.println("setArmor");
    }

    private char selectType() {
        if(name == "Troll") {
            return 'T';
        } else if(name == "Snake") {
            return 'S';
        } else if(name == "Hobgoblin") {
            return 'H';
        } else { // This should not happen
            return 'M';
        }
    }

    @Override
    public String toString() {
        if(this.getType() == 'T') {
            return "troll";
        } else if(this.getType() == 'S') {
            return "snake";
        } else if(this.getType() == 'H') {
            return "hobgoblin";
        } else { // This should not happen
            return "monster";
        }
    }

}
