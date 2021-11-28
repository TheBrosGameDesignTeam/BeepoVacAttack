package BeepoVacAttack.Networking;
import BeepoVacAttack.BeepoVacServer.MainGame;
import BeepoVacAttack.Networking.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Listener extends Thread {

    private final ObjectInputStream inputStream;

    public static ConcurrentLinkedQueue<Object> queue;
    public int player = 0;

    public Listener(ObjectInputStream inputStream, ConcurrentLinkedQueue queue) {
        this.inputStream = inputStream;
        this.queue = queue;
    }

    public void run() {
        // where the magic happens
        queue.add(this);

        while (true) {
            try {
                Object line = inputStream.readObject();
                if (line == null) {
                    break;
                }
//                Packet pack = (Packet) line;
                queue.add(line);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPlayer( int player ) { this.player = player; }
    public int getPlayer() { return this.player; }

}
