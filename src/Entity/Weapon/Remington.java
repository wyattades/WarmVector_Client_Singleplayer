package Entity.Weapon;

import Entity.Player;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class Remington extends Weapon {

    public Remington(int x, int y, int w, int h, float orient, Player i_user) {
        super(x, y, w, h, orient, i_user);
    }

    protected void setConstants() {
        name = "Remington";
        maxAmmo = 12;
        type = 2;
        bVel = 7;
        amount = 6;
        spread = 0.3f;
        damage = 15;
        rate = 400;
        shootSound = FileManager.sounds.get("m4_shoot.wav");

    }

}
