package Map;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Entities.ThisPlayer;
import Entities.Weapon.M4rifle;
import Entities.Weapon.Remington;
import Entities.Weapon.Weapon;
import Main.Game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/Map/
 * Created by Wyatt on 9/14/2015.
 */
public class GeneratedEntities {

    private int minWallDist;

    private HashMap<String, ArrayList<Entity>> entityList;

    public GeneratedEntities(GeneratedEnclosure map, float difficultyFactor) {
        minWallDist = 6 * map.scale;

        entityList = new HashMap<String, ArrayList<Entity>>();

        entityList.put("thisPlayer", new ArrayList<Entity>());
        entityList.put("enemy", new ArrayList<Entity>());
        entityList.put("weapon", new ArrayList<Entity>());

        Rect playerSpawn = map.cells.get((int) Game.random(0, map.rooms.size() - 1));

        entityList.get("thisPlayer").add(new ThisPlayer( randomRoomX(playerSpawn) , randomRoomY(playerSpawn), map ));

        entityList.get("weapon").add(
                new Remington(
                        entityList.get("thisPlayer").get(0).x + 50,
                        entityList.get("thisPlayer").get(0).y,
                        Game.random(0,6.29f),
                        null
                )
        );

        for (Rect r : map.rooms) {
            if (!r.equals(playerSpawn)) {
                Enemy newEnemy = new Enemy(randomRoomX(r), randomRoomY(r), Game.random(0, 6.29f), map);
                newEnemy.setWeapon(new M4rifle(0,0,0,newEnemy));
                entityList.get("enemy").add(newEnemy);
            }
        }
    }

    public HashMap<String, ArrayList<Entity>> getSpawnedEntities() {
        return entityList;
    }

    private int randomRoomX(Rect r) {
        return Math.round( Game.random( r.x + minWallDist , r.x + r.w - minWallDist ) );
    }

    private int randomRoomY(Rect r) {
        return Math.round( Game.random( r.y + minWallDist , r.y + r.h - minWallDist ) );
    }


}
