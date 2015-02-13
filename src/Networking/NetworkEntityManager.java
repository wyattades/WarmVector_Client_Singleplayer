package Networking;

import java.util.ArrayList;

public class NetworkEntityManager extends EntityManager {

    static ArrayList<NetworkEntity> networkEntityList;

    NetworkEntityManager() {
        networkEntityList = new ArrayList<>();
    }
    
    void update() {

    }

    void updateEntity(String data) {
        //Get entity based on UID, update it
        System.out.println("RECEIVED: " + data);
        String netVars[] = data.split("" + NetworkManager.NETVAR_SPLIT_CHAR);

        String name = netVars[0].split("" + NetworkManager.VALUE_SPLIT_CHAR)[0];
        int uID = Integer.parseInt(netVars[0].split("" + NetworkManager.VALUE_SPLIT_CHAR)[1]);

        NetworkEntity networkEntity = null;
        for(NetworkEntity ne : networkEntityList) {
            if(ne.getID() == uID) {
                networkEntity = ne;
            }
        }

        if(networkEntity != null) {
            networkEntity.refresh(data);
        } else {
            networkEntity = (NetworkEntity)createEntity(name);
            addEntity(networkEntity, uID);
        }
    }

    void addEntity(NetworkEntity networkEntity, int uID) {
        networkEntity.setID(uID);
        networkEntityList.add(networkEntity);
        super.addEntity(networkEntity);
    }
}
