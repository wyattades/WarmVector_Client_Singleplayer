package Entities;

import Entities.Projectiles.Projectile;
import Entities.Weapons.*;
import GameState.GameStateManager;
import GameState.PlayState;
import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.AudioManager;
import Util.MyMath;
import Util.Rect;
import Visual.Animation;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by wyatt on 8/18/2015.
 */
public class EntityManager {

    public ArrayList<Projectile> projectiles;
    public ArrayList<Weapon> weapons;
    public ArrayList<Entity> entities;
    public ArrayList<Player> players;
    public ArrayList<Enemy> enemies;
    public ThisPlayer thisPlayer;

    private GeneratedEnclosure map;
    private GameStateManager gsm;
    private PlayState playState;

    public EntityManager(GeneratedEnclosure _map, GameStateManager _gsm, int level, PlayState _playState) {
        map = _map;
        gsm = _gsm;
        playState = _playState;

        generateEntities(level);
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

                for (int j = 0; j < p.weapon.amountPerShot; j++) {
                    AudioManager.playSFX("m4_shoot.wav");
                    Projectile newProjectile = new Projectile(p);
                    projectiles.add(newProjectile);
                    entities.add(newProjectile);
                }
                p.shootTime = Game.currentTimeMillis();

                //Subtract one ammo from player
                p.weapon.changeAmmo(-1);

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
                    gsm.setTopState(GameStateManager.NEXTLEVEL);
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
            checkCollisions(p);

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

    public void checkCollisions(Projectile p) {

        if (map.inverseRegion.intersects(p.collideBox)) {
            playState.addExplosion(p, GeneratedEnclosure.HIT_ANIMATION);
            p.state = false;
            return;
        }

        for (Player player : players) {
            if (!player.equals(p.shooter) && p.collideBox.intersects(player.collideBox)) {
                player.handleHit(p.damage, p.orient);
                playState.addExplosion(p, player.hitAnimation);
                p.state = false;
                return;
            }
        }

    }

    public void generateEntities(int difficultyFactor) {
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        weapons = new ArrayList<>();
        entities = new ArrayList<>();
        players = new ArrayList<>();
        thisPlayer = new ThisPlayer(0, 0, map);

        Rect playerSpawn = map.cells.get(map.rooms.size() - 1);

        putEntity(thisPlayer, playerSpawn);
        players.add(thisPlayer);

        Weapon spawnWeapon = randomWeapon();
        spawnWeapon.orient = MyMath.random(0.0f, 6.29f);
        putEntity(spawnWeapon, playerSpawn);
        weapons.add(spawnWeapon);

        for (Rect room : map.rooms) {
            if (!room.equals(playerSpawn)) {
                for (int i = 0; i < difficultyFactor; i++) {
                    Enemy newEnemy = new Enemy(0, 0, MyMath.random(0.0f, 6.29f), map, thisPlayer);
                    newEnemy.setWeapon(randomWeapon());
                    putEntity(newEnemy, room);
                    enemies.add(newEnemy);
                    players.add(newEnemy);
                }
            }
        }
    }

    private void putEntity(Entity entity, Rect room) {

        Rectangle2D checkCollider = new Rectangle2D.Float(randomRoomX(room),randomRoomY(room),entity.w,entity.h);
        int i = 0;
        while(!map.region.contains(checkCollider)) {
            checkCollider.setRect(randomRoomX(room), randomRoomY(room), entity.w, entity.h);
            i++;
            if (i > 20) {
                System.out.println("No space for entity to be spawned at " + entity.w + " , " + entity.h);
                return;
            }
        }

        entity.x = (float) (checkCollider.getX()+checkCollider.getWidth() * 0.5f);
        entity.y = (float) (checkCollider.getY()+checkCollider.getHeight() * 0.5f);

        entities.add(entity);

    }

    private int randomRoomX(Rect r) {
        return Math.round( MyMath.random( r.x , r.x + r.w) );
    }

    private int randomRoomY(Rect r) {
        return Math.round( MyMath.random( r.y , r.y + r.h) );
    }

    private Weapon randomWeapon() {
        switch ((int) Math.floor(MyMath.random(0, 4))) {
            case (0):
                return new M4rifle();
            case (1):
                return new LMG();
            case (2):
                return new Remington();
            case (3):
                return new Sniper();
            default:
                return null;
        }
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
}
