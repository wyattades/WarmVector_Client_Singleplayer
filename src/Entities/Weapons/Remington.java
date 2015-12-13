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
                "Remington", 6, 6, 6, 400, 42, 10, 0.25f, 7, FileManager.sounds.get("m4_shoot.wav"));
    }

    @Override
    protected void loadSprites() {
        sprite = FileManager.images.get("m4.png");
    }
}
