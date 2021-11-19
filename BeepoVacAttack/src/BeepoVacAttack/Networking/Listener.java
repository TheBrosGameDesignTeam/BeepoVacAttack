package BeepoVacAttack.Networking;
import BeepoVacAttack.BeepoVacServer.MainGame;
import BeepoVacAttack.Networking.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Listener extends Thread {

    private final BufferedReader bufferedReader;
    public static ConcurrentLinkedQueue<Object> queue;
    public int player;

    public Listener(BufferedReader bufferedReader, ConcurrentLinkedQueue queue) {
        this.bufferedReader = bufferedReader;
        this.queue = queue;
        this.player = player;
    }

    public void run() {
        // where the magic happens
        this.queue.add(this);

        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                this.queue.add(new Packet(line, this.player, 0, 0));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
