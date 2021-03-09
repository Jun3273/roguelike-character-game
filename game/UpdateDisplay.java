package game;
public class UpdateDisplay extends CreatureAction {
    
    private String name;

    public UpdateDisplay(String name, Creature owner) {
        super(owner);
        this.name = name;
        System.out.println("UpdateDisplay class");
    }

    @Override
    public String toString() {
        return name;
    }

}
