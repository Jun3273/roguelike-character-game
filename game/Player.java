package game;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player extends Creature {
    
    private Item sword;
    private Item armor;
    private ArrayList<Scroll> scrolls;
    private ArrayList<Item> pack;

    public Player() {
        super();
        this.setType('@');
        scrolls = new ArrayList<Scroll>();
        pack = new ArrayList<Item>();
        System.out.println("Player class");
    }
    public ArrayList<Item> getPack(){
        return pack;
    }

    public void addToPack(Item item){
        pack.add(item);
    }

    public void setWeapon(Item sword) {
        System.out.println("setWeapon");
        this.sword = sword;
    }
    public Item getWeapon(){
        return this.sword;
    }

    public void setArmor(Item armor) {
        System.out.println("setArmor");
        this.armor = armor;
    }
    public Item getArmor(){
        return this.armor;
    }


}
