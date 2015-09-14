package GameState;

import Entity.Enemy;
import Entity.Entity;
import Entity.Player;
import Entity.ThisPlayer;
import Entity.Weapon.Weapon;
import Entity.Tile;
import Main.Game;
import Map.GeneratedMap;
import Map.TileMap;
import StaticManagers.InputManager;
import Visual.*;
import Visual.Occlusion.Visibility;

import javax.sound.sampled.Clip;
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

    private static float SCALEFACTOR = 1.0f;

    private HashMap<String, ArrayList<Entity>> entityList;
    private TileMap tileMap;
    private ScreenMover screenMover;
    private HUD hud;
    private ArrayList<Bullet> bullets;
    private ArrayList<Animation> animations;
    private ThisPlayer thisPlayer;
    private Visibility shadow;
    private boolean dead;
    private GeneratedMap generatedMap;
    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        System.out.println("Level = " + gsm.level);

        dead = false;
        bullets = new ArrayList<Bullet>();
        animations = new ArrayList<Animation>();
        tileMap = new TileMap(gsm.level);
        entityList = tileMap.setEntities();
        thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);
        screenMover = new ScreenMover(thisPlayer);
        hud = new HUD(thisPlayer, entityList.get("enemy").size());
        shadow = new Visibility(tileMap);
        gsm.cursor.setSprite(MouseCursor.CROSSHAIR);
        gsm.cursor.setMouse(Game.WIDTH / 2 + 70, Game.HEIGHT / 2);

        generatedMap = new GeneratedMap(200, 200, 0);


    }

    public void unload() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);

        //save stuff (stats?)
    }

    public void draw(Graphics2D g) {

        //TEMP
        g.setColor(Color.white);
        g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);

        //Create copy of transform for later
        AffineTransform oldT = g.getTransform();

        //Zoom in
        g.scale(SCALEFACTOR, SCALEFACTOR);

        //translate screen to follow player
        g.translate(screenMover.screenPosX - thisPlayer.x + Game.WIDTH / 2 / SCALEFACTOR, screenMover.screenPosY - thisPlayer.y + Game.HEIGHT / 2 / SCALEFACTOR);

        //background image
        //tileMap.drawBack(g);

        //draw objects \/
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

        //TEMP
