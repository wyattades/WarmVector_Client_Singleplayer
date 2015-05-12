package GameState;

import Entity.Enemy;
import Visual.Occlussion.EndPoint;
import Entity.Entity;
import Entity.Player;
import Entity.ThisPlayer;
import Entity.Weapon.Weapon;
import HUD.GUI;
import HUD.MouseCursor;
import Main.Game;
import Manager.FileManager;
import Manager.InputManager;
import Map.TileMap;
import Visual.Animation;
import Visual.Bullet;
import Visual.Occlussion.Segment;
import Visual.Occlussion.Visibility;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {


    private int px,py;
    private HashMap<String,ArrayList<Entity>> entityList;
    private TileMap tileMap;
    private GUI gui;
    private ArrayList<Bullet> bullets;
    private ArrayList<Animation> animations;
    private int level;
    private Robot robot;
    private MouseCursor cursor;
    private ThisPlayer thisPlayer;
    //Shadow2D shadow;
    Visibility shadow;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(0);
        }
        bullets = new ArrayList<Bullet>();
        animations = new ArrayList<Animation>();
        tileMap = new TileMap(FileManager.TILESET1, FileManager.BACKGROUND1);
        entityList = tileMap.setEntities();
        gui = new GUI((ThisPlayer) entityList.get("thisPlayer").get(0));
        thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);
        cursor = new MouseCursor(FileManager.CURSOR);
        //shadow = new Shadow2D(tileMap.tileArray,thisPlayer);
        shadow = new Visibility();
        ArrayList<Segment> segments = new ArrayList<Segment>();
        for (Entity e : entityList.get("tile")) {
            EndPoint p1,p2,p3,p4;
            p1 = new EndPoint(e.x-e.w/2,e.y+e.h/2);
            p2 = new EndPoint(e.x+e.w/2,e.y+e.h/2);
            p3 = new EndPoint(e.x+e.w/2,e.y-e.h/2);
            p4 = new EndPoint(e.x-e.w/2,e.y-e.h/2);
            segments.add(new Segment(p1,p2));
            segments.add(new Segment(p2,p3));
            segments.add(new Segment(p3,p4));
            segments.add(new Segment(p4,p1));
        }
        shadow.loadMap(segments);

    }

    public void draw(Graphics2D g) {
        //background
        g.setColor(tileMap.backgroundColor);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        //translate screen to follow player
        AffineTransform oldT = g.getTransform();
        g.scale(Game.SCALEFACTOR, Game.SCALEFACTOR);
        g.translate(gui.screenPosX+(-px + Game.WIDTH / 2 /Game.SCALEFACTOR),gui.screenPosY+(-py + Game.HEIGHT / 2 /Game.SCALEFACTOR));

        //draw objects
        for (Bullet b : bullets) {
            b.draw(g);
        }
        tileMap.draw(g);
        for (Entity entity : entityList.get("weapon")) {
            Weapon w = (Weapon) entity;
            w.draw(g);
        }

        if (thisPlayer.weapon != null) {
            thisPlayer.updateWeapon();
            thisPlayer.weapon.draw(g);
        }
        for (Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy) entity;
            e.draw(g);
            if (e.weapon != null) {
                e.updateWeapon();
                e.weapon.draw(g);
            }
        }
        thisPlayer.draw(g);
        for (Animation a : animations) {
            a.draw(g);
        }
        shadow.draw(g);

        //reset transformation
        g.setTransform(oldT);

        //draw cursor
        cursor.draw(g);
    }

    public void update() {
        //move mouse cursor to center of the display
        robot.mouseMove(Game.WIDTH / 2, Game.HEIGHT / 2);

        //create a copy of thisPlayer for convenience
        thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);

        //update objects
        thisPlayer.update();
        thisPlayer.updateAngle(cursor.x, cursor.y);
        px = thisPlayer.x;
        py = thisPlayer.y;
        gui.updatePosition();
        thisPlayer.stopMove();
        gui.updateRotation(cursor.x, cursor.y);

        shadow.setLightLocation(thisPlayer.x,thisPlayer.y);
        shadow.sweep(999);

        for (Bullet b : bullets) {
            b.update();
        }
        for (Entity entity : entityList.get("weapon")) {
            Weapon w = (Weapon)entity;
            w.updateCollideBox();
        }
        for (Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy) entity;
            e.normalBehavior(px, py);
            e.update();
            if (e.shooting) addBullets(e);
            if (e.life < 0) entityList.get("weapon").add(e.weapon);
        }
        for (Animation a : animations) {
            a.update();
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            if (!bullets.get(i).state) bullets.remove(i); // <-- should I remove "object" or "index location"???
        }
        for (int i = animations.size() - 1; i >= 0; i--) {
            if (!animations.get(i).state) animations.remove(i);
        }

        //Entity removing outcomes
        Iterator<HashMap.Entry<String, ArrayList<Entity>>> it = entityList.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, ArrayList<Entity>> entry = it.next();
            for (int i = entry.getValue().size() - 1; i >= 0; i--) {
                if (!entry.getValue().get(i).state) {
                    if (!entry.getKey().equals("thisPlayer")) {
                        if (entry.getKey().equals("enemy")) { //enemy dies
                            Enemy e = (Enemy) entry.getValue().get(i);
                            entityList.get("weapon").add(e.getWeapon());
                        }
                        entry.getValue().remove(i);
                    } else { //player dies
                        gsm.setState(GameStateManager.PLAY);
                    }
                }
            }
        }
    }

    private void addBullets(Player p) {
        if (p.weapon != null && Game.currentTimeMillis() - p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {
            for (int i = 0; i < p.weapon.amount; i++) {
                Bullet b = new Bullet(p.x, p.y, p.orient, p.weapon.spread, p.weapon.damage, entityList,p);
                for (Bullet.CollidePoint point : b.collidePoints) {
                    animations.add(new Animation(point.x, point.y, b.orient, 2, point.hitColor, FileManager.HIT));
                }
                bullets.add(b);
            }
            p.shootTime = Game.currentTimeMillis();
        }
    }

    public void inputHandle() {
        cursor.updatePosition(InputManager.mouse.x-Game.WIDTH/2, InputManager.mouse.y-Game.HEIGHT/2);

        if (InputManager.isKeyPressed("ESCAPE")) System.exit(0);
        if (InputManager.isKeyPressed("LEFT") && !InputManager.isKeyPressed("RIGHT"))
            thisPlayer.updateVelX(-ThisPlayer.topSpeed);
        else if (InputManager.isKeyPressed("RIGHT") && !InputManager.isKeyPressed("LEFT"))
            thisPlayer.updateVelX(ThisPlayer.topSpeed);
        if (InputManager.isKeyPressed("UP") && !InputManager.isKeyPressed("DOWN"))
            thisPlayer.updateVelY(-ThisPlayer.topSpeed);
        else if (InputManager.isKeyPressed("DOWN") && !InputManager.isKeyPressed("UP"))
            thisPlayer.updateVelY(ThisPlayer.topSpeed);

        if (InputManager.isMousePressed("LEFTMOUSE")) {
            addBullets(thisPlayer);
        }

        if (InputManager.isMouseClicked("RIGHTMOUSE") && Game.currentTimeMillis() - InputManager.getMouseTime("RIGHTMOUSE") > 500) {
            InputManager.setMouseTime("RIGHTMOUSE", Game.currentTimeMillis());
            if (thisPlayer.weapon != null) {
                entityList.get("weapon").add(thisPlayer.getWeapon());
                thisPlayer.weapon = null;
                thisPlayer.sprite = FileManager.PLAYER0;
                for (int i = 0; i < entityList.get("weapon").size() - 1; i++) {
                    Weapon w = (Weapon) entityList.get("weapon").get(i);
                    if (thisPlayer.collideBox.intersects(w.collideBox)) {
                        thisPlayer.weapon = w;
                        thisPlayer.sprite = FileManager.PLAYER0G;
                        entityList.get("weapon").remove(w);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < entityList.get("weapon").size(); i++) {
                    Weapon w = (Weapon) entityList.get("weapon").get(i);
                    if (thisPlayer.collideBox.intersects(w.collideBox)) {
                        thisPlayer.weapon = w;
                        thisPlayer.sprite = FileManager.PLAYER0G;
                        entityList.get("weapon").remove(w);
                        break;
                    }
                }
            }
        }
//        if (InputManager.isKeyPressed("ALT") && InputManager.getKeyTime("ALT") - Game.currentTimeMillis() > 100) {
//            InputManager.setKeyTime("ALT",Game.currentTimeMillis());
//            gsm.setPaused(true);
//        }
        if (InputManager.isKeyPressed("ALT")) gsm.setPaused(true);

    }

}
