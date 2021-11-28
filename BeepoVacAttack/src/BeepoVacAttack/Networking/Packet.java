package BeepoVacAttack.Networking;

import java.io.Serializable;

public class Packet implements Serializable {

    String message;
    int player = 0;

    float p1X = 0; float p1Y = 0;

    public Packet(String message) {
        this.message = message;
//        this.player = player;
    }

    public String getMessage(){ return this.message; }
    public int getPlayer(){ return this.player; }

}
