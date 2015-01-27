package Entity.Weapon;

import java.awt.*;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class Remington extends Weapon {

    public static int maxAmmo = 12;

    public Remington(double x, double y, double w, double h, double orient, int ammo) {
        super(x, y, w, h, orient, ammo);
        name = "Remington";
        type = 2;
    }
    public Remington(int ammo){
        super(ammo);
        name = "Remington";
        type = 2;
    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void update() {

    }
}
