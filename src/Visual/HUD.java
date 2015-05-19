package Visual;

import Entity.Player;
import Main.Game;
import com.sun.prism.*;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 5/14/2015.
 */
public class HUD {

    private Player user;
    private final static int HUDx = (int) (Game.WIDTH-20);
    private final static int HUDy = (int) (Game.HEIGHT*.9);
    public int enemies;

    public HUD(Player user) {
        this.user = user;
    }

    public void updateEnemyAmount(int amount) {
        enemies = Math.max(0,amount);
    }

    public void draw(Graphics2D g) {
        String  lifeS = "Health: " + (int)user.life + " / " + (int)user.maxLife,
                ammoS = user.weapon==null?"":"Ammo: " + user.weapon.ammo + " / " + user.weapon.maxAmmo,
                enemiesS = "Enemies: " + enemies;
        g.setColor(Color.white);
        g.setFont(new Font("Dotum Bold", Font.PLAIN, 45));
        g.drawString(enemiesS,HUDx  - textWidth(enemiesS, g),(int)(Game.HEIGHT*.1));
        g.drawString(lifeS, HUDx - textWidth(lifeS, g), HUDy);
        if (user.weapon != null) {
            g.drawString(ammoS, HUDx  - textWidth(ammoS, g), HUDy + 50);
        }
    }

    private int textWidth(String t, Graphics2D g) {
        return (int)g.getFontMetrics().getStringBounds(t, g).getWidth();
    }

}
