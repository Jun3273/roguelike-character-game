package game;
public class ChangedDisplayedType extends CreatureAction {

    private String name;

    public ChangedDisplayedType(String name, Creature owner) {
        super(owner);
        this.name = name;
        System.out.println("ChangedDisplayedType class");
    }

    @Override
    public String toString() {
        return name;
    }

}