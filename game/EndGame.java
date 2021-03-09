package game;
public class EndGame extends CreatureAction {
    
    private String name;

    public EndGame(String name, Creature owner) {
        super(owner);
        this.name = name;
        System.out.println("EndGame class");
    }

    @Override
    public String toString() {
        return name;
    }

}
