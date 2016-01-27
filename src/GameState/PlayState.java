package GameState;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Entities.Projectiles.Projectile;
import Entities.ThisPlayer;
import Entities.Weapons.Weapon;
import Helper.MyMath;
import Main.Game;
import Map.GeneratedEnclosure;
import Map.GeneratedEntities;
import StaticManagers.AudioManager;
import StaticManagers.FileManager;
import StaticManagers.InputManager;
import Visual.*;
import Visual.Occlusion.Shadow;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {

    private final static float SCALEFACTOR = 3f;

    private HashMap<String, ArrayList<Entity>> entityList;
    private ScreenMover screenMover;
    private HUD hud;
    private ArrayList<Bullet> bullets;
    private ArrayList<Animation> animations;
    private ThisPlayer thisPlayer;
    private Shadow shadow;
    private boolean dead;
    private GeneratedEnclosure map;
    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        System.out.println("Level = " + gsm.level);

        dead = false;
        bullets = new ArrayList<>();
        animations = new ArrayList<>();
        //entityList = tileMap.setEntities();

        map = new GeneratedEnclosure(200, 200, 1.0f);
        GeneratedEntities generatedEntities = new GeneratedEntities(map, 1.0f);
        entityList = generatedEntities.getSpawnedEntities();
        shadow = new Shadow(map, new Color(20,20,20));

        thisPlayer = (ThisPlayer) entityList.get("thisPlayer").get(0);

        screenMover = new ScreenMover(thisPlayer);
        hud = new HUD(entityList, map);

        gsm.cursor.setSprite(MouseCursor.CROSSHAIR);
        gsm.cursor.setMouse(Game.WIDTH / 2 + 70, Game.HEIGHT / 2);

    }

    public void unload() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);

        //save stuff (stats?)
    }
    BufferedImage background = FileManager.getImage("background_0" + 1 + ".png");

    public void draw(Graphics2D g) {

        //TEMP
        g.setColor(new Color(81, 105, 124));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        //Create copy of transform for later
        AffineTransform oldT = g.getTransform();

//        g.rotate(-thisPlayer.orient, Game.WIDTH/2, Game.HEIGHT/2);

        //Zoom in
        g.scale(SCALEFACTOR, SCALEFACTOR);

        //translate screen to follow player
        g.translate(screenMover.screenPosX - thisPlayer.x + Game.WIDTH / 2 / SCALEFACTOR, screenMover.screenPosY - thisPlayer.y + Game.HEIGHT / 2 / SCALEFACTOR);

        //background image
        g.drawImage(background, 0, 0, map.width, map.height, null);

        //draw objects \/
        for (Bullet b : bullets) {
            b.draw(g);
        }

        for (Entity entity : entityList.get("weapon")) {
            Weapon w = (Weapon) entity;
            w.draw(g);
        }

        if (thisPlayer.weapon != null) {
            thisPlayer.weapon.draw(g);
        }
        for (Entity entity : entityList.get("enemy")) {
            Enemy e = (Enemy) entity;
            e.draw(g);
            if (e.weapon != null) {
                e.weapon.draw(g);
            }
        }

        thisPlayer.draw(g);

        for (Entity entity : entityList.get("bullet")) {
            Projectile p = (Projectile)entity;
            p.draw(g);
        }

        for (Animation a : animations) {
            a.draw(g);
        }

        //TEMP
        shadow.draw(g);
        map.draw(g, new Color(90,90,90), new Color(50,50,50));

        //reset transformation
        g.setTransform(oldT);

        if (!dead) {
            hud.draw(g);
            gsm.cursor.draw(g);
        } else {
            g.setColor(Theme.buttonDefault);
            g.setFont(Theme.fontButton);
            g.drawString("PRESS [SPACE]", 26, Game.HEIGHT - 90);
            g.drawString("TO RESTART", 26, Game.HEIGHT - 26);
        }

    }

    public void update() { //update objects

        if (!dead) {
            thisPlayer.update();
            thisPlayer.regenHealth();
            thisPlayer.updateAngle(gsm.cursor.x - screenMover.screenVelX, gsm.cursor.y - screenMover.screenVelY);

            screenMover.updatePosition();
            screenMover.updateRotation(gsm.cursor.x, gsm.cursor.y);
        }

        shadow.setLightLocation(thisPlayer.x, thisPlayer.y);
        shadow.sweep(6.29f);

//        bullets.forEach(Bullet::update);
        for (Entity entity : entityList.get("bullet")) {
            Projectile p = (Projectile)entity;
            p.move();
            p.updateCollideBox();
            p.checkCollisions(map, entityList);
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
        }

        animations.forEach(Animation::update);

//        for (int i = bullets.size() - 1; i >= 0; i--) {
//            if (!bullets.get(i).state) bullets.remove(i); // <-- should I remove "object" or "index location"???
//        }

        for (int i = animations.size() - 1; i >= 0; i--) {
            if (!animations.get(i).state) animations.remove(i);
        }

        //Entities removal outcomes
        //Create an iterator to loop iterate through every entity
        outerloop:
        for (HashMap.Entry<String, ArrayList<Entity>> entry : entityList.entrySet()) {
            for (int i = entry.getValue().size() - 1; i >= 0; i--) {
                //If an entity's state is ever false...
                if (!entry.getValue().get(i).state) {
                    if (!entry.getKey().equals("thisPlayer")) {
                        if (entry.getKey().equals("enemy")) { //enemy dies
                            Player p = (Player) entry.getValue().get(i);
                            if (p.weapon != null) {
                                p.weapon.unloadUser();
                                entityList.get("weapon").add(p.getWeaponForDrop());
                            }
                            int enemyCount = entityList.get("enemy").size() - 1;
                            hud.updateEnemyAmount(enemyCount);
                            //if there are no enemies left...
                            if (enemyCount <= 0) {
                                //move on to next level
                                gsm.setTopState(GameStateManager.NEXTLEVEL);
                            }
                        }
                        entry.getValue().remove(i);
                    } else { //player dies
                        dead = true;
                        Player p = (Player) entry.getValue().get(i);
                        p.deathSequence();
                        break outerloop;
                    }
                }
            }
        }


    }

    private void addBullets(Player p) {
        if (p.weapon != null && Game.currentTimeMillis() - p.shootTime > p.weapon.rate && p.weapon.ammo > 0) {

            for (int i = 0; i < p.weapon.amountPerShot; i++) {
                AudioManager.playSFX(p.weapon.shootSound);
                entityList.get("bullet").add(new Projectile(p.x, p.y, p.orient + MyMath.random(-p.weapon.spread/2f, p.weapon.spread/2f), 15.0f, 0.0f, p));
            }
            p.shootTime = Game.currentTimeMillis();

            //Subtract one ammo from player
            p.weapon.changeAmmo(-1);

        }
    }

    public void inputHandle(InputManager inputManager) {

        gsm.cursor.setPosition(inputManager.mouse.x + Math.round(screenMover.screenVelX), inputManager.mouse.y + Math.round(screenMover.screenVelY));

//        for (String eventName : inputManager.getEvents()) {
//            switch(eventName) {
//                case "LEFT":
//                    thisPlayer.vx = - Player.topSpeed;
//                    break;
//                default:
//                    System.out.println("Unknown event name: " + eventName);
//                    break;
//            }
//        }

        //If R is pressed, player reloads
        if (inputManager.isKeyPressed("R") && Game.currentTimeMillis() - inputManager.getKeyTime("R") > 1000) {
            thisPlayer.reloadWeapon();
            inputManager.setKeyTime("R", Game.currentTimeMillis());
        }

        //If leftKey is pressed and right isn't, player goes left
        if (inputManager.isKeyPressed("LEFT") && !inputManager.isKeyPressed("RIGHT"))
        thisPlayer.moveX(-Player.acceleration);

        //If rightKey is pressed and left isn't, player goes right
        else if (inputManager.isKeyPressed("RIGHT") && !inputManager.isKeyPressed("LEFT"))
            thisPlayer.moveX(Player.acceleration);


        //If upKey is pressed and down isn't, player goes up
        if (inputManager.isKeyPressed("UP") && !inputManager.isKeyPressed("DOWN"))
            thisPlayer.moveY(-Player.acceleration);

        //If downKey is pressed and up isn't, player goes down
        else if (inputManager.isKeyPressed("DOWN") && !inputManager.isKeyPressed("UP"))
            thisPlayer.moveY(Player.acceleration);


        //If leftMouse is pressed, create bullets from player
        if (inputManager.isMousePressed("LEFTMOUSE")) {
            addBullets(thisPlayer);
        }

        //If rightMouse is clicked, check for pickup or drop a weapon
        if ((inputManager.isMouseClicked("RIGHTMOUSE") || inputManager.isMousePressed("RIGHTMOUSE")) && Game.currentTimeMillis() - inputManager.getMouseTime("RIGHTMOUSE") > 300) {

            inputManager.setMouseTime("RIGHTMOUSE", Game.currentTimeMillis());

            boolean availableWeapon = false;
            for (int i = 0; i < entityList.get("weapon").size(); i++) {
                Weapon w = (Weapon) entityList.get("weapon").get(i);
                if (thisPlayer.collideBox.intersects(w.collideBox)) {
                    if (thisPlayer.weapon != null) entityList.get("weapon").add(thisPlayer.getWeaponForDrop());
                    thisPlayer.setWeapon(w);
                    entityList.get("weapon").remove(w);
                    availableWeapon = true;
                    break;
                }
            }
            if (!availableWeapon && thisPlayer.weapon != null) {
                entityList.get("weapon").add(thisPlayer.getWeaponForDrop());
                thisPlayer.setWeapon(null);
            }

        }

        if (inputManager.isKeyPressed("ESC") && Game.currentTimeMillis() - inputManager.getKeyTime("ESC") > 400) {
            inputManager.setKeyTime("ESC", Game.currentTimeMillis());
            gsm.setTopState(GameStateManager.PAUSE);
        }

        if (dead && inputManager.isKeyPressed("SPACE")) {
            init();
        }

    }

}
