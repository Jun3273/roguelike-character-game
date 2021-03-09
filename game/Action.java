package game;
public class Action {
    
    protected String msg;
    protected int iVal;
    protected char cVal;
    protected String name;
    protected String type;
    
    public Action() {
        System.out.println("Action class");
    }

    public void setName(String _name){
        name = _name;
    }
    public String getName(){
        return name;
    }

    public void setType(String _type){
        type = _type;
    }
    public String getType(){
        return type;
    }

    public void setMessage(String msg) {
        this.msg = msg;
        System.out.println("setMsg");
    }
    public String getMessage(){
        return msg;
    }

    public void setIntValue(int v) {
        System.out.println("setIntValue");
        this.iVal = v;
    }
    public int getIntValue(){
        return iVal;
    }

    public void setCharValue(char c){
        System.out.println("setCharValue");
        this.cVal = c;
    }

    public char getCharValue(){
        return cVal;
    }

    public void displayAction(){
        //TODO: Display this message on the screen
        ;
    }
}
