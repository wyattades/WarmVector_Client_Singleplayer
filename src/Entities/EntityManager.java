package Entities;

import Entities.Player.Enemy;
import Entities.Player.Player;
import Entities.Player.ThisPlayer;
import GameState.GameStateManager;
import Main.Game;
import Main.OutputManager;
import UI.Map;
import UI.Sprite;
import Util.MyMath;
import Util.Rect;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by wyatt on 8/18/2015.
 */
public class EntityManager {

    private List<Projectile> projectiles;
    private List<Weapon> weapons;
    private List<Entity> entities;
    private List<Player> players;
    private List<Enemy> enemies;
    public ThisPlayer thisPlayer;

    private GameStateManager gsm;

    private Map map;
    private Random randomGenerate;

    public EntityManager(GameStateManager _gsm, Map _map, int _level, Random _randomGenerate) {
        map = _map;
        gsm = _gsm;
        randomGenerate = _randomGenerate;

        generateEntities(_level);
    }

    public void draw(Graphics2D g) {
        for (Entity e : entities) {
            e.draw(g);
        }
    }

    public void update() {

        // Loop through player array
        for (int i = players.size() - 1; i >= 0; i--) {
            Player p = players.get(i);

            // Generic player update
            p.update();
            // Add projectile if player is shooting
            if (p.shooting && p.weapon != null && p.weapon.ammo > 0 &&
                    Game.currentTimeMillis() - p.shootTime > p.weapon.rate) {

                p.weapon.shoot();

                for (int j = 0; j < p.weapon.amountPerShot; j++) {
                    Projectile newProjectile = new Projectile(gsm, p);
                    projectiles.add(newProjectile);
                    entities.add(newProjectile);
                }
                p.shootTime = Game.currentTimeMillis();

            }

            if (!p.state) {
                if (p.weapon != null) {
                    Weapon droppedWeapon = p.getWeaponForDrop();
                    entities.add(droppedWeapon);
                    weapons.add(droppedWeapon);
                }

                p.deathSequence();

                players.remove(i);
            }
        }

        // Loop through enemy array
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);

            if (!e.state) {
                enemies.remove(i);
                // If there are no enemies left, move on to next level
                if (enemies.size() == 0) {
                    gsm.setState(GameStateManager.NEXTLEVEL, GameStateManager.TOP);
                }
            }
        }

        // Loop through weapon array
        for (int i = weapons.size() - 1; i >= 0; i--) {
            Weapon w = weapons.get(i);

            if (!w.state) {
                weapons.remove(i);
            }
        }

        // Loop through projectile array
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);

            p.move();
            p.updateCollideBox();

            if (!p.state) {
                projectiles.remove(i);
            }
        }

        // Loop through entity array
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity e = entities.get(i);

            if (!e.state) {
                entities.remove(i);
            }
        }

    }

    private Hittable checkDirectHit(Projectile p) {

        for (Player player : players) {
            if (player.getId() != p.shooter.getId() && player.handleDirectHit(p)) {
                return player;
            }
        }

        if (map.handleDirectHit(p)) {
            return map;
        }

        return null;
    }

    public List<Sprite> getHitAnimations() {

        List<Sprite> hitAnimations = new ArrayList<>();

        for (Projectile p : projectiles) {
            Hittable hitThing = checkDirectHit(p);
            if (hitThing != null) {
                p.state = false;

                // TODO: instead, add these to graphicsManager
                hitAnimations.add(new Sprite(p.x, p.y, p.orient + MyMath.PI, 1, false, hitThing.getHitAnimation()));

                if (hitThing.getId() != map.getId()) map.handleIndirectHit(p);

                for (Player player : players) {
                    if (hitThing.getId() != player.getId() && player.handleIndirectHit(p)) {
                        double angle = Math.atan2(player.y - p.y, player.x - p.x);
                        hitAnimations.add(new Sprite(player.x, player.y, angle, 1, false, player.getHitAnimation()));
                    }
                }
            }
        }

        return hitAnimations;
    }

    public void generateEntities(int difficultyFactor) {
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        weapons = new ArrayList<>();
        entities = new ArrayList<>();
        players = new ArrayList<>();
        thisPlayer = new ThisPlayer(gsm, map);

        Rect playerSpawn = map.cells.get(map.rooms.size() - 1);

        putEntity(thisPlayer, playerSpawn);
        players.add(thisPlayer);

        Weapon spawnWeapon = randomWeapon(difficultyFactor);
        putEntity(spawnWeapon, playerSpawn);
        weapons.add(spawnWeapon);

        for (Rect room : map.rooms) {
            if (!room.equals(playerSpawn)) {
                for (int i = 0; i < difficultyFactor; i++) {
                    Enemy newEnemy = new Enemy(gsm, MyMath.random(0.0, MyMath.TWO_PI, randomGenerate), map, thisPlayer);
                    newEnemy.setWeapon(randomWeapon(difficultyFactor));
                    putEntity(newEnemy, room);
                    enemies.add(newEnemy);
                    players.add(newEnemy);
                }
            }
        }
    }

    private void putEntity(Entity entity, Rect room) {

        Rectangle2D checkCollider = new Rectangle2D.Double(randomRoomX(room),randomRoomY(room),entity.w,entity.h);
        int i = 0;
        while(!map.region.contains(checkCollider)) {
            checkCollider.setRect(randomRoomX(room), randomRoomY(room), entity.w, entity.h);
            i++;
            if (i > 20) {
                System.out.println("No space for entity to be spawned at " + entity.w + " , " + entity.h);
                return;
            }
        }

        entity.x = checkCollider.getX()+checkCollider.getWidth() * 0.5;
        entity.y = checkCollider.getY()+checkCollider.getHeight() * 0.5;

        entities.add(entity);

    }

    private int randomRoomX(Rect r) {
        return MyMath.round(MyMath.random(r.x , r.x + r.w, randomGenerate));
    }

    private int randomRoomY(Rect r) {
        return MyMath.round( MyMath.random(r.y , r.y + r.h, randomGenerate));
    }

    private Weapon randomWeapon(int level) {

        int randomNumber;
        do randomNumber = (int) MyMath.random(0.0, level * 2.0, randomGenerate);
        while (randomNumber > Weapon.TYPE_AMOUNT);

        return Weapon.getType(gsm, randomNumber);
    }

    public void attemptWeaponChange() {
        boolean availableWeapon = false;
        for (Weapon w : weapons) {
            w.updateCollideBox();
            if (thisPlayer.collideBox.intersects(w.collideBox)) {
                if (thisPlayer.weapon != null) {
                    Weapon droppedWeapon = thisPlayer.getWeaponForDrop();
                    entities.add(droppedWeapon);
                    weapons.add(droppedWeapon);
                }
                thisPlayer.setWeapon(w);
                w.state = false;
                availableWeapon = true;
                break;
            }
        }
        if (!availableWeapon && thisPlayer.weapon != null) {
            Weapon droppedWeapon = thisPlayer.getWeaponForDrop();
            entities.add(droppedWeapon);
            weapons.add(droppedWeapon);
            thisPlayer.setWeapon(null);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public ThisPlayer getThisPlayer() {
        return thisPlayer;
    }

}
