package BeepoVacAttack.Networking;

import BeepoVacAttack.GamePlay.BeepoVac;
import jig.Vector;

import java.io.Serializable;
import java.util.LinkedList;

public class Packet implements Serializable {

    String message;
    int player = 0;
    int howManyPlayers = 0;
    int howManyEnemies = 0;

    public LinkedList<Float> vacPositions = new LinkedList<Float>();
    public LinkedList<Float> enemyPositions = new LinkedList<Float>();

    public Packet(String message) {
        this.message = message;
    }

    public String getMessage(){ return this.message; }

    public int getPlayer(){ return this.player; }
    public void setPlayer(int player) { this.player = player; }

    public int getHowManyPlayers() { return this.howManyPlayers; }
    public void setHowManyPlayers(int howManyPlayers) { this.howManyPlayers = howManyPlayers; }

//    public void setP1(Vector p1Pos) { this.p1X = p1Pos.getX(); this.p1Y = p1Pos.getY(); }
//    public void setP2(Vector p2Pos) { this.p2X = p2Pos.getX(); this.p2Y = p2Pos.getY(); }

    public void setSnapshot(LinkedList<BeepoVac> players) {

        // add all movements to the linked list
        for (BeepoVac beepoVac : players) {
            this.vacPositions.add(beepoVac.getX());
            this.vacPositions.add(beepoVac.getY());
        }


    }

}
