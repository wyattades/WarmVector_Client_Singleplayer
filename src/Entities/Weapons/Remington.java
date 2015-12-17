package Entities.Weapons;

import Entities.Player;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class Remington extends Weapon {

    public Remington(float x, float y,  float orient, Player i_user) {
        super(x, y, orient, i_user,
                "Remington", 6, 18, 6, 800, 42, 15, 0.25f, 7, FileManager.getSound("m4_shoot.wav"));
    }

    @Override
    protected void loadSprites() {
        sprite = FileManager.getImage("m4.png");
    }
}
