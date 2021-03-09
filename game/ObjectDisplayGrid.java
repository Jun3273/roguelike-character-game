package game;

import asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject {
    
    private static final int DEBUG = 0;
    private int gameHeight;
    private int width;
    private int topHeight;
    private int bottomHeight;
    private int totalHeight;
    private static final String CLASSID = ".ObjectDisplayGrid";
    private static AsciiPanel terminal;
    private Displayable[][] objectGrid = null;
    private ArrayList<Displayable> displayables = null;
    private List<InputObserver> inputObservers = null;
    // Implementing stack
    private Stack<Displayable>[][] stackObjGrid = null;
    private char[][] charObjGrid = null;
    private Dungeon dungeon = null;
    private int playerGlobalPosX = 0 ;
    private int playerGlobalPosY = 0;
    private Player playerGlobal;
    private boolean stopGame = false;
    private int lastKeycode = 0;
    private int lastLastKeycode = 0;
    private int hpmTracker = 0;
    private int hallucinateTrack = 0;
    private char[] randoms ={'?','@',')','S','T','.','+','#','H',']' };
    private ArrayList<Position> teleportableSpace;

    public ObjectDisplayGrid() {
    }

    @Override
    public String toString() {
        String str = "ObjectDisplayGrid:\n";
        str += "Width: " + width + "\n";
        str += "topHeight: " + topHeight + "\n";
        str += "bottomHeight: " + bottomHeight + "\n";
        str += "totalHeight: " + totalHeight + "\n";
        str += "CLASSID: " + CLASSID + "\n";
        str += "Displayables: " + objectGrid + "\n";
        return str;
    }

    public ObjectDisplayGrid getObjectDisplayGrid(int gameHeight, int width, int topHeight, int bottomHeight) {

        if(objectGrid == null) {
            // Initialize
            this.gameHeight = gameHeight;
            this.width = width;
            this.topHeight = topHeight;
            this.bottomHeight = bottomHeight;
            this.totalHeight = topHeight + gameHeight + bottomHeight;
            stackObjGrid = (Stack<Displayable>[][]) new Stack[width][totalHeight]; 
            charObjGrid = new char[width][totalHeight];
            for(int x = 0; x < width; ++x) { // initializing each stack
                for(int y = 0; y < totalHeight; ++y) {
                    stackObjGrid[x][y] = (Stack<Displayable>) new Stack();
                }
            }
            objectGrid = new Displayable[width][totalHeight];
            displayables = new ArrayList<Displayable>();
            terminal = new AsciiPanel(width, totalHeight);
            super.add(terminal);
            super.setSize(width * 9, totalHeight * 16);
            super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            super.setVisible(true);
            terminal.setVisible(true);
            super.addKeyListener(this);
            inputObservers = new ArrayList<>();
            super.repaint();
            terminal.repaint();
        }
        return this;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void setDisplayables(ArrayList<Displayable> disp){
        displayables.addAll(disp);
    }

    public final void initializeDisplay() {
        setStackObjGrid();
        updateTopDisplay();
        updateGameDisplay();
        updateBottomDisplay("", ""); // no message at first
        assignItems();
        terminal.repaint();
    }

    public void handleHPMoves(){
        if(playerGlobal == null){
            return;
        }
        if(playerGlobal.getHpMoves() == hpmTracker){
            hpmTracker = 0;
            playerGlobal.setHp(playerGlobal.getHp() + 1);
        }
        else{
            hpmTracker++;
        }
        updateTopDisplay();
    }

    public void assignItems() {
      int x = playerGlobalPosX;
      int y = playerGlobalPosY;
        if(!stackObjGrid[x][y].empty()) {
            if(stackObjGrid[x][y].size() > 1) { //doubly defined item and creature
                for (Displayable disp : stackObjGrid[x][y]) {
                    if (disp.getType() == ')' || disp.getType() == ']' || disp.getType() == '?'){
                        playerGlobal.addToPack((Item)disp);
                    }
                }
                for (Displayable disp : playerGlobal.getPack()){
                    stackObjGrid[x][y].remove(disp);
                }
            }
        }

    }

    public void updateGameDisplay() {
        teleportableSpace = new ArrayList<Position>();
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < totalHeight; ++y) {
                if(!stackObjGrid[x][y].empty()) {
                    char type = stackObjGrid[x][y].peek().getType();
                    if (type != '@') { //ignore if it is a player
                        writeToTerminal(type, x, y);
                        if (type == '#') { // Passage
                            boolean containsRoom = false;
                            for (Displayable disp : stackObjGrid[x][y]) {
                                if (disp.getType() == 'X') {
                                    containsRoom = true;
                                }
                            }
                            if (containsRoom) { // connection between room and passage
                                writeToTerminal('+', x, y);
                            }
                            teleportableSpace.add(new Position(x, y));
                        } else if (type == 'X') { // Room
                            if (neighborsNotEmpty(x, y)) {
                                writeToTerminal('.', x, y);
                                teleportableSpace.add(new Position(x, y));
                            }
                        }
                    } else { // player
                        writeToTerminal('@',playerGlobalPosX, playerGlobalPosY);
                    }
                }
            }
        }
    }

    public void updateTopDisplay() {
        String message = "HP: " + Integer.toString(playerGlobal.getHp());
        message += "  score: " + Integer.toString(0) + "     ";// score zero for now
        writeMsgToTerminal(message, 0, 0);
    }

    public void updateBottomDisplay(String packMsg, String infoMsg) {
        int packY = topHeight + gameHeight + 1;
        int infoY = packY + 2;
        String pack = "Pack:" + packMsg;
        String info = "Info: " + infoMsg;
        if(!packMsg.isEmpty()) {
            clearLineOnTerminal(packY);
        }
        if(!infoMsg.isEmpty()) {
            clearLineOnTerminal(infoY);
        }
        writeMsgToTerminal(pack, 0, packY);
        writeMsgToTerminal(info, 0, infoY);
    }

    private boolean neighborsNotEmpty(int x, int y) {
        // All edges of game screen
        if(x == 0 || x == width-1 || y == topHeight || y == topHeight+gameHeight-1) {
            return false;
        }
        if(!stackObjGrid[x-1][y].empty() && !stackObjGrid[x+1][y].empty()
            && !stackObjGrid[x][y-1].empty() && !stackObjGrid[x][y+1].empty()) {
            return true;
        }
        return false;
    }



    public void setTopMessageHeight(int topHeight) {
        System.out.println("setTopMessageHeight");
        this.topHeight = topHeight;
    }


    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
            if (DEBUG > 0) {
                System.out.println(CLASSID + ".notifyInputObserver " + ch);
            }
        }
    }

    private void setStackObjGrid() {
        for(Room room : dungeon.getRooms()) { // Room and everything inside room
            int roomXpos = room.getPosX();
            int roomYpos = room.getPosY() + topHeight; // because topHeight is for HP
            int roomWidth = room.getWidth();
            int roomHeight = room.getHeight();
            for(int xPos = roomXpos; xPos < roomWidth+roomXpos; ++xPos) { // Adding room to objDisp
                for(int yPos = roomYpos; yPos < roomHeight+roomYpos; ++yPos) {
                    stackObjGrid[xPos][yPos].push(room);
                }
            }

            for(Item item : room.getItems()) { // Adding items on top of room
                int itemXpos = item.getPosX();
                int itemYpos = item.getPosY();
                stackObjGrid[roomXpos+itemXpos][roomYpos+itemYpos].push(item);
            }
            for(Creature creature : room.getCreatures()) { // Adding creatures on top of room
                int creatXpos = creature.getPosX();
                int creatYpos = creature.getPosY();
                stackObjGrid[roomXpos+creatXpos][roomYpos+creatYpos].push(creature);
                if(creature.getType() == '@'){ //get player
                    playerGlobal = (Player) creature;
                    playerGlobalPosX = roomXpos+creatXpos;
                    playerGlobalPosY = roomYpos+creatYpos;
                }
            }
        }
        for(Passage passage : dungeon.getPassages()) { // Setting up passages
            ArrayList<Integer> xEdges = passage.getxEdges();
            ArrayList<Integer> yEdges = passage.getyEdges();
            for (int point2 = 1; point2 < xEdges.size(); ++point2) {
                int point1 = point2 - 1;
                int xPos1 = xEdges.get(point1);
                int yPos1 = yEdges.get(point1) + topHeight;
                int xPos2 = xEdges.get(point2);
                int yPos2 = yEdges.get(point2) + topHeight;
                if (xPos1 == xPos2) { // vertical
                    int smallY = Math.min(yPos1, yPos2);
                    int bigY = Math.max(yPos1, yPos2);
                    for (int y = smallY; y <= bigY; ++y) {
                        stackObjGrid[xPos1][y].push(passage);
                    }
                } else if (yPos1 == yPos2) { // horizontal
                    int smallX = Math.min(xPos1, xPos2);
                    int bigX = Math.max(xPos1, xPos2);
                    for (int x = smallX; x <= bigX; ++x) {
                        stackObjGrid[x][yPos1].push(passage);
                    }
                } else { // Shouldn't happen!!!!
                    System.out.println("ERROR IN SETTING UP PASSAGES IN OBJDISP!!!!!!");
                }
            }
        }

        
    }

    // I kind of got lazy and implemented attcking the monster here
    // I don't think charObjGrid is needed and thought about changing it
    // to stack.peek().getType(), but I was scared cause it might break something

    //charObjGrid is to deal with stuff like passages: nothing is in the stack there
    //but still able to be moved to. AS well as '.' where its null but can move tthere,
    //more of quality of life without having to check a bunch of logic to see where it goes


    public boolean isValidMove(int x, int y){
       boolean isValidStep = false;
       boolean isValidBound = true;
        System.out.println("CharObjGrid SEE: " + charObjGrid[x][y]);
        if (charObjGrid[x][y] == '.') { //room
            isValidStep = true ;
        } else if (charObjGrid[x][y]  == '#') { //passage
            isValidStep = true;
        } else if (charObjGrid[x][y]  == '+') { //passage entrance
            isValidStep = true;
        } else if (charObjGrid[x][y]  == '?' ||  charObjGrid[x][y]  == ')' || charObjGrid[x][y]  == ']') {
            isValidStep = true;
        } else if(charObjGrid[x][y]  == 'T'
                    || charObjGrid[x][y]  == 'S'
                    || charObjGrid[x][y]  == 'H') {

            Monster monster = (Monster) stackObjGrid[x][y].peek();
            int monsterInitHp = monster.getHp();
            int damage = playerGlobal.attack(monster);
            boolean monsterHitBack = false;
            String info = "";
            if(monsterInitHp > 0) {  // for cases when moster doesn't have remove
                info = "player-" + damage + " ";
            }
            // Testing start ***********
            // for(CreatureAction cAction : playerGlobal.getDeathAction()) {
            //     System.out.println("Player death action " + cAction);
            // }
            // for(CreatureAction cAction : playerGlobal.getHitAction()) {
            //     System.out.println("Player hit action " + cAction);
            // }
            // for(CreatureAction cAction : monster.getDeathAction()) {
            //     System.out.println("Monster death action " + cAction);
            // }
            // for(CreatureAction cAction : monster.getHitAction()) {
            //     System.out.println("Monster hit action " + cAction);
            // }
            // Testing end ***********
            if(stackObjGrid[x][y].peek().getHp() <= 0) { // monster died
                // Death action for monster
                for(CreatureAction cAction : monster.getDeathAction()) {
                    System.out.println("*** MD:Name  " + cAction.getName());
                    if(cAction.getName().equals("Remove")) { // Remove
                        stackObjGrid[x][y].pop();
                        updateGameDisplay();
                    } 
                    if(cAction.getMessage() != null) {
                        info += cAction.getMessage();
                    }
                }
            } else { // monster fights back
                // Hit action for monster
                boolean hasTeleported = false;
                for(CreatureAction cAction : monster.getHitAction()) {
                    System.out.println("*** MH:Name  " + cAction.getName());
                    if(cAction.getName().equals("Teleport")) { // Teleport
                        hasTeleported = true;
                        Random random = new Random();
                        Position newPosition = teleportableSpace.get(random.nextInt(teleportableSpace.size()));
                        int newX = newPosition.getX();
                        int newY = newPosition.getY();
                        stackObjGrid[x][y].pop();
                        stackObjGrid[newX][newY].push(monster);
                    }
                    if(cAction.getMessage() != null) {
                        info += cAction.getMessage();
                    }
                }
                if(!hasTeleported) {
                    monsterHitBack = true;
                    damage = monster.attack(playerGlobal);
                    info += "" + monster + "-" + damage + " ";
                }
            }
            updateTopDisplay();
            if(playerGlobal.getHp() <= 0) { // end game
                // Death action for player
                for(CreatureAction cAction : playerGlobal.getDeathAction()) {
                    System.out.println("*** PD:Name  " + cAction.getName());
                    if(cAction.getName().equals("ChangeDisplayedType")) { // ChangeDisplayedType
                        writeToTerminal(cAction.getCharValue(), playerGlobalPosX, playerGlobalPosY);
                    } else if(cAction.getName().equals("Remove")) { // Remove
                        stackObjGrid[playerGlobalPosX][playerGlobalPosY].pop();
                        updateGameDisplay();
                    } else if(cAction.getName().equals("EmptyPack")) { // EmptyPack
                        dropAllItems();
                    }
                    if(cAction.getMessage() != null) {
                        info += cAction.getMessage();
                    }
                }
                dropAllItems();
                stopGame = true;
                updateBottomDisplay("", info);
                super.repaint();
                terminal.repaint();
            } else if(monsterHitBack) {
                // Hit Action for player
                for(CreatureAction cAction : playerGlobal.getHitAction()) {
                    System.out.println("*** PH:Name  " + cAction.getName());
                    if(cAction.getName().equals("DropPack")) { // DropPack
                        dropItem(0);
                    } else if(cAction.getName().equals("EmptyPack")) { // EmptyPack
                        dropAllItems();
                    }
                    if(cAction.getMessage() != null) {
                        info += cAction.getMessage();
                    }
                }
                updateBottomDisplay("", info);
                updateGameDisplay();
                super.repaint();
                terminal.repaint();
            } else { // monster dead player alive
                updateBottomDisplay("", info);
                updateGameDisplay();
                super.repaint();
                terminal.repaint();
            }
            
        }

        //check edgesboolean isMove
        if (!((0 <= x) && (y < objectGrid.length))) {
            isValidBound = false;
        } else if (!((0 <= y) && (y < objectGrid[0].length))) {
            isValidBound = false;
        }
        if(isValidBound && isValidStep){
            handleHPMoves();
            if(hallucinateTrack > 0){
                updateBottomDisplay("","Hallucinating for " + (hallucinateTrack - 1) + " moves");
                hallucinateTrack--;
            }
        }
        return isValidStep && isValidBound;
    }

    public void dropAllItems(){
       for (Item item : playerGlobal.getPack()){
           stackObjGrid[playerGlobalPosX][playerGlobalPosY].push(item);
       }
    }

    public void setNewPlayerCoords(int x, int y) {
        stackObjGrid[x][y].push(playerGlobal); //push player to new coords
        stackObjGrid[playerGlobalPosX][playerGlobalPosY].pop(); //pop from old coords
        playerGlobal.setPosX(playerGlobalPosX);
        playerGlobal.setPosY(playerGlobalPosY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(stopGame) { // end game
            return;
        }
        int keyCode = e.getKeyCode();
        System.out.println("Player at: " + playerGlobalPosX + ", " + playerGlobalPosY);
        switch( keyCode ) {
            case KeyEvent.VK_K: //up
                if (isValidMove(playerGlobalPosX, playerGlobalPosY - 1)) {
                    setNewPlayerCoords(playerGlobalPosX, playerGlobalPosY - 1); //change location on stack
                    playerGlobalPosY --; //change location within object
                    updateGameDisplay();
                    super.repaint();
                    terminal.repaint();
                }
                break;
            case KeyEvent.VK_J: // down
                if (isValidMove(playerGlobalPosX, playerGlobalPosY + 1)) {
                    setNewPlayerCoords(playerGlobalPosX, playerGlobalPosY + 1); //change location on stack
                    playerGlobalPosY++; //change location within object
                    updateGameDisplay();
                    super.repaint();
                    terminal.repaint();
                }
                break;
            case KeyEvent.VK_H: //left
                if (lastKeycode != KeyEvent.VK_SLASH && isValidMove(playerGlobalPosX - 1, playerGlobalPosY)) {
                    setNewPlayerCoords(playerGlobalPosX - 1, playerGlobalPosY); //change location on stack
                    playerGlobalPosX--; //change location within object
                    updateGameDisplay();
                    super.repaint();
                    terminal.repaint();
                }
                break;
            case KeyEvent.VK_L: //right
                if (isValidMove(playerGlobalPosX + 1, playerGlobalPosY)) {
                    setNewPlayerCoords(playerGlobalPosX + 1, playerGlobalPosY); //change location on stack
                    playerGlobalPosX++; //change location within object
                    updateGameDisplay();
                    super.repaint();
                    terminal.repaint();
                }
                break;
            case KeyEvent.VK_P: //pick up an item (P)
                //are we helping
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_P));
                    break;
                }
                System.out.println("Attempting to pick up item");
                pickUpItem(playerGlobalPosX, playerGlobalPosY);
                break;
            case KeyEvent.VK_I: // open inventory (I)
                //are we helping
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_I));
                    break;
                }
                String packMsg = " ";
                int ind = 0;
                for(Item item : playerGlobal.getPack()) {
                    packMsg += "["+(ind++)+"]:"+item;
                    if(playerGlobal.getWeapon() != null && item.equals(playerGlobal.getWeapon())){
                        packMsg +="(w)";
                    }
                    else if(playerGlobal.getArmor() != null && item.equals(playerGlobal.getArmor())){
                        packMsg +="(a)";
                    }
                    packMsg +=" ";
                }
                updateBottomDisplay(packMsg, "");
                break;
            case KeyEvent.VK_C:
                //are we helping
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_C));
                    break;
                }
                //make sure theres actually armor to take off
                if(playerGlobal.getArmor() == null){
                    System.out.println("No armor to remove");
                    break;
                }
                updateBottomDisplay("", "Took off armor " + playerGlobal.getArmor());
                //pop off first item if it pack is overflowing
                if (playerGlobal.getPack().size() > 9) {
                    stackObjGrid[playerGlobalPosX][playerGlobalPosY].push(playerGlobal.getPack().get(0));
                    playerGlobal.getPack().remove(0);
                    System.out.println("Overflowed Pack, dropped item 0");
                }
                //get rid of armor on player
                playerGlobal.setArmor(null);
                updateGameDisplay();
                super.repaint();
                terminal.repaint();
                break;
            case KeyEvent.VK_Y: //end game
                if(lastKeycode == KeyEvent.VK_E){
                    updateBottomDisplay("","Game has been ended by user");
                    stopGame = true;
                    return;
                }
                break;
            case KeyEvent.VK_E:
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_E));
                }
                break;
            case KeyEvent.VK_SLASH:
                updateBottomDisplay("","'c', 'd<int>', 'E<y|Y>', '?', 'h', 'i'," +
                                                                " 'w', 'p', 'r', 't'");
                break;
            case KeyEvent.VK_D:
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_D));
                }
                break;
            case KeyEvent.VK_W:
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_W));
                }
                break;
            case KeyEvent.VK_T:
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_T));
                }
                break;
            case KeyEvent.VK_R:
                if(lastLastKeycode == KeyEvent.VK_SLASH && lastKeycode == KeyEvent.VK_H){
                    updateBottomDisplay("",lookUpCommands(KeyEvent.VK_R));
                }
                break;
            case KeyEvent.VK_0:

                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(0);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(0);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(0);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(0);
                }
                break;
            case KeyEvent.VK_1:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(1);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(1);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(1);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(1);
                }
                break;
            case KeyEvent.VK_2:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(2);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(2);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(2);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(2);
                }
                break;
            case KeyEvent.VK_3:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(3);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(3);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(3);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(3);
                }
                break;
            case KeyEvent.VK_4:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(4);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(4);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(4);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(4);
                }
                break;
            case KeyEvent.VK_5:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(5);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(5);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(5);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(5);
                }
                break;
            case KeyEvent.VK_6:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(6);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(6);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(6);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(6);
                }
                break;
            case KeyEvent.VK_7:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(7);
                } else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(7);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(7);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(7);
                }
                break;
            case KeyEvent.VK_8:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(8);
                }else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(8);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(8);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(8);
                }
                break;
            case KeyEvent.VK_9:
                //put on armor w
                if(lastKeycode == KeyEvent.VK_W){
                    putOnArmor(9);
                } else if(lastKeycode == KeyEvent.VK_D){
                    dropItem(9);
                } else if(lastKeycode == KeyEvent.VK_T){
                    wieldSword(9);
                } else if(lastKeycode == KeyEvent.VK_R){
                    readScroll(9);
                }
                break;
        }
        lastLastKeycode = lastKeycode;
        lastKeycode = keyCode;
        // updateGameDisplay();
        super.repaint();
        terminal.repaint();
    }

    public String lookUpCommands(int command){
        String msg = "";
            if (command == KeyEvent.VK_C) {
                msg += "'c' - change or take off armor and place in pack";
            } else if (command == KeyEvent.VK_D) {
                msg += "'d<int>' - drop item <int> from the pack";
            } else if (command == KeyEvent.VK_E) {
                msg += "'E<y|Y>' - end current game";
            } else if (command == KeyEvent.VK_SLASH) {
                msg += "'?' - show different commands availible";
            } else if (command == KeyEvent.VK_H) {
                msg += "'h<char>' - give detailed information about a specific command";
            } else if (command == KeyEvent.VK_I) {
                msg += "'i' - show contents of the pack";
            } else if (command == KeyEvent.VK_P) {
                msg += "'p' - pickup item from the dungeon floor";
            } else if (command == KeyEvent.VK_R) {
                msg += "'r<int>' - read scrooll from pack";
            } else if (command == KeyEvent.VK_T) {
                msg += "'t<int>' - wield a sword from pack";
            } else if (command == KeyEvent.VK_W) {
                msg += "'w<int>' - wear an armor from pack";
            } else {
                msg += "That command does not exist";
            }
        return msg;
    }

    public void readScroll(int itemNum){
        //check for errors
        if(playerGlobal.getPack().size() < itemNum + 1 || playerGlobal.getPack().get(itemNum) == null || playerGlobal.getPack().get(itemNum).getType() != '?') {
            updateBottomDisplay("", "There is no scroll in that inventory slot");
            return;
        }
        Scroll scroll  = (Scroll)playerGlobal.getPack().get(itemNum);
        ItemAction ia = scroll.getItemAction();
        boolean usedScroll = true;
        if(ia.getName().equalsIgnoreCase("BlessArmor")){
            if(playerGlobal.getArmor() != null){
                playerGlobal.getArmor().setIntValue(playerGlobal.getArmor().value + ia.getIntValue());
                Armor armor = (Armor) playerGlobal.getArmor();
                if(armor.getIntValue() >= 0) {
                    armor.setName("+" + armor.getIntValue() + " Armor");
                } else {
                    armor.setName("-" + armor.getIntValue() + " Armor");
                }
                updateBottomDisplay("", "Read the scroll of " + ia.name + " changing the armor's effectiveness by " + ia.getIntValue());
            }else{
                updateBottomDisplay("", "The scroll of " + ia.name + " has no effect because no armor is wielded");
            }
        }
        else if(ia.getName().equalsIgnoreCase("BlessSword")){
            if(playerGlobal.getWeapon() != null){
                playerGlobal.getWeapon().setIntValue(playerGlobal.getWeapon().value + ia.getIntValue());
                Sword sword = (Sword) playerGlobal.getWeapon();
                if(sword.getIntValue() >= 0) {
                    sword.setName("+" + sword.getIntValue() + " Sword");
                } else {
                    sword.setName("-" + sword.getIntValue() + " Sword");
                }
                updateBottomDisplay("", "Read the scroll of " + ia.name+ " changing the sword's effectiveness by " + ia.getIntValue());
            }
            else{
                updateBottomDisplay("", "The scroll of " + ia.name + " has no effect because no sword is wielded");
            }
        }
        else if(ia.getName().equalsIgnoreCase("Hallucinate")){
            hallucinateTrack += ia.getIntValue() + 1;
            updateBottomDisplay("", "Read the scroll of " + ia.name + ", causing player to hallucinate for " + ia.getIntValue() + " moves");
            updateGameDisplay();
            super.repaint();
            terminal.repaint();
        }
        else {
            usedScroll = false;
        }
        // Removing scroll after use
        if(usedScroll) {
            playerGlobal.getPack().remove(itemNum);
        }
    }


    public void wieldSword(int itemNum){

        if(playerGlobal.getPack().size() < itemNum + 1 || playerGlobal.getPack().get(itemNum) == null || playerGlobal.getPack().get(itemNum).getType() != ')') {
            updateBottomDisplay("", "There is no sword in that inventory slot");
            return;
        }
        //put on weapon
        playerGlobal.setWeapon(playerGlobal.getPack().get(itemNum));
        updateBottomDisplay("", "Put on sword " + playerGlobal.getWeapon());
    }

    public void dropItem(int itemNum){
        // the problem was array out of index error 
        // if array has 3 items, arr[4] results in seg fault (so does arr[3])
        // causing the code to just not run
        if(itemNum >= playerGlobal.getPack().size()) { // use this logic instead of arr.get(itemNum) == null
            updateBottomDisplay("", "There is no item in that inventory slot");
            return;
        }
        //insert below player
        updateBottomDisplay("", "Dropped item " + playerGlobal.getPack().get(itemNum));
        stackObjGrid[playerGlobalPosX][playerGlobalPosY].add(stackObjGrid[playerGlobalPosX][playerGlobalPosY].size() - 1, playerGlobal.getPack().get(itemNum));
        if(playerGlobal.getPack().get(itemNum).equals(playerGlobal.getWeapon())){
            playerGlobal.setWeapon(null);
        }
        if(playerGlobal.getPack().get(itemNum).equals(playerGlobal.getArmor())){
            playerGlobal.setArmor(null);
        }
        playerGlobal.getPack().remove(itemNum);
    }

    public void putOnArmor(int itemNum) {
            //check for errors
            if(playerGlobal.getArmor() != null){
                updateBottomDisplay("", "Need to take off Armor first with 'c'");
                return;
            }
            if(playerGlobal.getPack().size() < itemNum + 1 || playerGlobal.getPack().get(itemNum) == null || playerGlobal.getPack().get(itemNum).getType() != ']') {
                updateBottomDisplay("", "There is no armor in that inventory slot");
                return;
            }
            //put on armor
            playerGlobal.setArmor(playerGlobal.getPack().get(itemNum));
            updateBottomDisplay("", "Put on armor " + playerGlobal.getArmor());
    }

    public void pickUpItem(int x, int y){
        boolean isItemPickedUp = false;
        Displayable temp;
        for(int idx = stackObjGrid[x][y].size() - 1; idx >= 0 && !isItemPickedUp; idx--){
            System.out.println("Looking at item: "+ stackObjGrid[x][y].get(idx));
            if(stackObjGrid[x][y].get(idx).getType() == ']'){
                //get the object and remove it from the stack
                temp = stackObjGrid[x][y].get(idx);
                stackObjGrid[x][y].remove(idx);
                //if player has an armor, push it on the stack
                if (playerGlobal.getPack().size() > 9){
                    stackObjGrid[x][y].add(idx, playerGlobal.getPack().remove(0));
                    System.out.println("Overflowed Pack, dropped item 0");
                }
                //add armor to the pack
                playerGlobal.addToPack((Armor) temp);
                System.out.println("Picked up Armor");
                updateBottomDisplay("", "Picked up " + temp);
                isItemPickedUp = true;
            } else if(stackObjGrid[x][y].get(idx).getType()== ')'){
                //get the object and remove it from the stack
                temp = stackObjGrid[x][y].get(idx);
                stackObjGrid[x][y].remove(idx);
                //if player has an armor, push it on the stack
                if (playerGlobal.getPack().size() > 9){
                    stackObjGrid[x][y].add(idx, playerGlobal.getPack().remove(0));
                    System.out.println("Overflowed Pack, dropped item 0");
                }
                //add weapon to the pack
                playerGlobal.addToPack((Item) temp);
                System.out.println("Picked up Sword");
                updateBottomDisplay("", "Picked up " + temp);
                isItemPickedUp = true;
            } else if(stackObjGrid[x][y].get(idx).getType() == '?'){
                //get the object and remove it from the stack
                temp = stackObjGrid[x][y].get(idx);
                stackObjGrid[x][y].remove(idx);
                if (playerGlobal.getPack().size() > 9){
                    stackObjGrid[x][y].add(idx, playerGlobal.getPack().remove(0));
                    System.out.println("Overflowed Pack, dropped item 0");
                }
                //add scroll to the pack
                playerGlobal.addToPack((Scroll) temp);
                System.out.println("Picked up Scroll");
                updateBottomDisplay("", "Picked up " + temp);
                isItemPickedUp = true;
            }
        }
    }

    // Not used
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Comment out if it causes bugs
    public void fireUp() {
        initializeDisplay();
        this.setFocusable(true);
        if (this.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Displayable disp, int x, int y) {
        if ((0 <= x) && (x < objectGrid.length)) {
            if ((0 <= y) && (y < objectGrid[0].length)) {
                objectGrid[x][y] = disp;
                writeToTerminal(disp.getType(),x, y);
            }
        }
    }

    private void writeMsgToTerminal(String msg, int x, int y) {
        for(int msgInd = 0; msgInd < msg.length(); ++msgInd) {
            terminal.write(msg.charAt(msgInd), x++, y);
        }
        terminal.repaint();
    }

    private void clearLineOnTerminal(int y) {
        for(int x = 0; x < width; ++x) {
            terminal.write(' ', x, y);
        }
        terminal.repaint();
    }

    private void writeToTerminal(char sym, int x, int y) {
        if(hallucinateTrack <= 0) {
            terminal.write(sym, x, y);
            charObjGrid[x][y] = sym;
            terminal.repaint();
        } else{
            if(sym != '@') {
                int randomInt = ThreadLocalRandom.current().nextInt(0, 9 + 1);
                char randomChar = randoms[randomInt];
                terminal.write(randomChar, x, y);
            } else{
                terminal.write(sym, x, y);
            }
            charObjGrid[x][y] = sym;
            terminal.repaint();
        }
    }

}
