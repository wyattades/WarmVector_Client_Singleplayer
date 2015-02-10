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
        entityList = new HashMap<String, ArrayList<Entity>>();
        bullets = new ArrayList<Bullet>();
        entityList.put("thisPlayer", new ArrayList<Entity>());
        entityList.put("enemy",new ArrayList<Entity>());
        entityList.put("tile", new ArrayList<Entity>());
        entityList.put("bullet", new ArrayList<Entity>());
        tileMap = new TileMap(level, FileManager.TILESET1);
        int ts = TileMap.tileSize;
        entityList.get("thisPlayer").add(new ThisPlayer(400,400,4*ts,4*ts,0,new Remington(Remington.maxAmmo),tileMap,entityList.get("tile")));
        for (int i = 0; i < tileMap.width; i++) {
            for (int j = 0; j < tileMap.height; j++) {
                int t = tileMap.tileArray[i][j];
                if (t == TileMap.SOLID){
                    entityList.get("tile").add(new Tile((i + 0.5) * ts, (j + 0.5) * ts, ts, ts, 0, TileMap.SOLID));
                } else if (t == TileMap.WINDOW) {
                    entityList.get("tile").add(new Tile((i + 0.5) * ts, (j + 0.5) * ts, ts, ts, 0, TileMap.WINDOW));
                } else if (t == TileMap.SPAWN) {
                    entityList.get("thisPlayer").get(0).x = i*ts;
                    entityList.get("thisPlayer").get(0).y = j*ts;
                } else if (t == TileMap.ENEMY) {
                    entityList.get("enemy").add(new Enemy(i * ts, j * ts, 4 * ts, 4 * ts, 0, new M4rifle(M4rifle.maxAmmo), tileMap, entityList.get("tile")));
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        double px = entityList.get("thisPlayer").get(0).x;
        double py = entityList.get("thisPlayer").get(0).y;

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

//        Iterator<HashMap.Entry<String, ArrayList<Entity>>> iterator = entityList.entrySet().iterator();
//        while(iterator.hasNext()){
//            HashMap.Entry<String,ArrayList<Entity>> entry = iterator.next();
//            for (Entity e : entry.getValue()) {
//                if (!e.state) iterator.remove();
//            }
//        }
        for(int i = bullets.size()-1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            if (!b.state) bullets.remove(b); // <-- remove object or index location???
        }

        entityList.get("thisPlayer").get(0).update();
        for(Bullet b : bullets) {
            b.update();
        }

        double px = entityList.get("thisPlayer").get(0).x;
        double py = entityList.get("thisPlayer").get(0).y;
        for(Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy)entity;
            e.stopMove();
            if (e.lineOfSight(px, py) && e.distBetween(px, py) < 400) {
                if (e.distBetween(px, py) > 100) {
                    e.goTowards(px, py, (float) 1);
                }
                if (e.lookingAt(px, py, (float) 0.05)) {
                    addBullets(e);
                } else {
                    e.orientTo(px, py, (float) 0.1);
                }
            } else {
                //e.patrol(1);
                if (!e.lookingAt((float) (e.x + e.vx), (float) (e.y + e.vy), (float) 0.06)) {
                    e.orientTo((float) (e.x + e.vx), (float) (e.y + e.vy), (float) 0.12);
                }
            }

            e.update();
        }
    }

    private void addBullets(Player p) {
//         \/ put (int) here to fix lag
        if (System.currentTimeMillis()-p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {
            for (int i = 0; i < p.weapon.amount; i++){
                bullets.add(new Bullet(p.x, p.y, p.orient, p.weapon.spread, p.weapon.damage, entityList));
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

        if (InputManager.isMousePressed("RIGHT") && System.currentTimeMillis()-InputManager.getMouseTime("RIGHT")>500) {
            InputManager.setMouseTime("RIGHT", (int) System.currentTimeMillis());
            if (thisPlayer.weapon.type != 0) {
                entityList.get("weapon").add(thisPlayer.weapon);
                thisPlayer.weapon = null;
//                for (int i = 0; i < weapons.size ()-1; i++) {
//                    Weapon w = weapons.get(i);
//                    if (collideRects(thisPlayer, w)) {
//                        thisPlayer.weapon = w;
//                        weapons.remove(w);
//                        break;
//                    }
//                }
            } else {
//                for (int i = 0; i < weapons.size (); i++) {
//                    Weapon w = weapons.get(i);
//                    if (collideRects(thisPlayer, w)) {
//                        thisPlayer.weapon = w;
//                        weapons.remove(w);
//                        break;
//                    }
//                }
            }
        }
    }

}
