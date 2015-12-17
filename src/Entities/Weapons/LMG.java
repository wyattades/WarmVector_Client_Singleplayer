package Entities.Weapons;

import Entities.Player;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class LMG extends Weapon {

    public LMG(float x, float y, float orient, Player i_user) {
        super(x, y, orient, i_user,
                "LMG", 84, 168, 1, 50, 42, 9, 0.12f, 6, FileManager.getSound("m4_shoot.wav"));
    }


    @Override
    protected void loadSprites() {
        sprite = FileManager.getImage("m4.png");
    }
}
