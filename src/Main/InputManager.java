package Main;

import GameState.GameStateManager;
import Util.MyInputEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */
class InputManager implements MouseListener, KeyListener, MouseMotionListener {

    private BlockingQueue<MyInputEvent> eventQueue;
    private static final int QUEUE_SIZE = 16;

    InputManager(Canvas canvas) {
        canvas.addMouseListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseMotionListener(this);

        eventQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    }
    
    ArrayList<MyInputEvent> getEvents() {
        ArrayList<MyInputEvent> events = new ArrayList<>();
        eventQueue.drainTo(events);
        return events;
    }

    private void newEvent(MyInputEvent e) {
        try {
           eventQueue.add(e);
        } catch(Exception exception) {
            System.err.println("BlockingQueue Exception: queue has overflowed");
        }
    }

    public void mouseMoved(MouseEvent e) {
        newEvent(new MyInputEvent(MyInputEvent.MOUSE_MOVE, e.getX(), e.getY()));
    }

    public synchronized void mousePressed(MouseEvent e) {
        newEvent(new MyInputEvent(MyInputEvent.MOUSE_DOWN, e.getButton()));
    }

    public synchronized void mouseReleased(MouseEvent e) {
        newEvent(new MyInputEvent(MyInputEvent.MOUSE_UP, e.getButton()));
    }

    public synchronized void keyPressed(KeyEvent e) {
        newEvent(new MyInputEvent(MyInputEvent.KEY_DOWN, e.getKeyCode()));
    }

    public synchronized void keyReleased(KeyEvent e) {
        newEvent(new MyInputEvent(MyInputEvent.KEY_UP, e.getKeyCode()));
    }

    public synchronized void mouseDragged(MouseEvent e) {
        newEvent(new MyInputEvent(MyInputEvent.MOUSE_MOVE, e.getX(), e.getY()));
    }

    public void keyTyped(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}


}
