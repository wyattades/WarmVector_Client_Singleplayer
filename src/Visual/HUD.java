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
    private final static int HUDx = (int) (Game.WIDTH*.8);
    private final static int HUDy = (int) (Game.HEIGHT*.9);
    public int enemies;


    public HUD(Player user) {
        this.user = user;
    }

    public void updateEnemyAmount(int amount) {
        enemies = Math.max(0,amount);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Dotum Bold", Font.PLAIN, 45));
        g.drawString("Enemies: " + enemies,(int)(Game.WIDTH*.8),(int)(Game.HEIGHT*.1));
        g.drawString("Health: " + (int)user.life + " / " + (int)user.maxLife, HUDx, HUDy);
        if (user.weapon != null) {
            g.drawString("Ammo: " + user.weapon.ammo + " / " + user.weapon.maxAmmo, HUDx, HUDy + 50);
        }
    }

}
