package game;
public class DropPack extends CreatureAction {
    
    private String name;

    public DropPack(String name, Creature owner) {
        super(owner);
        this.name = name;
        System.out.println("DropPack class");
    }

    @Override
    public String toString() {
        return name;
    }

}
