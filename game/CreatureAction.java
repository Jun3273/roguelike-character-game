package game;
public class CreatureAction extends Action {
    
    protected Creature owner;

    public CreatureAction(Creature owner) {
        super();
        System.out.println("CreatureAction class");
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name + ", " + type + ", " + msg;
    }

}
