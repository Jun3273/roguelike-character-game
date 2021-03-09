package game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyStrokeReader implements InputObserver, Runnable {

    private static String CLASSID = "KeyStrokeReaser";
    private static Queue<Character> inputQueue = null;
    private ObjectDisplayGrid displayGrid;

    public KeyStrokeReader(ObjectDisplayGrid grid) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
    }

    @Override
    public void observerUpdate(char ch) {
        System.out.println("Received character: " + ch);
    }

    private void rest() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean processInput() {
        char ch;
        boolean processing = true;
        while(processing) {
            if(inputQueue.peek() == null) {
                processing = false;
            } else {
                ch = inputQueue.poll();
                System.out.println("Char:"+ch+" is being processed");
            }
        }
        return true;
    }

    @Override
    public void run() {
        System.out.println("KeyStrokeReader is working....");
        displayGrid.registerInputObserver(this);
        boolean working = true;
        while(working) {
            rest();
            working = processInput();
        }
    }

}
