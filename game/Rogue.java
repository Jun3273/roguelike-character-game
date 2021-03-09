package game;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Rogue implements Runnable {
    
    private static final int DEBUG = 0;
    private boolean isRunning;
    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private static Dungeon dungeon = null;
    private Thread keyStrokeReader;

    @Override
    public void run() {
        displayGrid.fireUp();
    }

    public static void main(String[] args) throws Exception {
        // We're required to use input arguements as filename
        String fileName = null;
        switch (args.length) {
            case 1:
                fileName = "xmlfiles/" + args[0];
                break;
            default:
                System.out.println("java Rogue <xmlfilename>");
                return;
        }
        //fileName = "src/xmlfiles/" + "badScroll.xml";
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            RogueXMLHandler handler = new RogueXMLHandler();
            saxParser.parse(new File(fileName), handler);
            ArrayList<Displayable> displayables = handler.getDisplayable();
            displayGrid = handler.getObjdisplay();
            displayGrid.setDisplayables(displayables);
            dungeon = handler.getDungeon();
            displayGrid.setDungeon(dungeon);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        }

        Rogue rogue = new Rogue();
        Thread rogueThread = new Thread(rogue);
        rogueThread.start();

        //KeyStrokeReader keyStrokeReader = new KeyStrokeReader(displayGrid);
        rogue.keyStrokeReader = new Thread(new KeyStrokeReader(displayGrid));
        rogue.keyStrokeReader.start();

       // keyStrokeReader.run();

        rogueThread.join();
        rogue.keyStrokeReader.join();

    }
}
