package Networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private BufferedReader inputStream;
    private DataOutputStream outputStream;

    Client(Socket tempSocket) {
        this.socket = tempSocket;

        try {
            inputStream = (new BufferedReader(new InputStreamReader(socket.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void write(String data) {
        try {
            outputStream.writeChars(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> read() {
        ArrayList<String> inputQueue = new ArrayList<>();

        String line = "";

        try {
            while(inputStream.ready()) {

                line = inputStream.readLine();
                inputQueue.add(line);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return inputQueue;
    }
}
