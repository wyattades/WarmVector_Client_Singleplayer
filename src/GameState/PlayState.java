package GameState;

import Entities.EntityManager;
import Entities.Player.ThisPlayer;
import Main.OutputManager;
import Main.Window;
import UI.*;
import Util.MyInputEvent;
import Util.MyMath;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PlayState extends GameState {

    public static final double SCALEFACTOR = Window.SCALE * 1.5;
    public static final int MAXLEVEL = 5;

    private static final String[] ASSETS = {
            "concrete0.wav", "concrete1.wav", "concrete2.wav", "concrete3.wav", "concrete4.wav", "concrete5.wav",
            "impact_missile.wav", "reload.wav", "ric0.wav", "ric1.wav", "ric2.wav", "shoot_LMG.wav", "shoot_Pistol.wav",
            "shoot_Rifle.wav", "shoot_RPG.wav", "shoot_Shotgun.wav", "shoot_Sniper.wav", "player0g.png", "player1g.png",
            "player1.png", "player0.png", "gun_Rifle.png", "gun_LMG.png", "gun_Pistol.png", "gun_Sniper.png", "gun_Shotgun.png", "gun_RPG.png",
            "missile.png", "bullet.png", "background.png", "hit_"
    };

    private EntityManager entityManager;
    private Camera camera;
    private HUD hud;
    private Shadow shadow;
    private Map map;
    private List<Sprite> sprites;
    private HashMap<String, Integer> keyMap;
    private boolean restart;

    public PlayState(GameStateManager _gsm) {
        super(_gsm);
    }

    public void load() {
        gsm.assetManager.loadAssets(ASSETS);
    }

    public void init() {

        restart = false;

        //Add key and mouse mappings to inputManager
        keyMap = new HashMap<>();
        keyMap.put("RELOAD", KeyEvent.VK_R);
        keyMap.put("UP", KeyEvent.VK_W);
        keyMap.put("DOWN", KeyEvent.VK_S);
        keyMap.put("LEFT", KeyEvent.VK_A);
        keyMap.put("RIGHT", KeyEvent.VK_D);
        keyMap.put("PAUSE", KeyEvent.VK_ESCAPE);
        keyMap.put("RESTART", KeyEvent.VK_SPACE);
        keyMap.put("ATTACK", MouseEvent.BUTTON1);
        keyMap.put("CHANGE_WEAPON", MouseEvent.BUTTON3);

        int level = OutputManager.getSetting("level");

        System.out.println("Starting Level " + level);

        // TODO: use this seed thing somehow
        long seed = (long) MyMath.random(0, 10000000);
        Random randomGenerate = new Random(seed);

        map = new Map(gsm, 200, 200, 1.0, OutputManager.getSetting("cave_mode") == 1, randomGenerate);
        entityManager = new EntityManager(gsm, map, level, randomGenerate);

        shadow = new Shadow(map, entityManager.thisPlayer.x, entityManager.thisPlayer.y);

        sprites = new ArrayList<>();

        camera = new Camera(entityManager.getThisPlayer());
        hud = new HUD(entityManager, map);

        gsm.cursor.setSprite(MouseCursor.CROSSHAIR);
        gsm.cursor.setMouse((int) (Main.Window.WIDTH * 0.5 + 70), (int) (Window.HEIGHT * 0.5));

        update(0.0);

    }

    public void unload() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);

        gsm.assetManager.unload(ASSETS);
    }

    public void draw(Graphics2D g) {

        //TEMP
        g.setColor(new Color(81, 105, 124));
        g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

        //Create copy of transform for later
        AffineTransform oldT = g.getTransform();

        //Zoom in
        g.scale(SCALEFACTOR, SCALEFACTOR);

        //translate screen to follow player
        g.translate(camera.displayX, camera.displayY);

        //background image
        g.drawImage(map.background, 0, 0, map.width, map.height, null);

        entityManager.draw(g);

        for (Sprite a : sprites) {
            a.draw(g);
        }

        // TODO: move shadow into map maybe
        shadow.draw(g);
        map.draw(g);

        //reset transformation
        g.setTransform(oldT);

        hud.draw(g);

        gsm.cursor.draw(g);

    }

    public void update(double deltaTime) {

        entityManager.update();

        // TODO: should add animations in entityManager
        List<Sprite> hitAnimations = entityManager.getHitAnimations();
        sprites.addAll(hitAnimations.stream().collect(Collectors.toList()));
        if (hitAnimations.size() > 0) {
            shadow.queueWorldUpdate();
        }

        for (int i = sprites.size() - 1; i >= 0; i--) {
            Sprite a = sprites.get(i);
            a.step();
            if (!a.state) sprites.remove(i);
        }

        camera.update(gsm.cursor.x, gsm.cursor.y);

        // TODO: move this somewhere else (map?)
        ThisPlayer thisPlayer = entityManager.getThisPlayer();
        if (thisPlayer.state && thisPlayer.moving) {
            shadow.queueOriginUpdate();
        }
        shadow.update(thisPlayer.x, thisPlayer.y);

        if (restart) {
            init();
        }

    }

    public void inputHandle(MyInputEvent event) {

        switch (event.type) {
            case MyInputEvent.MOUSE_MOVE:
                gsm.cursor.setPosition(event.x, event.y);
                break;
            case MyInputEvent.MOUSE_DOWN:
                if (event.code == keyMap.get("ATTACK")) {
                    entityManager.thisPlayer.shooting = true;
                } else if (event.code == keyMap.get("CHANGE_WEAPON")) {
                    //If rightMouse is clicked, check for pickup or drop a weapon
                    entityManager.attemptWeaponChange();
                }
                break;
            case MyInputEvent.MOUSE_UP:
                if (event.code == keyMap.get("ATTACK")) {
                    entityManager.thisPlayer.shooting = false;
                }
                break;
            case MyInputEvent.KEY_DOWN:
                if (event.code == keyMap.get("RELOAD")) {
                    entityManager.thisPlayer.reloadWeapon();
                } else if (event.code == keyMap.get("LEFT")) {
                    entityManager.thisPlayer.left = true;
                } else if (event.code == keyMap.get("RIGHT")) {
                    entityManager.thisPlayer.right = true;
                } else if (event.code == keyMap.get("UP")) {
                    entityManager.thisPlayer.up = true;
                } else if (event.code == keyMap.get("DOWN")) {
                    entityManager.thisPlayer.down = true;
                } else if (event.code == keyMap.get("PAUSE")) {
                    System.out.println("Paused game.");
                    gsm.setState(GameStateManager.PAUSE, GameStateManager.TOP);
                } else if (event.code == keyMap.get("RESTART")) {
                    if (!entityManager.thisPlayer.state) {
                        restart = true;
                    }
                }
                break;
            case MyInputEvent.KEY_UP:
                if (event.code == keyMap.get("LEFT")) {
                    entityManager.thisPlayer.left = false;
                } else if (event.code == keyMap.get("RIGHT")) {
                    entityManager.thisPlayer.right = false;
                } else if (event.code == keyMap.get("UP")) {
                    entityManager.thisPlayer.up = false;
                } else if (event.code == keyMap.get("DOWN")) {
                    entityManager.thisPlayer.down = false;
                }
                break;
        }

    }

}