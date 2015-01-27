package Manager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Wyatt on 12/29/2014.
 */
public class InputManager implements MouseListener,KeyListener,MouseMotionListener {

    protected class Key {
        public boolean pressed;
        public int keyCode,pressCount,pressTime;
        public String name;

        public Key(String name, int keyCode) {
            this.name = name;
            this.keyCode = keyCode;
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

    protected class Click {
        public String name;
        public int mouseCode,clickCount,pressCount, pressTime;
        public boolean pressed,clicked;

        public Click(String name, int mouseCode) {
            this.name = name;
            this.mouseCode = mouseCode;
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

        public int x, y;
        public boolean dragged, inScreen;

        public Mouse() {}

    }

    public static Mouse mouse;
    public static ArrayList<Key> keys;
    public static ArrayList<Click> clicks;

    public InputManager(Canvas c) {
        c.addKeyListener(this);
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
        mouse = new Mouse();
        keys = new ArrayList<Key>();
        clicks = new ArrayList<Click>();
    }

    public void addKeyMapping(String s, int keyCode) {
        keys.add(new Key(s,keyCode));
    }

    public void addMouseMapping(String s, int mouseCode) {
        clicks.add(new Click(s,mouseCode));
    }

    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < clicks.size(); i++) {
            if (e.getButton() == clicks.get(i).mouseCode) {
                clicks.get(i).togglePressed(true);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (int i = 0; i < clicks.size(); i++) {
            if (e.getButton() == clicks.get(i).mouseCode) {
                clicks.get(i).togglePressed(false);
            }
        }
    }

    public static boolean isKeyPressed(String s) {
        for (int i = 0; i < keys.size(); i++) {
            if (s.equals(keys.get(i).name)) {
                return keys.get(i).pressed;
            }
        }
        return false;
    }

    public static int getKeyTime(String s) {
        for (int i = 0; i < keys.size(); i++) {
            if (s.equals(keys.get(i).name)) {
                return keys.get(i).pressTime;
            }
        }
        return 0;
    }

    public static int getMouseTime(String s) {
        for (int i = 0; i < clicks.size(); i++) {
            if (s.equals(clicks.get(i).name)) {
                return clicks.get(i).pressTime;
            }
        }
        return 0;
    }

    public static void setKeyTime(String s, int n) {
        for (int i = 0; i < keys.size(); i++) {
            if (s.equals(keys.get(i).name)) {
                keys.get(i).pressTime = n;
            }
        }
    }

    public static void setMouseTime(String s, int n) {
        for (int i = 0; i < clicks.size(); i++) {
            if (s.equals(clicks.get(i).name)) {
                clicks.get(i).pressTime = n;
            }
        }
    }

    public static boolean isMouseClicked(String s) {
        for (int i = 0; i < clicks.size(); i++) {
            if (s.equals(clicks.get(i).name)) {
                if (clicks.get(i).clicked) {
                    clicks.get(i).clicked = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isMousePressed(String s) {
        for (int i = 0; i < clicks.size(); i++) {
            if (s.equals(clicks.get(i).name)) {
                return clicks.get(i).pressed;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (int i = 0; i < keys.size(); i++) {
            if (e.getKeyCode() == keys.get(i).keyCode) {
                keys.get(i).toggle(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (int i = 0; i < keys.size(); i++) {
            if (e.getKeyCode() == keys.get(i).keyCode) {
                keys.get(i).toggle(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < clicks.size(); i++) {
            if (e.getButton() == clicks.get(i).mouseCode) {
                clicks.get(i).toggleClick(true);
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.dragged = false;
        mouse.x = e.getX();
        mouse.y = e.getY();
    }
}
