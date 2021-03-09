package game;
public class Remove extends CreatureAction {
    
    private String name;

    public Remove(String name, Creature owner) {
        super(owner);
        this.name = name;
        System.out.println("Remove class");
    }

    @Override
    public String toString() {
        return name;
    }

}
