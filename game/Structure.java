package game;

import java.util.ArrayList;

public class Structure extends Displayable {
    protected ArrayList<Creature> creatures;
    protected ArrayList<Item> items;

    public Structure() {
        super();
        creatures = new ArrayList<Creature>();
        items = new ArrayList<Item>();
        System.out.println("Structure class");
    }

    public void setCreature(Creature creature) {
        System.out.println("setCreature");
        if(creature != null) // Causes error
            creatures.add(creature);
        }
    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public void setItem(Item item){
        System.out.println("setItem");
        if(item != null) // Causes error
            items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    public ArrayList<Creature> getMonsters() {
        return creatures;
    }
}
