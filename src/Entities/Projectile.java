package Entities;

import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 9/12/2015.
 */
public class Projectile extends Entity {

    private float speed, accel;

    public Projectile(float x, float y, float orient, float speed, float acceleration) {

        super(x, y, orient);
        this.speed = speed;
        this.accel = acceleration;

    }

    public void move() {
        speed += accel;
        x += speed * Math.cos(orient);
        y += speed * Math.sin(orient);
    }

    @Override
    protected void loadSprites() {
        sprite = FileManager.images.get("bullet");
    }

    @Override
    public boolean hit(int damage, float angle) {
        return false;
    }
}
