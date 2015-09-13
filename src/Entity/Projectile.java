package Entity;

import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/Entity/
 * Created by Wyatt on 9/12/2015.
 */
public class Projectile extends Entity {

    private float speed,accel;

    public Projectile(int x, int y, int w, int h, float orient, float speed, float acceleration) {

        super(x,y,w,h,orient);
        this.speed = speed;
        this.accel = acceleration;
        sprite = FileManager.images.get("bullet");

    }

    public void move() {
        speed += accel;
        x += speed * Math.cos(orient);
        y += speed * Math.sin(orient);
    }

    @Override
    public boolean hit(int damage, float angle) {
        return false;
    }
}
