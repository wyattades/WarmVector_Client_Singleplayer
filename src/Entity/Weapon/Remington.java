package Entity.Weapon;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class Remington extends Weapon {

    public static int maxAmmo = 12;

    public Remington(int x, int y, int w, int h, float orient, int ammo) {
        super(x, y, w, h, orient, ammo);
        setConstants();
    }

    protected void setConstants() {
        name = "Remington";
        type = 2;
        bVel = 7;
        amount = 6;
        spread = 0.3f;
        damage = 15;
        rate = 400;
    }

}