//        shadow.draw(g);
//        tileMap.drawFore(g);
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < generatedMap.cells.size(); i++) {
            GeneratedMap.Rect t = generatedMap.cells.get(i);
            g.drawRect((int)t.x, (int)t.y, (int)t.w,(int) t.h);
        }
        if (InputManager.isKeyPressed("SPACE")) {
            generatedMap.addWalls();
        }

        //reset transformation
        g.setTransform(oldT);

        if (!dead) {
            hud.draw(g);
            gsm.cursor.draw(g);
        } else {
            g.setColor(ThemeColors.textOver);
            g.setFont(ThemeColors.fontButton);
            g.drawString("PRESS [SPACE] TO RESTART", Game.WIDTH / 2, Game.HEIGHT - 60);
        }

    }

    public void update() {

        //This is here, rather than in inputHandle(), because it needs to be run before thisPlayer is updated
        gsm.cursor.setPosition(InputManager.mouse.x+screenMover.screenVelX, InputManager.mouse.y+screenMover.screenVelY);

        //create a copy of thisPlayer for convenience
        thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);

        if (!dead) {
            //update objects
            thisPlayer.update();
            thisPlayer.regenHealth();
            thisPlayer.updateAngle(gsm.cursor.x-screenMover.screenVelX, gsm.cursor.y-screenMover.screenVelY);

            screenMover.updatePosition();
            screenMover.updateRotation(gsm.cursor.x, gsm.cursor.y);
            thisPlayer.stopMove();
        }

        shadow.setLightLocation(thisPlayer.x, thisPlayer.y);
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
            e.normalBehavior(thisPlayer.x, thisPlayer.y, dead);
            e.update();
            if (e.shooting) addBullets(e);
            //if (e.life < 0) entityList.get("weapon").add(e.weapon);
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

        //Entity removal outcomes
        Iterator<HashMap.Entry<String, ArrayList<Entity>>> it = entityList.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, ArrayList<Entity>> entry = it.next();
            for (int i = entry.getValue().size() - 1; i >= 0; i--) {
                if (!entry.getValue().get(i).state) {
                    if (!entry.getKey().equals("thisPlayer")) {
                        if (entry.getKey().equals("enemy")) { //enemy dies
                            Player p = (Player) entry.getValue().get(i);
                            entityList.get("weapon").add(p.getWeapon());
                            int enemyCount = entityList.get("enemy").size()-1;
                            hud.updateEnemyAmount(enemyCount);
                            //if there are no enemies left...
                            if (enemyCount <= 0) {
                                //move on to next level
                                gsm.setState(GameStateManager.NEXTLEVEL);
                            }
                        }
                        entry.getValue().remove(i);
                    } else { //player dies
                        dead = true;
                        Player p = (Player) entry.getValue().get(i);
                        p.deathSequence();
                    }
                }
            }
        }





    }

    private void addBullets(Player p) {
        if (p.weapon != null && Game.currentTimeMillis() - p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {
            for (int i = 0; i < p.weapon.amount; i++) {
                Bullet b = new Bullet(entityList, p);
                for (Bullet.CollidePoint point : b.collidePoints) {
                    animations.add(new Animation(point.x, point.y, b.orient + (float) Math.PI, 2, point.hitColor, "hit_"));
                    p.weapon.hitSound.stop();
                    Clip copy = p.weapon.hitSound;
                    copy.setFramePosition(0);
                    copy.start();
                }
                //Subtract one ammo from player
                p.weapon.changeAmmo(-1);
                //Add bullet object to arrayList so it can be updated and displayed
                bullets.add(b);

                //Player bullet shoot sound for each bullet shot
                p.weapon.shootSound.stop();
                Clip copy = p.weapon.shootSound;
                copy.setFramePosition(0);
                copy.start();
            }


            p.shootTime = Game.currentTimeMillis();
        }
    }

    public void inputHandle() {

        //If leftKey is pressed and right isn't, player goes left
        if (InputManager.isKeyPressed("LEFT") && !InputManager.isKeyPressed("RIGHT"))
            thisPlayer.updateVelX(-ThisPlayer.topSpeed);
        //If rightKey is pressed and left isn't, player goes right
        else if (InputManager.isKeyPressed("RIGHT") && !InputManager.isKeyPressed("LEFT"))
            thisPlayer.updateVelX(ThisPlayer.topSpeed);

        //If upKey is pressed and down isn't, player goes up
        if (InputManager.isKeyPressed("UP") && !InputManager.isKeyPressed("DOWN"))
            thisPlayer.updateVelY(-ThisPlayer.topSpeed);
        //If downKey is pressed and up isn't, player goes down
        else if (InputManager.isKeyPressed("DOWN") && !InputManager.isKeyPressed("UP"))
            thisPlayer.updateVelY(ThisPlayer.topSpeed);

        //If leftMouse is pressed, create bullets from player
        if (InputManager.isMousePressed("LEFTMOUSE")) {
            addBullets(thisPlayer);
        }

        //If rightMouse is clicked, check for pickup or drop a weapon
        if ((InputManager.isMouseClicked("RIGHTMOUSE") || InputManager.isMousePressed("RIGHTMOUSE")) && Game.currentTimeMillis() - InputManager.getMouseTime("RIGHTMOUSE") > 300) {
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
        if (InputManager.isKeyPressed("ESC") && Game.currentTimeMillis() - InputManager.getKeyTime("ESC") > 400) {
            InputManager.setKeyTime("ESC", Game.currentTimeMillis());
//            gsm.cursor.updateOldPos();
            //gsm.setPaused(true);
            gsm.setTopState(GameStateManager.PAUSE);
        }
        if (dead && InputManager.isKeyPressed("SPACE")) {
            init();
        }

    }
}
