package Networking;

public class Snapshot implements NetworkData {
    String data;

    Snapshot() {
        data = "";
    }

    public String read() { //Package the data up
        return data;
    }

    public String getType() {
        return "snapshot";
    }

    public int getSize() {
        return data.length();
    }
}
