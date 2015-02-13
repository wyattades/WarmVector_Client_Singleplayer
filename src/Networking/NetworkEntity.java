package Networking;

import java.util.HashMap;

public abstract class NetworkEntity extends Entity {

    private HashMap<String, NetworkVariable<? extends Object>> netVars = new HashMap<>();

    private int uID;

    NetworkEntity(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    NetworkEntity(NetworkEntity networkEntity) {
        super(networkEntity);
    }

    void refresh(String data) {

    }

    int getID() { return uID; }
    void setID(int uID) { this.uID = uID; }

}