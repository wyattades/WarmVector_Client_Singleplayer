package Entities;

import Entities.Weapon.Weapon;
import Visual.Bullet;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by wyatt on 8/18/2015.
 */
public class EntityManager {

    //WORK IN PROGRESS, might use EntityManager if EntityList proves inefficient

    private ThisPlayer thisPlayer;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Weapon> weapons;

    public EntityManager() {
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<Bullet>();
        weapons = new ArrayList<Weapon>();
        //thisPlayer = new ThisPlayer();
    }

    public void draw(Graphics2D g) {
        for (Weapon w : weapons) {
            w.draw(g);
        }
        for (Enemy e : enemies) {
            e.draw(g);
        }
        for (Bullet b : bullets) {
            b.draw(g);
        }
        thisPlayer.draw(g);
    }

    public void update() {
        for (Weapon w : weapons) {
            w.updatePos();
            w.updateCollideBox();
        }
        for (Enemy e : enemies) {
            e.update();
        }
        for (Bullet b : bullets) {
            b.update();
        }
        thisPlayer.update();
    }

}
