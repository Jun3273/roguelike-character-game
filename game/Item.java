package game;
public class Item extends Displayable {
    
    protected Creature owner;
    protected ItemAction itemAct;

    public Item() {
        super();
        System.out.println("Item class");
    }

    public void setOwner(Creature owner) {
        System.out.println("setOwner");
        this.owner = owner;
    }

    public void setItemAction(ItemAction ia){
        itemAct = ia;
    }
    public ItemAction getItemAction(){
        return itemAct;
    }

}
