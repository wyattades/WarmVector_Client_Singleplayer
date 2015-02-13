package Networking;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NetworkManager {

    public static final char HEADER_SPLIT_CHAR = ((char)28), ENTITY_SPLIT_CHAR = ((char)29), NETVAR_SPLIT_CHAR = ((char)30), VALUE_SPLIT_CHAR = ((char)31);
    public static final char PACKET_START_CHAR = ((char)2), PACKET_END_CHAR = ((char)3);

    private Client client;
    String serverAddress;
    int port;

    private HashMap<String, DataManager> dataManagers;

    NetworkManager(String serverAddress, int port, NetworkEntityManager networkEntityManager) {
        this.serverAddress = serverAddress;
        this.port = port;

        dataManagers = new HashMap<>();
        dataManagers.put("snapshot", new SnapshotManager(networkEntityManager));
    }

    void start() {
        try {
            client = new Client(new Socket(serverAddress, port));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        input();

        output();
    }

    private void input() {
        System.out.println("PREFUCK");

        for(String data : client.read()) {

            System.out.println("FUCK");

//        inputQueue.stream()
//                .filter(s -> { return (s.startsWith("" + PACKET_START_CHAR) && s.endsWith("" + PACKET_END_CHAR)); })
//                .forEach(s -> { selectManager(s); });

            if (data.startsWith("" + PACKET_START_CHAR) && data.endsWith("" + PACKET_END_CHAR)) {
                data.replaceAll("" + PACKET_START_CHAR, "");
                data.replaceAll("" + PACKET_END_CHAR, "");
                selectManager(data);
            }
        }
    }

    private void selectManager(String data) {
        String info[] = data.split("" + HEADER_SPLIT_CHAR);

        String header[] = info[0].split("" + VALUE_SPLIT_CHAR);

        System.out.println(info[1]);

        if(Integer.parseInt(header[1]) == info[1].length()) {
            dataManagers.get(header[0].trim()).process(info[1]);
        } else {
            System.out.println("Packet incorrect size!");
        }
    }

    private void output() {

    }

    boolean isRunning() {
        return true;
    }
}
