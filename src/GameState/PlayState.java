package GameState;

import Entities.EntityManager;
import Entities.Player;
import Entities.Projectiles.Projectile;
import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.AudioManager;
import StaticManagers.FileManager;
import StaticManagers.InputManager;
import StaticManagers.OutputManager;
import Util.ImageUtil;
import Util.MyMath;
import Visual.*;
import Visual.Occlusion.Shadow;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {

    private final static float SCALEFACTOR = 1920.0f / Game.WIDTH * 1.5f;

    private BufferedImage background;
    private EntityManager entityManager;
    private ScreenMover screenMover;
    private HUD hud;
    private Shadow shadow;
    private GeneratedEnclosure map;
    private List<Animation> animations;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        int level = OutputManager.getSetting("level");

        System.out.println("Level = " + level);

        background = ImageUtil.colorizeImage(FileManager.getImage("background.png"), MyMath.random(0.0f, 1.0f));

        map = new GeneratedEnclosure(200, 200, 1.0f, OutputManager.getSetting("cave_mode") == 1);
        entityManager = new EntityManager(map, gsm, level, this);
        shadow = new Shadow(map);

        animations = new ArrayList<>();

        screenMover = new ScreenMover(entityManager.thisPlayer);
        hud = new HUD(entityManager, map);

        gsm.cursor.setSprite(MouseCursor.CROSSHAIR);
        gsm.cursor.setMouse((int)(Game.WIDTH * 0.5f + 70), (int)(Game.HEIGHT * 0.5f));

        int musicLevel = level % 3;
        AudioManager.playMusic("background" + musicLevel + ".wav");

    }

    public void unload() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);
    }

    public void addExplosion(Projectile p, BufferedImage[] hitImages) {
        AudioManager.playSFX("bulletHit.wav");
        map.addExplosion(p.x, p.y, p.explodeRadius, 8);
        shadow.updateSegments();
        animations.add(new Animation(p.x, p.y, p.orient + MyMath.PI, 1, hitImages));
    }

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
        g.translate(screenMover.screenPosX - entityManager.thisPlayer.x + Game.WIDTH * 0.5f / SCALEFACTOR, screenMover.screenPosY -  entityManager.thisPlayer.y + Game.HEIGHT * 0.5f / SCALEFACTOR);

        //background image
        g.drawImage(background, 0, 0, map.width, map.height, null);

        entityManager.draw(g);

        //TEMP
        shadow.draw(g);
        map.draw(g);

        for (Animation a : animations) {
            a.draw(g);
        }

        //reset transformation
        g.setTransform(oldT);

        if (entityManager.thisPlayer.state) {
            hud.draw(g);
            gsm.cursor.draw(g);
        } else {
            g.setColor(ButtonC.buttonDefault);
            g.setFont(ButtonC.BUTTON_FONT);
            g.drawString("PRESS [SPACE]", 26, Game.HEIGHT - 90);
            g.drawString("TO RESTART", 26, Game.HEIGHT - 26);
        }

    }

    public void update() { //update objects

        entityManager.update();

        for (int i = animations.size() - 1; i >= 0; i--) {
            Animation a = animations.get(i);
            a.update();
            if (!a.state) animations.remove(i);
        }


        if (entityManager.thisPlayer.state) {
            entityManager.thisPlayer.updateAngle(gsm.cursor.x - screenMover.screenVelX, gsm.cursor.y - screenMover.screenVelY);

            screenMover.updatePosition();
            screenMover.updateRotation(gsm.cursor.x, gsm.cursor.y);
        }

        shadow.setLightLocation(entityManager.thisPlayer.x, entityManager.thisPlayer.y);
        shadow.sweep(6.29f);

    }

    public void inputHandle(InputManager inputManager) {

        gsm.cursor.setPosition(inputManager.mouse.x + Math.round(screenMover.screenVelX), inputManager.mouse.y + Math.round(screenMover.screenVelY));

        //If R is pressed, player reloads
        if (inputManager.isKeyPressed("R") && Game.currentTimeMillis() - inputManager.getKeyTime("R") > 1000) {
            entityManager.thisPlayer.reloadWeapon();
            inputManager.setKeyTime("R", Game.currentTimeMillis());
        }

        //If leftKey is pressed and right isn't, player goes left
        if (inputManager.isKeyPressed("LEFT") && !inputManager.isKeyPressed("RIGHT"))
            entityManager.thisPlayer.moveX(-Player.acceleration);

        //If rightKey is pressed and left isn't, player goes right
        else if (inputManager.isKeyPressed("RIGHT") && !inputManager.isKeyPressed("LEFT"))
            entityManager.thisPlayer.moveX(Player.acceleration);


        //If upKey is pressed and down isn't, player goes up
        if (inputManager.isKeyPressed("UP") && !inputManager.isKeyPressed("DOWN"))
            entityManager.thisPlayer.moveY(-Player.acceleration);

        //If downKey is pressed and up isn't, player goes down
        else if (inputManager.isKeyPressed("DOWN") && !inputManager.isKeyPressed("UP"))
            entityManager.thisPlayer.moveY(Player.acceleration);


        //If leftMouse is pressed, create bullets from player
        entityManager.thisPlayer.shooting = inputManager.isMousePressed("LEFTMOUSE");

        //If rightMouse is clicked, check for pickup or drop a weapon
        if ((inputManager.isMouseClicked("RIGHTMOUSE") || inputManager.isMousePressed("RIGHTMOUSE")) && Game.currentTimeMillis() - inputManager.getMouseTime("RIGHTMOUSE") > 300) {

            inputManager.setMouseTime("RIGHTMOUSE", Game.currentTimeMillis());

            entityManager.attemptWeaponChange();
        }

        if (inputManager.isKeyPressed("ESC") && Game.currentTimeMillis() - inputManager.getKeyTime("ESC") > 400) {
            inputManager.setKeyTime("ESC", Game.currentTimeMillis());
            gsm.setTopState(GameStateManager.PAUSE);
        }

        if (!entityManager.thisPlayer.state && inputManager.isKeyPressed("SPACE")) {
            init();
        }

    }

}
