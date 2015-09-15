package Map;

import Entities.Entity;
import Entities.ThisPlayer;
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

        entityList.get("thisPlayer").add(new ThisPlayer( randomRoomX(playerSpawn) , randomRoomY(playerSpawn) , 0 , null, map ));

        for (Rect r : map.rooms) {

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
