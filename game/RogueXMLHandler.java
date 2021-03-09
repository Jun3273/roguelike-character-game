package game;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


//TODO: need to fix Room.setCreature() and add Room.setItem() implementations

// import jdk.vm.ci.code.site.Site;

import java.util.ArrayList;
import java.util.Stack;

public class RogueXMLHandler extends DefaultHandler {

    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "RogueXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    // When the parser parses the file it will add references to
    // Student objects to this array so that it has a list of
    // all specified students.  Had we covered containers at the
    // time I put this file on the web page I would have made this
    // an ArrayList of Students (ArrayList<Student>) and not needed
    // to keep tract of the length and maxStudents.  You should use
    // an ArrayList in your project.
    private ArrayList<Displayable> displayable;
    private Dungeon dungeon;
    private ObjectDisplayGrid objdisplay;
    private ArrayList<Room> rooms;
    private ArrayList<Passage> passages;
    private Stack<Displayable> dispStack; // to keep track of what Disp obj is used

    // The XML file contains a list of Students, and within each
    // Student a list of activities (clubs and classes) that the
    // student participates in.  When the XML file initially
    // defines a student, many of the fields of the object have
    // not been filled in.  Additional lines in the XML file
    // give the values of the fields.  Having access to the
    // current Student and Activity allows setters on those
    // objects to be called to initialize those fields.
    private Displayable displayableBeingParsed = null;
    private Room roomBeingParsed = null;
    private Structure structureBeingParsed = null;
    private Creature creatureBeingParsed = null;
    private Item itemBeingParsed = null;
    private Magic magicBeingParsed = null;
    private Action actionBeingParsed = null;
    private Room multipleRoomsBeingParsed = null;
    private Passage passageBeingParsed = null;

    // The bX fields here indicate that at corresponding field is
    // having a value defined in the XML file.  In particular, a
    // line in the xml file might be:
    // <instructor>Brook Parke</instructor>
    // The startElement method (below) is called when <instructor>
    // is seen, and there we would set bInstructor.  The endElement
    // method (below) is called when </instructor> is found, and
    // in that code we check if bInstructor is set.  If it is,
    // we can extract a string representing the instructor name
    // from the data variable above.
    private boolean bAction = false;
    private boolean bArmor = false;
    private boolean bCreature = false;
    private boolean bCreatureAction = false;
    private boolean bDisplayable = false;
    private boolean bDungeon = false;
    private boolean bItem = false;
    private boolean bItemAction = false;
    private boolean bMagic = false;
    private boolean bMonster = false;
    private boolean bObjectDisplayGrid = false;
    private boolean bPassage = false;
    private boolean bPlayer = false;
    private boolean bRoom = false;
    private boolean bScroll = false;
    private boolean bStructure = false;
    private boolean bSword = false;
    private boolean bVisible = false;
    private boolean bPosX = false;
    private boolean bPosY = false;
    private boolean bWidth = false;
    private boolean bHeight = false;
    private boolean bType = false;
    private boolean bHP = false;
    private boolean bMaxHit = false;
    private boolean bActionMessage = false;
    private boolean bHPMove = false;
    private boolean bItemIntValue = false;
    private boolean isbType = false;
    private boolean bActionIntValue = false;
    private boolean bActionCharValue = false;

    // Used by code outside the class to get the list of Student objects
    // that have been constructed.
    public ArrayList<Displayable> getDisplayable() {
        return displayable;
    }

