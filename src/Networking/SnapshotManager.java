package Networking;

/**
 * Created by spens_000 on 1/13/2015.
 */
public class SnapshotManager implements DataManager {

    private NetworkEntityManager networkEntityManager;

    SnapshotManager(NetworkEntityManager networkEntityManager) {
        this.networkEntityManager = networkEntityManager;
    }

    public void process(String data) {
        String entities[] = data.split("" + NetworkManager.ENTITY_SPLIT_CHAR);

        for(String s : entities) {
            if(s.length() > 2)
                networkEntityManager.updateEntity(s);
        }
    }
}
