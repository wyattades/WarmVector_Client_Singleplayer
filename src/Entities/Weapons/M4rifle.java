package Entities.Weapons;

import Entities.Player;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class M4rifle extends Weapon {

    public M4rifle(float x, float y, float orient, Player i_user) {
        super(x, y, orient, i_user,
                "M4Rifle", 320000, 2, 1, 100, 42, 15, 0.05f, 8, FileManager.getSound("m4_shoot.wav"));
    }


    @Override
    protected void loadSprites() {
        sprite = FileManager.getImage("m4.png");
    }
}
