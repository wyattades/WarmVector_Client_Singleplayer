package Entities.Weapons;

import Entities.Player;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class Sniper extends Weapon {

    public Sniper(float x, float y, float orient, Player i_user) {
        super(x, y, orient, i_user,
                "Sniper", 10, 20, 1, 1000, 42, 50, 0.01f, 12, FileManager.getSound("m4_shoot.wav"));
    }


    @Override
    protected void loadSprites() {
        sprite = FileManager.getImage("m4.png");
    }
}
