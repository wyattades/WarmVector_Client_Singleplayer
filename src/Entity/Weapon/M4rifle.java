package Entity.Weapon;

import java.awt.*;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class M4rifle extends Weapon {

    public static int maxAmmo = 40;

    public M4rifle(double x, double y, double w, double h, double orient, int ammo) {
        super(x, y, w, h, orient, ammo);
        name = "M4Rifle";
        type = 1;
    }
    public M4rifle(int ammo){
        super(ammo);
        name = "M4Rifle";
        type = 1;
    }

    public void draw(Graphics2D g) {

    }

    public void update() {

    }
}
