package BeepoVacClient;

import java.io.BufferedReader;
import java.io.IOException;

public class Listener extends Thread {

    private final BufferedReader bufferedReader;
    public int player;

    public Listener(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void run() {
        // where the magic happens

        MainGame.queue.add(this);

        while (true) {

            System.out.println("We are in listener");
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                MainGame.queue.add(new Packet(line, this.player, 0, 0));

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
