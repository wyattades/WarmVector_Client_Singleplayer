package StaticManagers;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */
public class InputManager implements MouseListener, KeyListener, MouseMotionListener {

    //TODO: have a sendEvent() function that wakes inputHandle(Event e) with an Event e (so it only runs when there's an event)

    class Key {
        public boolean pressed;
        public int keyCode, pressCount, pressTime;

        public Key(int _keyCode) {
            keyCode = _keyCode;
            pressed = false;
            pressTime = 0;
        }

        public void toggle(boolean toggle) {
            if (pressed != toggle) {
                pressed = toggle;
            }
            if (pressed) {
                pressCount++;
            }
        }
    }

    class Click {
        public int mouseCode, clickCount, pressCount, pressTime;
        public boolean pressed, clicked;

        public Click(int _mouseCode) {
            mouseCode = _mouseCode;
            pressed = false;
            clicked = false;
            pressTime = 0;
        }

        public void togglePressed(boolean toggle) {
            if (pressed != toggle) {
                pressed = toggle;
            }
            if (pressed) {
                pressCount++;
            }
        }

        public void toggleClick(boolean toggle) {
            if (clicked != toggle) {
                clicked = true;
            }
            if (clicked) {
                clickCount++;
            }
        }

    }

    public class Mouse {

        public int x, y, dx, dy, px, py;
        public boolean dragged, inScreen;

        public Mouse() {
            dragged = false;
            inScreen = true;
        }

    }

    public Mouse mouse;
    private Map<String, Key> keys;
    private Map<String, Click> clicks;

    public InputManager(Canvas c) {
        c.addKeyListener(this);
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
        mouse = new Mouse();
        keys = new HashMap<>();
        clicks = new HashMap<>();
    }

    public void addKeyMapping(String s, int keyCode) {
        keys.put(s, new Key(keyCode));
    }

    public void addMouseMapping(String s, int mouseCode) {
        clicks.put(s, new Click(mouseCode));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Click click : clicks.values()) {
            if (e.getButton() == click.mouseCode) {
                click.togglePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Click click : clicks.values()) {
            if (e.getButton() == click.mouseCode) {
                click.togglePressed(false);
                break;
            }
        }
    }

    public boolean isKeyPressed(String s) {
        return keys.get(s).pressed;
    }

    public int getKeyTime(String s) {
        return keys.get(s).pressTime;
    }

    public int getMouseTime(String s) {
        return clicks.get(s).pressTime;
    }

    public void setKeyTime(String s, int n) {
        keys.get(s).pressTime = n;
    }

    public void setMouseTime(String s, int n) {
        clicks.get(s).pressTime = n;
    }

    public boolean isMouseClicked(String s) {
        Click click = clicks.get(s);
        if (click.clicked) {
            click.clicked = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean isMousePressed(String s) {
        return clicks.get(s).pressed;
    }

//    public boolean isMouseDown(String s) {
//        for (Click click : clicks) {
//            if (s.equals(click.name)) {
//                return click.mouseDown;
//            }
//        }
//        return false;
//    }
//
//    public boolean isMouseUp(String s) {
//        for (Click click : clicks) {
//            if (s.equals(click.name)) {
//                return click.mouseReleased;
//            }
//        }
//        return false;
//    }

    @Override
    public void keyPressed(KeyEvent e) {

        for (Key key : keys.values()) {
            if (e.getKeyCode() == key.keyCode) {
                key.toggle(true);
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        for (Key key : keys.values()) {
            if (e.getKeyCode() == key.keyCode) {
                key.toggle(false);
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        for (Click click : clicks.values()) {
            if (e.getButton() == click.mouseCode) {
                click.toggleClick(true);
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouse.inScreen = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouse.inScreen = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouse.dragged = true;
        mouse.x = e.getX();
        mouse.y = e.getY();
        mouse.dx = mouse.x - mouse.px;
        mouse.dy = mouse.y - mouse.py;
        mouse.px = mouse.x;
        mouse.py = mouse.y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.dragged = false;
        mouse.x = e.getX();
        mouse.y = e.getY();
        mouse.dx = mouse.x - mouse.px;
        mouse.dy = mouse.y - mouse.py;
        mouse.px = mouse.x;
        mouse.py = mouse.y;
    }
}
