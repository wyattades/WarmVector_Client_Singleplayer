package Main;

import GameState.GameStateManager;
import Util.MyInputEvent;

import java.awt.*;
import java.awt.event.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */
class InputManager implements MouseListener, KeyListener, MouseMotionListener {

    private GameStateManager gsm;

    InputManager(Canvas canvas, GameStateManager _gsm) {
        canvas.addMouseListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseMotionListener(this);

        gsm = _gsm;
    }

    public void mouseMoved(MouseEvent e) {
        gsm.inputHandle(new MyInputEvent(MyInputEvent.MOUSE_MOVE, e.getX(), e.getY()));
    }

    public void mousePressed(MouseEvent e) {
        gsm.inputHandle(new MyInputEvent(MyInputEvent.MOUSE_DOWN, e.getButton()));
    }

    public void mouseReleased(MouseEvent e) {
        gsm.inputHandle(new MyInputEvent(MyInputEvent.MOUSE_UP, e.getButton()));
    }

    public void keyPressed(KeyEvent e) {
        gsm.inputHandle(new MyInputEvent(MyInputEvent.KEY_DOWN, e.getKeyCode()));
    }

    public void keyReleased(KeyEvent e) {
        gsm.inputHandle(new MyInputEvent(MyInputEvent.KEY_UP, e.getKeyCode()));
    }

    public void mouseDragged(MouseEvent e) {
        gsm.inputHandle(new MyInputEvent(MyInputEvent.MOUSE_MOVE, e.getX(), e.getY()));
    }

    public void keyTyped(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}


}
