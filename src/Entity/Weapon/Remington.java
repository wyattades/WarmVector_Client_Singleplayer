package Entity.Weapon;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class Remington extends Weapon {

    public static int maxAmmo = 12;

    public Remington(double x, double y, double w, double h, double orient, int ammo) {
        super(x, y, w, h, orient, ammo);
        setConstants();
    }
    public Remington(int ammo){
        super(ammo);
        setConstants();
    }

    protected void setConstants() {
        name = "Remington";
        type = 2;
        bVel= 7;
        amount = 6;
        spread = 0.3;
        damage = 15;
        rate = 400;
    }

    public void update() {

    }
}
