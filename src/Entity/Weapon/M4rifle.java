package Entity.Weapon;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class M4rifle extends Weapon {

    public static int maxAmmo = 40;

    public M4rifle(int x, int y, int w, int h, float orient, int ammo) {
        super(x, y, w, h, orient, ammo);
    }

    protected void setConstants() {
        name = "M4Rifle";
        type = 1;
        bVel = 8;
        amount = 1;
        spread = 0.05f;
        damage = 15;
        rate = 100;
    }

}
