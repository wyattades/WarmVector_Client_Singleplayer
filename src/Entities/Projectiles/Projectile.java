package Entities.Projectiles;

import Entities.Entity;
import Entities.Player;
import Map.GeneratedEnclosure;
import StaticManagers.FileManager;

import java.awt.geom.Area;
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

        this.x += shooter.weapon.gunLength * (float)Math.cos(orient);
        this.y += shooter.weapon.gunLength * (float)Math.sin(orient);

        vx = (float) Math.cos(orient)*i_speed;
        vy = (float) Math.sin(orient)*i_speed;

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
        if (!map.region.intersects(collideBox)) {
            map.addExplosion(x, y, 40);
            state = false;
        } else {
            for (HashMap.Entry<String, ArrayList<Entity>> entry : entityList.entrySet()) {
                if (entry.getKey().equals("enemy") || entry.getKey().equals("thisPlayer")) {
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
        sprite = FileManager.images.get("bullet.png");
    }

    @Override
    public boolean hit(int damage, float angle) {
        return false;
    }
}