    public ObjectDisplayGrid getObjdisplay() {
        return objdisplay;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its
    // functionality by deriving from it!
    public RogueXMLHandler() {
        displayable = new ArrayList<Displayable>();
        dungeon = new Dungeon();
        objdisplay = new ObjectDisplayGrid();
        rooms = new ArrayList<Room>();
        passages = new ArrayList<Passage>();
        dispStack = new Stack<Displayable>();
    }

    // startElement is called when a <some element> is called as part of
    // <some element> ... </some element> start and end tags.
    // Rather than explain everything, look at the xml file in one screen
    // and the code below in another, and see how the different xml elements
    // are handled.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if (qName.equalsIgnoreCase("Dungeon")) {
            //get info of dungeon
            System.out.println("Start of dungeon");
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            //create the dungeon
            dungeon = dungeon.getDungeon(name, width, gameHeight);
            objdisplay = new ObjectDisplayGrid().getObjectDisplayGrid(gameHeight, width, topHeight, bottomHeight);
            // displayable = new ArrayList<Displayable>();
            bDungeon = true;
            bObjectDisplayGrid = true;
        }
        else if (qName.equalsIgnoreCase("Rooms")) {
            //displayable = new ArrayList();
            Displayable dummy = new Displayable();
            dispStack.add(dummy); // adding dummy to not get peek() error
            //set the parser object to the room
            // multipleRoomsBeingParsed = ;
            // displayableBeingParsed = rooms;
            System.out.println("Start of rooms");
        }
        else if (qName.equalsIgnoreCase("Room")) {
            //get room info
            String roomName = attributes.getValue("room");
            Room room = new Room(roomName);
            room.setId(Integer.parseInt(roomName));
            rooms.add(room);
            dungeon.addRoom(room);
            //set the parser object to the room
            structureBeingParsed = room;
            dispStack.push(room);
            displayableBeingParsed = room;
            roomBeingParsed = room;
            bRoom = true;
        }
        else if (qName.equalsIgnoreCase("Passages")) {
            //displayable = new ArrayList();
            Displayable dummy = new Displayable();
            dispStack.add(dummy); // adding dummy to not get peek() error
            //set the parser object to the room
            // multipleRoomsBeingParsed = ;
            // displayableBeingParsed = rooms;
            System.out.println("Start of passages");
        }
        else if (qName.equalsIgnoreCase("Passage")) {
            //get room info
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));
            Passage passage = new Passage();
            passage.setID(room1, room2);
            passages.add(passage);
            dungeon.addPassage(passage);
            //set the parser object to the room
            structureBeingParsed = passage;
            dispStack.push(passage);
            displayableBeingParsed = passage;
            passageBeingParsed = passage;
            bPassage = true;
        } else if (qName.equalsIgnoreCase("Monster")) {
            String monsterName = attributes.getValue("name");
            int roomName = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Monster monster = new Monster();
            monster.setID(roomName, serial);
            monster.setName(monsterName);
            roomBeingParsed.setCreature(monster);
            dungeon.addCreature(monster);
            //set parser and display to the monster
            creatureBeingParsed = monster;
            //structureBeingParsed.setCreature(monster);
            dispStack.push(monster);
            displayableBeingParsed = monster;
            bMonster = true;
        } else if (qName.equalsIgnoreCase("Player")) {
            String playerName = attributes.getValue("name");
            int roomName = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            // ^Don't know where to store these 
            Player player = new Player();
            dungeon.addCreature(player);
            roomBeingParsed.setCreature(player);
            //set partser and displaybale to the player
            creatureBeingParsed = player;
            //structureBeingParsed.setCreature(player);
            displayableBeingParsed = player;
            dispStack.push(player);
            bPlayer = true;
        }
        else if (qName.equalsIgnoreCase("Armor")) {
            String armorName = attributes.getValue("name");
            int roomName = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Armor armor = new Armor(armorName);
            armor.setName(armorName);
            armor.setID(roomName, serial);
            armor.setOwner(creatureBeingParsed);
            //creatureBeingParsed.setArmor(armor);
            itemBeingParsed = armor;
            dispStack.push(armor);
            displayableBeingParsed = armor;
            bArmor = true;
        }
        else if (qName.equalsIgnoreCase("Sword")) {
            String swordName = attributes.getValue("name");
            int roomName = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Sword sword = new Sword(swordName);
            sword.setID(roomName, serial);
            sword.setOwner(creatureBeingParsed);
            itemBeingParsed = sword;
            dispStack.push(sword);
            displayableBeingParsed = sword;
            bSword = true;
        }
        else if (qName.equalsIgnoreCase("Scroll")) {
            String scrollName = attributes.getValue("name");
            int roomName = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Scroll scroll = new Scroll(scrollName);
            scroll.setID(roomName, serial);
            itemBeingParsed = scroll;
            dispStack.push(scroll);
            displayableBeingParsed = scroll;
            bScroll = true;
        }
        else if (qName.equalsIgnoreCase("CreatureAction")) {
            String actionName = attributes.getValue("name");
            String type = attributes.getValue("type");
            CreatureAction creatureAction = new CreatureAction(creatureBeingParsed);
            creatureAction.setName(actionName);
            creatureAction.setType(type);
            actionBeingParsed = creatureAction;
            bCreatureAction = true;
        }
        else if (qName.equalsIgnoreCase("ItemAction")) {
            String actionName = attributes.getValue("name");
            String type = attributes.getValue("type");
            ItemAction itemAction = new ItemAction(itemBeingParsed);
            itemAction.setName(actionName);
            itemAction.setType(type);
            actionBeingParsed = itemAction;
            bItemAction = true;
        }
        //single line tags
        else if (qName.equalsIgnoreCase("PosX")) {
            bPosX = true;
        }
        else if (qName.equalsIgnoreCase("PosY")) {
            bPosY = true;
        }
        else if (qName.equalsIgnoreCase("Width")) {
            bWidth = true;
        }
        else if (qName.equalsIgnoreCase("Height")) {
            bHeight = true;
        }
        else if (qName.equalsIgnoreCase("Visible")) {
            bVisible = true;
        }
        else if(qName.equalsIgnoreCase("ItemIntValue")) {
            bItemIntValue = true;
        }
        else if(qName.equalsIgnoreCase("actionMessage")) {
            bActionMessage = true;
        }
        else if(qName.equalsIgnoreCase("type")) {
            bType = true;
        }
        else if(qName.equalsIgnoreCase("hp")) {
            bHP = true;
        }
        else if(qName.equalsIgnoreCase("maxhit")) {
            bMaxHit = true;
        }
        else if(qName.equalsIgnoreCase("hpMoves")) {
            bHPMove = true;
        }
        else if (qName.equalsIgnoreCase("actionIntValue")) {
            bActionIntValue = true;
        }
        else if (qName.equalsIgnoreCase("actionCharValue")) {
            bActionCharValue = true;
        }
        else{
            System.out.println("Unknown parser: " + qName);
        }



        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        boolean isUnknown = true;
        try {
            if (qName.equalsIgnoreCase("rooms")) {
                // multipleRoomsBeingParsed.addRoom((Room) structureBeingParsed); //add the room to the dungeon
                displayable.add(displayableBeingParsed); // add the room to displayble
                structureBeingParsed = null; //structure parsed is null
                isUnknown = false;
            }else if (qName.equalsIgnoreCase("room")) {
                displayable.add(displayableBeingParsed); // add the room to displayble
                structureBeingParsed = null; //structure parsed is null
                // TODO: for some reason there is a null pointer exception to roomBeingParsed
                roomBeingParsed = null;
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                isUnknown = false;
            }else if (qName.equalsIgnoreCase("Passages")) {
                // multipleRoomsBeingParsed.addRoom((Room) structureBeingParsed); //add the room to the dungeon
                displayable.add(displayableBeingParsed); // add the room to displayble
                structureBeingParsed = null; //structure parsed is null
                isUnknown = false;
            }else if (qName.equalsIgnoreCase("Passage")) {
                displayable.add(displayableBeingParsed); // add the room to displayble
                structureBeingParsed = null; //structure parsed is null
                passageBeingParsed = null;
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                isUnknown = false;
            }
            else if (qName.equalsIgnoreCase("Monster")) {
                // roomBeingParsed.setCreature(creatureBeingParsed); //add Monster to the structure (should be "AddCreature" I think but thats not a method)
                displayable.add(displayableBeingParsed); //add creature to displayble
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                creatureBeingParsed = null;
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("Player")) {
                displayable.add(displayableBeingParsed); //add player to displayble
                //doesnt have to be ina  sctrucutre, see playerkill
                //structureBeingParsed.setCreature(creatureBeingParsed);  //add player to the strucutere (should be "AddCreature" I think but thats not a method)
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                creatureBeingParsed = null;
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("Armor")) {
                displayable.add(displayableBeingParsed); //add item to displayable
                dungeon.addItem(itemBeingParsed); //add item to the dungeon
                roomBeingParsed.setItem(itemBeingParsed);
                itemBeingParsed = null;
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("Scroll")) {
                displayable.add(displayableBeingParsed); //add item to displayable
                dungeon.addItem(itemBeingParsed); //add item to the dungeon
                roomBeingParsed.setItem(itemBeingParsed);
                itemBeingParsed = null;
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("Sword")) {
                displayable.add(displayableBeingParsed); //add item to displayable
                dungeon.addItem(itemBeingParsed); //add item to the dungeon
                roomBeingParsed.setItem(itemBeingParsed);
                itemBeingParsed = null;
                dispStack.pop();
                displayableBeingParsed = dispStack.peek();
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("CreatureAction")) {
                if(actionBeingParsed.getType().equalsIgnoreCase("Death")){
                    creatureBeingParsed.setDeathAction((CreatureAction) actionBeingParsed);
                }
                else if(actionBeingParsed.getType().equalsIgnoreCase("hit")){
                    creatureBeingParsed.setHitAction((CreatureAction) actionBeingParsed);
                }
                actionBeingParsed = null;
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("ItemAction")) {
                itemBeingParsed.setItemAction((ItemAction) actionBeingParsed);
                actionBeingParsed = null;
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("Dungeon")) {
                //do nothing
                System.out.println("End of xml");
                isUnknown = false;
            } else if (qName.equalsIgnoreCase("Rooms")) {
                //do nothing
                System.out.println("End of rooms");
                isUnknown = false;
            }
            //single line tags
            if(displayableBeingParsed != null) {
                if (bPosX) {
                    displayableBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    bPosX = false;
                    isUnknown = false;
                } else if (bPosY) {
                    displayableBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    bPosY = false;
                    isUnknown = false;
                } else if (bWidth) {
                    displayableBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    bWidth = false;
                    isUnknown = false;
                } else if (bHeight) {
                    displayableBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    bHeight = false;
                    isUnknown = false;
                } else if (bVisible) {
                    displayableBeingParsed.setVisible();
                    bVisible = false;
                    isUnknown = false;
                } else if (bType) {
                    displayableBeingParsed.setType(data.toString().charAt(0));
                    bType = false;
                    isUnknown = false;
                } else if (bHP) {
                    displayableBeingParsed.setHp(Integer.parseInt(data.toString()));
                    bHP = false;
                    isUnknown = false;
                } else if (bMaxHit) {
                    displayableBeingParsed.setMaxHit(Integer.parseInt(data.toString()));
                    bMaxHit = false;
                    isUnknown = false;
                } else if (bHPMove) {
                    displayableBeingParsed.setHpMove(Integer.parseInt(data.toString()));
                    bHPMove = false;
                    isUnknown = false;
                } else if (bItemIntValue) {
                    itemBeingParsed.setIntValue(Integer.parseInt(data.toString())); // Not sure if this is needed
                    bItemIntValue = false;
                    isUnknown = false;
                    }
            }
            if(actionBeingParsed != null) {
                    if (bActionMessage) {
                        actionBeingParsed.setMessage(data.toString());
                        bActionMessage = false;
                        isUnknown = false;
                    } else if (bActionCharValue) {
                        actionBeingParsed.setCharValue((char) data.toString().charAt(0));
                        bActionCharValue = false;
                        isUnknown = false;
                    } else if (bActionIntValue) {
                        actionBeingParsed.setIntValue(Integer.parseInt(data.toString()));
                        bActionIntValue = false;
                        isUnknown = false;
                    }
            }
            if (isUnknown) {
                System.out.println("Unknown end tag: " + qName);
            }
        } catch (NullPointerException e){
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

    @Override
    public String toString() {
        String str = "RogueXMLHandler\n";
        str += "   Dungeon: " + dungeon.toString() + "\n";
        str += "   displaybleCount: " + displayable.size() + "\n";
        str +=    displayable.toString() + "\n";
        str += "   ObjectDisplayGrid: " + objdisplay.toString() + "\n";
        return str;
    }
}
