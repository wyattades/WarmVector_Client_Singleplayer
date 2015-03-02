package GameState;

import Entity.*;
import Manager.FileManager;
import Manager.GameStateManager;
import Manager.InputManager;
import Map.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {


    double px,py;
    public HashMap<String,ArrayList<Entity>> entityList;
    TileMap tileMap;
    ArrayList<Bullet> bullets;
    ArrayList<Animation> animations;
    int level;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        bullets = new ArrayList<Bullet>();
        animations = new ArrayList<Animation>();
        tileMap = new TileMap(level,FileManager.TILESET1,FileManager.BACKGROUND1);
        entityList = tileMap.setEntities();
    }

    public void draw(Graphics2D g) {
        for(Bullet b : bullets) {
            b.draw(g,px,py);
        }
        tileMap.updateDispPos(px, py);
        tileMap.draw(g);
        entityList.get("thisPlayer").get(0).draw(g);
        for(Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy)entity;
            e.updateDispPos(px,py);
            e.draw(g);
        }
        for (Animation a : animations) {
            a.updateDispPos(px,py);
            a.draw(g);
        }
    }

    public void update() {
        try { entityList.get("thisPlayer").get(0).update();
        }catch(Exception e) {
            gsm.setState(GameStateManager.PLAY);
            return;
        }
        px = entityList.get("thisPlayer").get(0).x;
        py = entityList.get("thisPlayer").get(0).y;
        for(Bullet b : bullets) {
            b.update();
        }
        for(Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy)entity;
            e.normalBehavior(px,py);
            e.update();
            if (e.shooting) addBullets(e);
        }
        for (Animation a : animations) {
            a.update();
        }

        for(int i = bullets.size()-1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            if (!b.state) bullets.remove(b); // <-- remove "object" or "index location"???
        }

        //Entity removing outcomes
        Iterator<HashMap.Entry<String,ArrayList<Entity>>> it = entityList.entrySet().iterator();
        while(it.hasNext()) {
            HashMap.Entry<String,ArrayList<Entity>> entry = it.next();
            for (int i = entry.getValue().size()-1; i >= 0; i--) {
                if (!entry.getValue().get(i).state) {
                    if (!entry.getKey().equals("thisPlayer")) {
                        entry.getValue().remove(i);
                    } else {
                        //restartGame
                    }
                }
            }
        }
        for (int i = animations.size()-1; i >= 0; i--) {
            if (!animations.get(i).state) animations.remove(i);
        }
    }

    private void addBullets(Player p) {
        if (System.currentTimeMillis()-p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {
            for (int i = 0; i < p.weapon.amount; i++){
                Bullet b = new Bullet(p.x, p.y, p.orient, p.weapon.spread, p.weapon.damage, entityList,p);
                for (Point point : b.collidePoints) {
                    animations.add(new Animation(point.x,point.y, b.orient, 2, b.hitObject.hitColor, FileManager.HIT));
                }
                bullets.add(b);
            }
            p.shootTime = (int) System.currentTimeMillis();
        }
    }

    public void inputHandle() {
        ThisPlayer thisPlayer = (ThisPlayer)entityList.get("thisPlayer").get(0);

        thisPlayer.updateAngle(InputManager.mouse.x, InputManager.mouse.y);
        thisPlayer.stopMove();
        if (InputManager.isKeyPressed("LEFT") && InputManager.isKeyPressed("RIGHT"));
        else if (InputManager.isKeyPressed("LEFT")) thisPlayer.updateVelX(-ThisPlayer.topSpeed);
        else if (InputManager.isKeyPressed("RIGHT") ) thisPlayer.updateVelX(ThisPlayer.topSpeed);
        if (InputManager.isKeyPressed("UP")  && InputManager.isKeyPressed("DOWN") );
        else if (InputManager.isKeyPressed("UP") ) thisPlayer.updateVelY(-ThisPlayer.topSpeed);
        else if (InputManager.isKeyPressed("DOWN") ) thisPlayer.updateVelY(ThisPlayer.topSpeed);

        if (InputManager.isMousePressed("LEFTMOUSE")) {
            addBullets(thisPlayer);
        }

//        if (InputManager.isMousePressed("RIGHT") && System.currentTimeMillis()-InputManager.getMouseTime("RIGHT")>500) {
//            InputManager.setMouseTime("RIGHT", (int) System.currentTimeMillis());
//            if (thisPlayer.weapon.type != 0) {
//                entityList.get("weapon").add(thisPlayer.weapon);
//                thisPlayer.weapon = null;
//                for (int i = 0; i < weapons.size ()-1; i++) {
//                    Weapon w = weapons.get(i);
//                    if (collideRects(thisPlayer, w)) {
//                        thisPlayer.weapon = w;
//                        weapons.remove(w);
//                        break;
//                    }
//                }
//            } else {
//                for (int i = 0; i < weapons.size (); i++) {
//                    Weapon w = weapons.get(i);
//                    if (collideRects(thisPlayer, w)) {
//                        thisPlayer.weapon = w;
//                        weapons.remove(w);
//                        break;
//                    }
//                }
//            }
//        }
    }

}
