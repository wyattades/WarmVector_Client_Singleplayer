package Visual;

import Entities.Entity;
import Entities.Player;
import Main.Game;
import Map.GeneratedEnclosure;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 5/14/2015.
 */
public class HUD {

    private Player user;

    private final static int
            HUDx = (int) (Game.WIDTH - 20),
            HUDy = (int) (Game.HEIGHT - 40),
//            SCALE = (int)(Game.WIDTH/1080.0f),
            lifeBar_w = 150,
            lifeBar_h = 12,
            lifeBar_x = 110,
            lifeBar_y = HUDy + lifeBar_h / 2,
            lifeBar_offset = 3;

    private Area mapArea;

    //private GeneratedEnclosure map;

    public int enemies;

    public int startEnemies;

    private static final Color defaultHudColor = new Color(180, 180, 180, 220),
            nearDeathHudColor = new Color(200, 0, 0, 200),
            enemyBoxColor = new Color(255, 0, 0, 180);


    public HUD(HashMap<String, ArrayList<Entity>> entityList, GeneratedEnclosure map) {

        //this.map = map;

        user = (Player)entityList.get("thisPlayer").get(0);
        startEnemies = enemies = entityList.get("enemy").size();

        mapArea = map.region;

    }

    public void updateEnemyAmount(int amount) {
        enemies = Math.max(0, amount);
    }

    // TODO: replace this with vector image file?
    private static Polygon cross = new Polygon();
    static {
        int w = 10;
        cross.addPoint(-w, w / 2);
        cross.addPoint(-w, -w / 2);
        cross.addPoint(-w / 2, -w / 2);
        cross.addPoint(-w / 2, -w);
        cross.addPoint(w / 2, -w);
        cross.addPoint(w / 2, -w / 2);
        cross.addPoint(w, -w / 2);
        cross.addPoint(w, w / 2);
        cross.addPoint(w / 2, w / 2);
        cross.addPoint(w / 2, w);
        cross.addPoint(-w / 2, w);
        cross.addPoint(-w / 2, w / 2);
        cross.translate(16, HUDy + w + 2);
    }


    public void draw(Graphics2D g) {

        g.setFont(Theme.fontHUD);

        //AMMO
        String ammoS = user.weapon == null ? "" : "Ammo: " + user.weapon.ammo + " / " + user.weapon.reserveAmmo;
        g.setColor(defaultHudColor);
        g.drawString(ammoS, HUDx - textWidth(ammoS, g), HUDy + textHeight(ammoS, g) / 2);

        //LIFE
        if (user.life <= 10) g.setColor(nearDeathHudColor);

        //LIFE AMOUNT
        String lifeS = String.valueOf((int) user.life);
        g.drawString(lifeS, 28, HUDy + textHeight(lifeS, g) / 2);

        //HEALTH BAR
        g.setStroke(new BasicStroke(2));
        g.drawRect(lifeBar_x - lifeBar_offset, lifeBar_y - lifeBar_offset, lifeBar_w + 2 * lifeBar_offset, lifeBar_h + 2 * lifeBar_offset);
        g.setStroke(new BasicStroke(1));
        g.fillRect(lifeBar_x, lifeBar_y, (int) (user.life * lifeBar_w / user.maxLife), lifeBar_h);

        //CROSS SYMBOL
        g.fill(cross);

        //ENEMY COUNTER BOX
        g.setColor(enemyBoxColor);
        int side = 60;
        g.fillRect(Game.WIDTH / 2 - side/2, Game.HEIGHT - side, side, side);

        //ENEMY COUNTER
        g.setColor(new Color(255,255,255,180));
        String enemiesS = String.valueOf(enemies);
        g.drawString(enemiesS, Game.WIDTH/2 - textWidth(enemiesS, g) / 2 - 1, Game.HEIGHT - 16);

        //MINI MAP

        AffineTransform oldTransform = g.getTransform();

        g.translate(Game.WIDTH - 300, 20);
        g.scale(0.1f, 0.1f);

        g.setColor(Color.gray);
        g.fill(mapArea);

        g.setStroke(new BasicStroke(6));
        g.setColor(Color.white);
        g.draw(mapArea);

        g.setColor(Color.cyan);
        g.setStroke(new BasicStroke(0));
        g.fillOval((int) user.x-10, (int)user.y-10, 40, 40);

        g.setTransform(oldTransform);

    }

    private int textWidth(String t, Graphics2D g) {
        return (int) g.getFontMetrics().getStringBounds(t, g).getWidth();
    }

    private int textHeight(String t, Graphics2D g) {
        return (int) g.getFontMetrics().getStringBounds(t, g).getHeight();
    }


}
