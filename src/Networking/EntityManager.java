package Networking;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityManager {
    ArrayList<Entity> entityList;
    HashMap<String, Entity>  entityStorageMap;

    EntityManager() {
        entityList = new ArrayList<>();
        entityStorageMap = new HashMap<>();

        init();
    }

    void init() {
        entityStorageMap.put("player", new Player(0, 0));
    }

    void addEntity(Entity entity) {
        entityList.add(entity);
    }

    void update() {
        entityList.stream().forEach(e -> e.update());
    }

    Entity createEntity(String name) {
        return entityStorageMap.get(name);
    }
}
