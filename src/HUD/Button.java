package HUD;

import java.awt.*;

/**
 * Created by Wyatt on 3/7/2015.
 */
public class Button {

    private int x,y,w,h;
    private Color c1,c2;
    private String text;

    public Button(String text, int x, int y, int w, int h) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void action() {}

    public void draw(Graphics2D g, MouseCursor cursor) {
        if (!overBox(cursor)) {
            g.setColor(c1);
            //g.setStroke(c2);
        } else {
            g.setColor(c2);
            //g.setStroke(c1);
        }
        g.drawRect(x-(w/2),y-(h/2),w,h);
        //text goes here
    }

    private boolean overBox(MouseCursor cursor) {
        if (cursor.x > x-w/2 && cursor.x < x+w/2 &&
                cursor.y > y-w/2 && cursor.y < y+w/2) return true;
        return false;
    }
}
