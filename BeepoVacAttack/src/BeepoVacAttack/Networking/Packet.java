package BeepoVacAttack.Networking;

import jig.Vector;

import java.io.Serializable;

public class Packet implements Serializable {

    String message;
    int player = 0;

    public float p1X = 0; public float p1Y = 0;
    public float p2X = 0; public float p2Y = 0;

    public Packet(String message) {
        this.message = message;
//        this.player = player;
    }

    public String getMessage(){ return this.message; }

    public int getPlayer(){ return this.player; }
    public void setPlayer(int player) { this.player = player; }

    public void setP1(Vector p1Pos) { this.p1X = p1Pos.getX(); this.p1Y = p1Pos.getY(); }
    public void setP2(Vector p2Pos) { this.p2X = p2Pos.getX(); this.p2Y = p2Pos.getY(); }


}
