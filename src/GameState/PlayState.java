package GameState;

import Entity.Enemy;
import Entity.Entity;
import Entity.Weapon.M4rifle;
import Entity.Weapon.Weapon;
import Manager.GameStateManager;
import Entity.ThisPlayer;
import Manager.InputManager;
import Map.TileMap;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {

    ThisPlayer thisPlayer;
    ArrayList<Enemy> enemies;
    ArrayList<Weapon> weapons;
    TileMap tileMap;
    int level;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        thisPlayer = new ThisPlayer(0,0,64,64,0,new M4rifle(M4rifle.maxAmmo),tileMap);
        tileMap = new TileMap(level,64);
    }

    public void draw(Graphics2D g) {
        thisPlayer.draw(g);
        for(Weapon w : weapons) w.draw(g);
        for(Enemy e : enemies) e.draw(g);

    }

    public void update() {

        thisPlayer.update();

//        for(Enemy e : enemies) {
//            if (lineOfSight(thisPlayer.position) && distBetween(thisPlayer.position).mag() < 400) {
//                if (distBetween(thisPlayer.position).mag() > 100) goTowards(thisPlayer.position, 3.5);
//                else velocity.set(0,0,0);
//                if (lookingAt(thisPlayer.position, 0.05)) {
//                    if (millis()-shootTime > 300) {
//                        world.addBullets(this);
//                        shootTime = millis();
//                    }
//                } else {
//                    orientTo(thisPlayer.position, 0.1);
//                }
//            } else {
//                patrol(2.5);
//                PVector lookPos = new PVector(position.x+velocity.x, position.y+velocity.y);
//                if (!lookingAt(lookPos, 0.06)) orientTo(lookPos, 0.12);
//            }
//            updatePosition();
//            updateLife();
//        }
    }

    public void inputHandle() {

        if (InputManager.isKeyPressed("LEFT") && InputManager.isKeyPressed("RIGHT"));
        else if (InputManager.isKeyPressed("LEFT") ) thisPlayer.updateVelX(-ThisPlayer.topSpeed);
        else if (InputManager.isKeyPressed("RIGHT") ) thisPlayer.updateVelX(ThisPlayer.topSpeed);
        if (InputManager.isKeyPressed("UP")  && InputManager.isKeyPressed("DOWN") );
        else if (InputManager.isKeyPressed("UP") ) thisPlayer.updateVelY(-ThisPlayer.topSpeed);
        else if (InputManager.isKeyPressed("DOWN") ) thisPlayer.updateVelY(ThisPlayer.topSpeed);

        if (InputManager.isMousePressed("LEFT") && System.currentTimeMillis()-InputManager.getMouseTime("LEFT")>500) {
            InputManager.setMouseTime("LEFT", (int) System.currentTimeMillis());
            if (thisPlayer.weapon.type != 0) {
                weapons.add(thisPlayer.weapon);
                thisPlayer.weapon = null;
                for (int i = 0; i < weapons.size ()-1; i++) {
                    Weapon w = weapons.get(i);
                    if (collideRects(thisPlayer, w)) {
                        thisPlayer.weapon = w;
                        weapons.remove(w);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < weapons.size (); i++) {
                    Weapon w = weapons.get(i);
                    if (collideRects(thisPlayer, w)) {
                        thisPlayer.weapon = w;
                        weapons.remove(w);
                        break;
                    }
                }
            }
        }

    }

    boolean collideRects(Entity e1, Entity e2) {
        if (e1.x+e1.w/2 < e2.x-e2.x/2 || e1.x-e1.w/2 > e2.x+e2.x/2 || e1.y+e1.h/2 < e2.y-e2.h/2 || e1.y-e1.h/2 > e2.y+e2.h/2) return false;
        return true;
    }
}
