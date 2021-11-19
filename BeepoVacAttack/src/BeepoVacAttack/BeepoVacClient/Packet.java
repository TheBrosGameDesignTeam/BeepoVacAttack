package BeepoVacAttack.BeepoVacClient;

import java.io.Serializable;

public class Packet implements Serializable {

    int player;
    String message;

    float p1X = 0; float p1Y = 0;

    public Packet(String message, int player, float x, float y) {
        this.player = player;
        this.message = message;
        this.p1X = x; this.p1Y = y;
    }

}
