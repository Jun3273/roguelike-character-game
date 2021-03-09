package game;
public class Displayable extends Char {

    protected boolean isVisible;
    protected int maxHit;
    protected int hpMoves;
    protected int hp;
    protected char type; // Used as the character representation on Ascii display
    protected int value;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected boolean isCreature;
    
    public Displayable() {
        super(' ');
        isCreature = false;
        System.out.println("Displayable class");
    }

    public void setInvisible() {
        System.out.println("setInvisible");
        this.isVisible = false;
    }

    public void setVisible() {
        System.out.println("setVisible");
        this.isVisible = true;
    }

    public void setMaxHit(int maxHit) {
        System.out.println("setMaxHit");
        this.maxHit = maxHit;
    }

    public int getMaxHit(){
        return this.maxHit;
    }

    public void setHpMove(int hpMoves) {
        System.out.println("setHpMove");
        this.hpMoves = hpMoves;
    }

    public int getHpMoves() {
        return hpMoves;
    }

    public void setHp(int hp) {
        System.out.println("setHp");
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setType(char t) {
        System.out.println("setType");
        this.type = t;
    }

    public char getType() {
        return type;
    }

    public void setIntValue(int v) {
        System.out.println("setIntValue");
        this.value = v;
    }

    public int getIntValue() {
        return value;
    }

    public void setPosX(int x) {
        System.out.println("setPosX");
        this.posX = x;
    }

    public void setPosY(int y) {
        System.out.println("setPosY");
        this.posY = y;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }

    public void setWidth(int x) {
        System.out.println("setWidth");
        this.width = x;
    }
    public int getWidth() {
        return width;
    }

    public void setHeight(int y) {
        System.out.println("setHeight");
        this.height = y;
    }
    public int getHeight() {
        return height;
    }

}
