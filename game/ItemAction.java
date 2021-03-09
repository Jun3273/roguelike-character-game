package game;
public class ItemAction extends Action {
    
    protected Item owner;

    public ItemAction(Item owner) {
        super();
        System.out.println("ItemAction class");
        this.owner = owner;
    }

}
