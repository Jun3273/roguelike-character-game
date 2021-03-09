package game;
public class Teleport extends CreatureAction {
    
    private String name;

    public Teleport(String name, Creature owner) {
        super(owner);
        this.name = name;
        System.out.println("Teleport class");
    }

    @Override
    public String toString() {
        return name;
    }

}
