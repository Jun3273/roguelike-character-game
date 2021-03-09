package game;
public class YouWin extends CreatureAction {
    
    private String name;

    public YouWin(String name, Creature owner) {
        super(owner);
        System.out.println("YouWin class");
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
