package Entity.Weapon;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class M4rifle extends Weapon {

    public static int maxAmmo = 40;

    public M4rifle(double x, double y, double w, double h, double orient, int ammo) {
        super(x, y, w, h, orient, ammo);
        setConstants();
    }

    protected void setConstants() {
        name = "M4Rifle";
        type = 1;
        bVel = 8;
        amount = 1;
        spread = 0.05;
        damage = 15;
        rate = 60;
    }

    public void update() {

    }

}
