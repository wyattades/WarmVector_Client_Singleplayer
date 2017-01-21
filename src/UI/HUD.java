package UI;

import Entities.EntityManager;
import Entities.Player.Enemy;
import Entities.Player.Player;
import Main.Window;
import Util.MyMath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 5/14/2015.
 */
public class HUD {

    // Private constants
    private static final double
            hudOffset = 15.0 * Window.SCALE,
            hudY = Window.HEIGHT - 20.0 * Window.SCALE - hudOffset,
            crossWidth = 35.0 * Window.SCALE,
            lifeBar_w = 200.0 * Window.SCALE,
            lifeBar_h = 24.0 * Window.SCALE,
            lifeBar_x = 2.0 * hudOffset + crossWidth,
            lifeBar_y = hudY - lifeBar_h * 0.5,
            lifeBar_offset = 3.0,
            miniMapScale = 0.1 * Window.SCALE,
            playerDotSize = 60.0 * Window.SCALE;
    private static final Polygon cross = new Polygon();
    static {
        int radius = (int)(crossWidth * 0.5),
            offset = (int)(crossWidth * 0.20);
        cross.addPoint(-radius, offset);
        cross.addPoint(-radius, -offset);
        cross.addPoint(-offset, -offset);
        cross.addPoint(-offset, -radius);
        cross.addPoint(offset, -radius);
        cross.addPoint(offset, -offset);
        cross.addPoint(radius, -offset);
        cross.addPoint(radius, offset);
        cross.addPoint(offset, offset);
        cross.addPoint(offset, radius);
        cross.addPoint(-offset, radius);
        cross.addPoint(-offset, offset);
        cross.translate((int)(hudOffset + radius), (int)(hudY));
    }
    private static final Color defaultHudColor = new Color(180, 180, 180, 220),
            nearDeathHudColor = new Color(200, 0, 0, 200),
            enemyBoxColor = new Color(255, 0, 0, 180),
            enemyCounterColor = new Color(255, 255, 255, 180);
    private static final Font HUD_FONT = new Font("Dotum Bold", Font.BOLD, (int)(crossWidth * 1.3));


    // Private variables
    private Area mapArea;
    private Player user;
    private List<Enemy> enemies;

    public HUD(EntityManager _entityManager, Map _map) {

        enemies = _entityManager.getEnemies();

        user = _entityManager.getThisPlayer();

        mapArea = _map.region;
    }

    public void draw(Graphics2D g) {
        if (user.state) {
            drawHud(g);
        } else {
            g.setColor(ButtonUI.COLOR_DEFAULT);
            g.setFont(ButtonUI.BUTTON_FONT);
            g.drawString("PRESS SPACE", 26, Window.HEIGHT - 92);
            g.drawString("TO RESTART", 26, Window.HEIGHT - 28);
        }
    }

    public void drawHud(Graphics2D g) {

        g.setFont(HUD_FONT);
        g.setColor(defaultHudColor);

        //AMMO
        if (user.weapon != null) {
            String ammoS = user.weapon.name + " Ammo: " + user.weapon.ammo + " / " + user.weapon.reserveAmmo;
            Rectangle2D ammoBounds = textBounds(ammoS, g);
            g.drawString(ammoS, (int)(Main.Window.WIDTH - ammoBounds.getWidth() - hudOffset), (int)(hudY + ammoBounds.getHeight() * 0.3));
        }

        //LIFE
        if (user.life <= 10) g.setColor(nearDeathHudColor);

        //LIFE AMOUNT
        String lifeS = String.valueOf((int) user.life);
        g.drawString(lifeS, (int)(lifeBar_x + lifeBar_w + hudOffset), (int)(hudY + textBounds(lifeS, g).getHeight() * 0.3));

        //HEALTH BAR
        g.setStroke(new BasicStroke(2));
        g.drawRect((int)(lifeBar_x), (int)(lifeBar_y), (int)(lifeBar_w), (int)(lifeBar_h));
        g.setStroke(new BasicStroke(1));
        g.fillRect((int)(lifeBar_x +lifeBar_offset), (int)(lifeBar_y + lifeBar_offset), (int) (user.life * lifeBar_w / user.maxLife - 2.0 * lifeBar_offset), (int)(lifeBar_h - 2.0 * lifeBar_offset));

        //CROSS SYMBOL
        g.fill(cross);

        //ENEMY COUNTER BOX
        g.setColor(enemyBoxColor);
        double side = 2.0 * (Window.HEIGHT - hudY);
        g.fillRect((int)(Window.WIDTH * 0.5 - side * 0.5), (int)(Window.HEIGHT - side), (int)side, (int)side);

        //ENEMY COUNTER
        g.setColor(enemyCounterColor);
        String enemiesS = String.valueOf(enemies.size());
        Rectangle2D enemiesBounds = textBounds(enemiesS, g);
        g.drawString(enemiesS, (int)(Window.WIDTH * 0.5 - enemiesBounds.getWidth() * 0.5 - 2.0), (int) (hudY + enemiesBounds.getHeight() * 0.3));

        //MINI MAP
        AffineTransform oldTransform = g.getTransform();

        g.translate(hudOffset, hudOffset);
        g.scale(miniMapScale, miniMapScale);

        g.setColor(Color.gray);
        g.fill(mapArea);

        g.setStroke(new BasicStroke(6));
        g.setColor(Color.white);
        g.draw(mapArea);

        drawPlayer(user, g, Color.cyan);

        if (enemies.size() <= 3) {
            for (Enemy enemy : enemies) {
                drawPlayer(enemy, g, Color.red);
            }
        }

        g.setTransform(oldTransform);

    }

    private void drawPlayer(Player p, Graphics2D g, Color c) {
        g.setColor(c);
        g.setStroke(new BasicStroke(0));
        g.fillOval(MyMath.round(p.x - playerDotSize * 0.25), MyMath.round(p.y - playerDotSize * 0.25), (int)playerDotSize, (int)playerDotSize);
    }

    private Rectangle2D textBounds(String text, Graphics2D g) {
        return g.getFontMetrics().getStringBounds(text, g);
    }

}
