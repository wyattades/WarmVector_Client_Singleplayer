package Visual;

import Entity.Player;
import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 5/14/2015.
 */
public class HUD {

    private Player user;

    private final static int
            HUDx = (int) (Game.WIDTH - 20),
            HUDy = (int) (Game.HEIGHT - 40),
            lifeBar_w = 150,
            lifeBar_h = 12,
            lifeBar_x = 110,
            lifeBar_y = HUDy + lifeBar_h / 2,
            lifeBar_offset = 3;

    public int enemies;

    public int startEnemies;

    private static Color defaultHudColor = new Color(100, 100, 100, 200),
            nearDeathHudColor = new Color(200, 0, 0, 200),
            enemyBoxColor = new Color(255, 0, 0, 180);


    public HUD(Player user, int i_enemies) {
        this.user = user;
        startEnemies = i_enemies;
    }

    public void updateEnemyAmount(int amount) {
        enemies = Math.max(0, amount);
    }

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
        g.setFont(new Font("Dotum Bold",Font.BOLD,40));

        String lifeS = String.valueOf((int) user.life),
                ammoS = user.weapon == null ? "" : "Ammo: " + user.weapon.ammo + " / " + user.weapon.maxAmmo;
        g.setColor(defaultHudColor);

//        g.drawString(enemiesS, Game.WIDTH/2 - textWidth(enemiesS, g)/2, HUDy + textHeight(enemiesS,g)/2);

        g.drawString(ammoS, HUDx - textWidth(ammoS, g), HUDy + textHeight(ammoS, g) / 2);
        for (int i = -startEnemies / 2; i < startEnemies / 2; i++) {
            g.fillRect(Game.WIDTH / 2 + i * 70, Game.HEIGHT - 50, 50, 50);
        }
        if (user.life <= 10) g.setColor(nearDeathHudColor);
        g.drawString(lifeS, 26, HUDy + textHeight(lifeS, g) / 2);
        g.setStroke(new BasicStroke(2));
        g.drawRect(lifeBar_x - lifeBar_offset, lifeBar_y - lifeBar_offset, lifeBar_w + 2 * lifeBar_offset, lifeBar_h + 2 * lifeBar_offset);
        g.setStroke(new BasicStroke(1));
        g.fillRect(lifeBar_x, lifeBar_y, (int) (user.life * lifeBar_w / user.maxLife), lifeBar_h);
        g.fill(cross);
        for (int i = -startEnemies / 2; i < enemies - startEnemies / 2; i++) {
            g.setColor(enemyBoxColor);
            g.fillRect(Game.WIDTH / 2 + i * 70, Game.HEIGHT - 50, 50, 50);
        }
    }

    private int textWidth(String t, Graphics2D g) {
        return (int) g.getFontMetrics().getStringBounds(t, g).getWidth();
    }

    private int textHeight(String t, Graphics2D g) {
        return (int) g.getFontMetrics().getStringBounds(t, g).getHeight();
    }


}
