package GameState;

import Entity.*;
import Entity.Weapon.M4rifle;
import Entity.Weapon.Remington;
import Manager.FileManager;
import Manager.GameStateManager;
import Manager.InputManager;
import Map.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {

    public HashMap<String,ArrayList<Entity>> entityList;
    TileMap tileMap;
    ArrayList<Bullet> bullets;
    int level;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        bullets = new ArrayList<Bullet>();
        tileMap = new TileMap(level,FileManager.TILESET1,FileManager.BACKGROUND1);
        entityList = tileMap.setEntities();
    }

    public void draw(Graphics2D g) {
        double px = entityList.get("thisPlayer").get(0).x;
        double py = entityList.get("thisPlayer").get(0).y;

        tileMap.updateDispPos(px,py);
        tileMap.draw(g);
        for(Bullet b : bullets) {
            b.draw(g,px,py);
        }

        for(HashMap.Entry<String,ArrayList<Entity>> entry : entityList.entrySet()) {
            for(Entity e : entry.getValue()) {
                if (!entry.getKey().equals("thisPlayer")) e.updateDispPos(px,py);
                e.draw(g);
            }
        }
    }

    public void update() {
        for(int i = bullets.size()-1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            if (!b.state) bullets.remove(b); // <-- remove "object" or "index location"???
        }
        Iterator<HashMap.Entry<String, ArrayList<Entity>>> iterator = entityList.entrySet().iterator();
        while(iterator.hasNext()){
            HashMap.Entry<String,ArrayList<Entity>> entry = iterator.next();
            for (Entity e : entry.getValue()) {
                if (!e.state) iterator.remove();
            }
        }
        try {
            entityList.get("thisPlayer").get(0).update();
        }catch(Exception e) {
            //gsm.setState(SWAG);
            return;
        }
        for(Bullet b : bullets) {
            b.update();
        }
        for(Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy)entity;
            e.normalBehavior(entityList.get("thisPlayer").get(0).x,entityList.get("thisPlayer").get(0).y);
            if (e.shooting) addBullets(e);
        }
    }

    private void addBullets(Player p) {
        if ((int)System.currentTimeMillis()-p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {
            for (int i = 0; i < p.weapon.amount; i++){
                bullets.add(new Bullet(p.x, p.y, p.orient, p.weapon.spread, p.weapon.damage, entityList,p));
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
