package game;

import java.util.ArrayList;
import java.util.Random;

public class Creature extends Displayable {
    
    protected ArrayList<CreatureAction> deathAction;
    protected ArrayList<CreatureAction> hitAction;

    public Creature() {
        super();
        System.out.println("Creature class");
        deathAction = new ArrayList<CreatureAction>();
        hitAction = new ArrayList<CreatureAction>();
    }

    public void setHp(int h) {
        System.out.println("setHP");
        this.hp = h;
    }

    public void setHpMoves(int hpm){
        System.out.println("setHPM");
        this.hpMoves = hpm;
    }

    public void setDeathAction(CreatureAction da) {
        System.out.println("setDA");
        deathAction.add(da);
    }
    public  ArrayList<CreatureAction> getDeathAction(){
        return deathAction;
    }

    public void setHitAction(CreatureAction ha) {
        System.out.println("setHA");
        hitAction.add(ha);
    }
    public  ArrayList<CreatureAction> getHitAction(){
        return hitAction;
    }

    public void setArmor(Item armor){
        System.out.println("setArmor");
    }

    public void setSword(Item sword)
    {
        System.out.println("setSword");
    }

    // Returns dammage dealt
    public int attack(Creature creature) {
        int myHp = this.getHp();
        int myMaxHit = this.getMaxHit();
        int creatHp = creature.getHp();
        int creatMaxHit = creature.getMaxHit();
        System.out.println("My hp: " +myHp+" MaxHit: "+myMaxHit); // debugging msg
        System.out.println("Creature hp: " +creatHp+" MaxHit: "+creatMaxHit);
        Random rand = new Random();
        int damage = rand.nextInt(myMaxHit + 1);
        //avoiding negative hp
        int newHp = creatHp - damage;
        if(newHp < 0){
            newHp = 0;
        }
        creature.setHp(newHp);
        return damage;
    }
}
