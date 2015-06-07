package Entity.Weapon;

import Entity.Player;
import Manager.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class M4rifle extends Weapon {

    public M4rifle(int x, int y, int w, int h, float orient,Player i_user) {
        super(x, y, w, h, orient, i_user);
    }

    protected void setConstants() {
        name = "M4Rifle";
        maxAmmo = 40;
        type = 1;
        bVel = 8;
        amount = 1;
        spread = 0.05f;
        damage = 15;
        rate = 100;
        shootSound = FileManager.sounds.get("m4_shoot.wav");
    }

}
