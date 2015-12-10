package Entities;

import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 9/12/2015.
 */
public class Projectile extends Entity {

    private float x, y, vx, vy, accel;

    public Projectile(float x, float y, float orient, float i_speed, float acceleration) {

        super(x, y, orient);

        this.x = x;
        this.y = y;
        vx = (float) Math.cos(orient)*i_speed;
        vy = (float) Math.sin(orient)*i_speed;
        this.accel = acceleration;

    }

    public void move() {
        vx += (float) Math.sin(orient)*accel;
        vy += (float) Math.sin(orient)*accel;
        x += vx;
        y += vy;
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
