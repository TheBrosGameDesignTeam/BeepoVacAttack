package BeepoVacAttack.Networking;
import BeepoVacAttack.BeepoVacServer.MainGame;
import BeepoVacAttack.Networking.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Listener extends Thread {

    private final BufferedReader bufferedReader;
    public static ConcurrentLinkedQueue<Object> queue;
    public int player = 0;

    public Listener(BufferedReader bufferedReader, ConcurrentLinkedQueue queue) {
        this.bufferedReader = bufferedReader;
        this.queue = queue;
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
                this.queue.add(new Packet(line, this.player));

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

    public void setPlayer( int player ) { this.player = player; }
    public int getPlayer() { return this.player; }

}
