package Entities.Projectiles;

import Entities.Entity;
import Entities.Player;
import Map.GeneratedEnclosure;
import StaticManagers.FileManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 9/12/2015.
 */
public class Projectile extends Entity {

    private float vx, vy, accel;

    private Player shooter;

    public Projectile(float x, float y, float orient, float i_speed, float acceleration, Player shooter) {

        super(x, y, orient);

        this.shooter = shooter;

        w = sprite_w;
        h = sprite_h;

        float cosAngle = (float)Math.cos(orient);
        float sinAngle = (float)Math.sin(orient);

        this.x += shooter.weapon.gunLength * cosAngle;
        this.y += shooter.weapon.gunLength * sinAngle;

        vx = i_speed * cosAngle;
        vy = i_speed * sinAngle;

        this.accel = acceleration;

    }

    public void move() {
        if (accel != 0) {
            vx += (float) Math.sin(orient) * accel;
            vy += (float) Math.sin(orient) * accel;
        }
        x += vx;
        y += vy;
    }

    public void checkCollisions(GeneratedEnclosure map, HashMap<String, ArrayList<Entity>> entityList) {
        if (map.inverseRegion.intersects(collideBox)) {
            map.addExplosion(x, y, 30, 8);
            state = false;
        } else {
            //TODO fix this so bullets don't hit shooter or shooter's weapon
            for (HashMap.Entry<String, ArrayList<Entity>> entry : entityList.entrySet()) {
                if (entry.getKey().equals("enemy") || entry.getKey().equals("thisPlayer")) {
                    //TODO replace with foreach????
                    for (Entity e : entry.getValue()) {
                        if (e.collideBox.intersects(collideBox)) {
                            if (e.hit(shooter.weapon.damage, orient)) {
                                state = false;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void loadSprites() {
        sprite = FileManager.getImage("bullet.png");
    }

    @Override
    public boolean hit(int damage, float angle) {
        return false;
    }
}
