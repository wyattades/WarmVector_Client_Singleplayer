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
    private final static int SPACING = 20, TEXT_HEIGHT = 45;
    public int enemies;

    public HUD(Player user, int enemy) {
        this.user = user;
    }

    public void updateEnemyAmount(int amount) {
        enemies = Math.max(0,amount);
    }

    public void draw(Graphics2D g) {
        //Create strings to display info
        String  lifeS = "Health: " + (int)user.life + " / " + (int)user.maxLife,
                ammoS = user.weapon == null? "" : "Ammo: " + user.weapon.ammo + " / " + user.weapon.maxAmmo,
                enemiesS = "Enemies: " + enemies;

        //Set font and font color
        g.setColor(Color.white);
        g.setFont(new Font("Dotum Bold", Font.PLAIN, TEXT_HEIGHT));

        //Draw strings
        g.drawString(enemiesS, Game.WIDTH - SPACING - textWidth(enemiesS, g),SPACING + TEXT_HEIGHT);
        g.drawString(lifeS, Game.WIDTH - SPACING - textWidth(lifeS, g), Game.HEIGHT - SPACING);
        if (user.weapon != null) {
            g.drawString(ammoS, Game.WIDTH - SPACING - textWidth(ammoS, g), Game.HEIGHT - TEXT_HEIGHT - 2 * SPACING);
        }
    }

    //Returns the width of the string for the specified font and font size
    private int textWidth(String t, Graphics2D g) {
        return (int)g.getFontMetrics().getStringBounds(t, g).getWidth();
    }

}
