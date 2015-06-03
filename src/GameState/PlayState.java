package GameState;

import Entity.Enemy;
import Entity.Entity;
import Entity.Player;
import Entity.ThisPlayer;
import Entity.Weapon.Weapon;
import Visual.ScreenMover;
import Visual.HUD;
import Main.Game;
import Manager.FileManager;
import Manager.InputManager;
import Map.TileMap;
import Visual.Animation;
import Visual.Bullet;
import Visual.Occlusion.Visibility;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {

    private PauseState pauseState;
    private int px, py, level;
    private HashMap<String,ArrayList<Entity>> entityList;
    private TileMap tileMap;
    private ScreenMover screenMover;
    private HUD hud;
    private ArrayList<Bullet> bullets;
    private ArrayList<Animation> animations;
    private ThisPlayer thisPlayer;
    private Visibility shadow;
    private Robot robot;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        level = 1;
        pauseState = new PauseState(gsm);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void init() {
        if (level > 2) {
            System.out.println("YOU WIN!!!!\nThis game is still in development,\nmore content coming soon!");
            System.exit(0);
        } else {
            System.out.println("Level = " + level);

        }
        pauseState.init();
        gsm.paused = false;
        bullets = new ArrayList<Bullet>();
        animations = new ArrayList<Animation>();
        tileMap = new TileMap(FileManager.images.get("leveltiles_0"+level+".png"), FileManager.images.get("background_0"+level+".png"), FileManager.images.get("foreground_0"+level+".png"));
        entityList = tileMap.setEntities();
        screenMover = new ScreenMover((ThisPlayer) entityList.get("thisPlayer").get(0));
        thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);
        hud = new HUD(thisPlayer,entityList.get("enemy").size());
        shadow = new Visibility(tileMap);
        robot.mouseMove(Game.WIDTH/2,Game.HEIGHT/2);
    }

    public void draw(Graphics2D g) {
        //translate screen to follow player
        AffineTransform oldT = g.getTransform();
        g.scale(Game.SCALEFACTOR, Game.SCALEFACTOR);
        g.translate(screenMover.screenPosX+(-px + Game.WIDTH / 2 /Game.SCALEFACTOR), screenMover.screenPosY+(-py + Game.HEIGHT / 2 /Game.SCALEFACTOR));
        //background image
        tileMap.drawBack(g);

//        Path2D path = new Path2D.Float();
//        path.moveTo(shadow.output.get(0).x, shadow.output.get(0).y);
//        for (int i = 0; i < shadow.output.size(); i++) {
//            Visual.Occlusion.Point e = shadow.output.get(i);
//            path.lineTo(e.x,e.y);
//        }
//        path.closePath();
//        g.setClip(path);

        //draw objects
        for (Bullet b : bullets) {
            b.draw(g);
        }

        for (Entity entity : entityList.get("weapon")) {
            Weapon w = (Weapon) entity;
            w.draw(g);
        }

        if (thisPlayer.weapon != null) {
            thisPlayer.weapon.updatePos();
            thisPlayer.weapon.draw(g);
        }
        for (Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy) entity;
            e.draw(g);
            if (e.weapon != null) {
                e.weapon.updatePos();
                e.weapon.draw(g);
            }
        }
        thisPlayer.draw(g);
        for (Animation a : animations) {
            a.draw(g);
        }
        shadow.draw(g);
        tileMap.drawFore(g);
        //reset transformation
        g.setTransform(oldT);
        hud.draw(g);
        if (gsm.paused) pauseState.draw(g);

    }

    public void update() {
        if (!gsm.paused) {

            //create a copy of thisPlayer for convenience
            thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);

            //update objects
            thisPlayer.update();
            thisPlayer.regenHealth();
            thisPlayer.updateAngle(gsm.cursor.x, gsm.cursor.y);
            px = thisPlayer.x;
            py = thisPlayer.y;
            screenMover.updatePosition();
            thisPlayer.stopMove();
            screenMover.updateRotation(gsm.cursor.x, gsm.cursor.y);

            shadow.setLightLocation(px, py);
            shadow.sweep(999);

            for (Bullet b : bullets) {
                b.update();
            }
            for (Entity entity : entityList.get("weapon")) {
                Weapon w = (Weapon) entity;
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
                            init();
                        }
                    }
                }
            }
            int enemies = entityList.get("enemy").size();
            hud.updateEnemyAmount(enemies);

            //if there are no enemies left...
            if (enemies <= 0) {
                //move on to next level
                level++;
                init();
            }
        } else {
            pauseState.update();
        }
    }

    private void addBullets(Player p) {
        if (p.weapon != null && Game.currentTimeMillis() - p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {
            for (int i = 0; i < p.weapon.amount; i++) {
                Bullet b = new Bullet(p.x, p.y, p.orient, p.weapon.spread, p.weapon.damage, entityList,p);
                for (Bullet.CollidePoint point : b.collidePoints) {
                    animations.add(new Animation(point.x, point.y, b.orient, 2, point.hitColor, FileManager.animations.get("hit_")));
                }
                p.weapon.changeAmmo(-1);
                bullets.add(b);
            }
            p.shootTime = Game.currentTimeMillis();
        }
    }

    public void inputHandle() {
        if (!gsm.paused) {
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
                    thisPlayer.weapon.unloadUser();
                    entityList.get("weapon").add(thisPlayer.getWeapon());
                    thisPlayer.weapon = null;
                    thisPlayer.setSpriteToDefault(true);
                    for (int i = 0; i < entityList.get("weapon").size() - 1; i++) {
                        Weapon w = (Weapon) entityList.get("weapon").get(i);
                        if (thisPlayer.collideBox.intersects(w.collideBox)) {
                            thisPlayer.weapon = w;
                            w.user = thisPlayer;
                            thisPlayer.setSpriteToDefault(false);
                            entityList.get("weapon").remove(w);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < entityList.get("weapon").size(); i++) {
                        Weapon w = (Weapon) entityList.get("weapon").get(i);
                        if (thisPlayer.collideBox.intersects(w.collideBox)) {
                            thisPlayer.weapon = w;
                            w.user = thisPlayer;
                            thisPlayer.setSpriteToDefault(false);
                            entityList.get("weapon").remove(w);
                            break;
                        }
                    }
                }
            }
            if (InputManager.isKeyPressed("ALT") && Game.currentTimeMillis()-InputManager.getKeyTime("ALT") > 400) {
                InputManager.setKeyTime("ALT",Game.currentTimeMillis());
                gsm.setPaused(true);
            }
            //gsm.cursor.setPosition(InputManager.mouse.x - Game.WIDTH / 2, InputManager.mouse.y - Game.HEIGHT / 2);
            gsm.cursor.setPosition(InputManager.mouse.x + 2* screenMover.screenPosX, InputManager.mouse.y + 2* screenMover.screenPosY);
            //robot.mouseMove(Game.WIDTH / 2, Game.HEIGHT / 2);
        } else {
            pauseState.inputHandle();
        }
    }
}
