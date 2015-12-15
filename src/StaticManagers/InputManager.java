package StaticManagers;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */
public class InputManager implements MouseListener, KeyListener, MouseMotionListener {


    //TODO: have a sendEvent() function that wakes inputHandle(Event e) with an Event e (so it only runs when there's an event)

    class Key {
        public boolean pressed;
        public int keyCode, pressCount, pressTime;
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

    class Click {
        public String name;
        public int mouseCode, clickCount, pressCount, pressTime;
        public boolean pressed, clicked;

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

        public int x, y, dx, dy, px, py;
        public boolean dragged, inScreen;

        public Mouse() {
        }

    }

    public Mouse mouse;
    private ArrayList<Key> keys;
    private ArrayList<Click> clicks;

    public InputManager(Canvas c) {
        c.addKeyListener(this);
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
        mouse = new Mouse();
        keys = new ArrayList<>();
        clicks = new ArrayList<>();
    }

    public void addKeyMapping(String s, int keyCode) {
        keys.add(new Key(s, keyCode));
    }

    public void addMouseMapping(String s, int mouseCode) {
        clicks.add(new Click(s, mouseCode));
    }

    @Override
    public void mousePressed(MouseEvent e) {

        for (Click click : clicks) {
            if (e.getButton() == click.mouseCode) {
                click.togglePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        for (Click click : clicks) {
            if (e.getButton() == click.mouseCode) {
                click.togglePressed(false);
            }
        }
    }

    public boolean isKeyPressed(String s) {
        for (Key key : keys) {
            if (s.equals(key.name)) {
                return key.pressed;
            }
        }
        return false;
    }

    public int getKeyTime(String s) {
        for (Key key : keys) {
            if (s.equals(key.name)) {
                return key.pressTime;
            }
        }
        return 0;
    }

    public int getMouseTime(String s) {
        for (Click click : clicks) {
            if (s.equals(click.name)) {
                return click.pressTime;
            }
        }
        return 0;
    }

    public void setKeyTime(String s, int n) {
        for (Key key : keys) {
            if (s.equals(key.name)) {
                key.pressTime = n;
            }
        }
    }

    public void setMouseTime(String s, int n) {
        for (Click click : clicks) {
            if (s.equals(click.name)) {
                click.pressTime = n;
            }
        }
    }

    public boolean isMouseClicked(String s) {
        for (Click click : clicks) {
            if (s.equals(click.name)) {
                if (click.clicked) {
                    click.clicked = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isMousePressed(String s) {
        for (Click click : clicks) {
            if (s.equals(click.name)) {
                return click.pressed;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        for (Key key : keys) {
            if (e.getKeyCode() == key.keyCode) {
                key.toggle(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        for (Key key : keys) {
            if (e.getKeyCode() == key.keyCode) {
                key.toggle(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        for (Click click : clicks) {
            if (e.getButton() == click.mouseCode) {
                click.toggleClick(true);
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
