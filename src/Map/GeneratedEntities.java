package Map;

import Entities.Enemy;
import Entities.Entity;
import Entities.ThisPlayer;
import Entities.Weapons.*;
import Helper.MyMath;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/Map/
 * Created by Wyatt on 9/14/2015.
 */
public class GeneratedEntities {

    private HashMap<String, ArrayList<Entity>> entityList;

    private Area region;

    private String[] weaponTypes;

    public GeneratedEntities(GeneratedEnclosure map, float difficultyFactor) {

        weaponTypes = new String[]{
                "LMG",
                "M4rifle",
                "Sniper",
                "Remington"
        };

        entityList = new HashMap<>();

        region = map.region;

        entityList.put("thisPlayer", new ArrayList<>());
        entityList.put("enemy", new ArrayList<>());
        entityList.put("weapon", new ArrayList<>());
        entityList.put("bullet", new ArrayList<>());

        Rect playerSpawn = map.cells.get(map.rooms.size() - 1);

//        entityList.get("thisPlayer").add(new ThisPlayer( randomRoomX(playerSpawn) , randomRoomY(playerSpawn), map ));
//        //Keep re-spawning player until he doesn't intersect with the map
//        while(map.region.intersects(entityList.get("thisPlayer").get(0).collideBox)) {
//            entityList.get("thisPlayer").set(0, new ThisPlayer( randomRoomX(playerSpawn) , randomRoomY(playerSpawn), map ));
//        }

        putEntity(new ThisPlayer(0, 0, map), "thisPlayer", playerSpawn);

        putEntity(new M4rifle(0,0, MyMath.random(0, 6.29f), null), "weapon", playerSpawn);

        for (Rect r : map.rooms) {
            if (!r.equals(playerSpawn)) {
                Enemy newEnemy = new Enemy(0, 0, MyMath.random(0, 6.29f), map);
                newEnemy.setWeapon(randomWeapon());
                newEnemy.weapon.user = newEnemy;
                putEntity(newEnemy, "enemy", r);
            }
        }
    }

    private Weapon randomWeapon() {
        switch((int) Math.floor(MyMath.random(0, 4))) {
            case(0):
                return new M4rifle(0,0,0,null);
            case(1):
                return new LMG(0,0,0,null);
            case(2):
                return new Remington(0,0,0,null);
            case(3):
                return new Sniper(0,0,0,null);
            default:
                return null;
        }
//        try {
//            Class someWeaponClass = Class.forName("Entities.Weapons." + weaponTypes[Math.round(MyMath.random(0, weaponTypes.length - 1))]);
//            return (Weapon) someWeaponClass.newInstance();
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            System.out.println("Random weapon spawn did not work");
//            System.exit(1);
//        }
//        return null;
    }

    private void putEntity(Entity entity, String category, Rect room) {

        Rectangle2D checkCollider = new Rectangle2D.Float(randomRoomX(room),randomRoomY(room),entity.w,entity.h);
        int i = 0;
        while(!region.contains(checkCollider)) {
            checkCollider.setRect(randomRoomX(room), randomRoomY(room), entity.w, entity.h);
            i++;
            if (i > 20) {
                System.out.println("broke    " + entity.w + " , " + entity.h);
                break;
            }
        }

        entity.x = (float) (checkCollider.getX()+checkCollider.getWidth()/2);
        entity.y = (float) (checkCollider.getY()+checkCollider.getHeight()/2);

        entityList.get(category).add(entity);

    }

    public HashMap<String, ArrayList<Entity>> getSpawnedEntities() {
        return entityList;
    }

    private int randomRoomX(Rect r) {
        return Math.round( MyMath.random( r.x , r.x + r.w) );
    }

    private int randomRoomY(Rect r) {
        return Math.round( MyMath.random( r.y , r.y + r.h) );
    }


}
