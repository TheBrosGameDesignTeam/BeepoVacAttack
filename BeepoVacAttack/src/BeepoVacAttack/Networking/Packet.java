package BeepoVacAttack.Networking;

import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.GamePlay.DustBunny;
import jig.Vector;

import java.io.Serializable;
import java.util.LinkedList;

public class Packet implements Serializable {

    String message;
    int player = 0;
    int howManyPlayers = 0;
    int howManyEnemies = 0;
    int removeThisBun = 100;

    boolean restart = false;

    public LinkedList<Float> vacPositions = new LinkedList<Float>();
    public LinkedList<Float> enemyPositions = new LinkedList<Float>();
    public LinkedList<Integer> vacTypes = new LinkedList<Integer>();
    public LinkedList<Integer> vacDirections = new LinkedList<Integer>();
    public LinkedList<Boolean> underSomething = new LinkedList<>();

    public Packet(String message) {
        this.message = message;
    }

    public boolean getRestart() { return this.restart; }
    public void setRestart() { this.restart = true; }

    public String getMessage(){ return this.message; }

    public int getPlayer(){ return this.player; }
    public void setPlayer(int player) { this.player = player; }

    public int getHowManyPlayers() { return this.howManyPlayers; }
    public void setHowManyPlayers(int howManyPlayers) { this.howManyPlayers = howManyPlayers; }

    public int getRemoveThisBun() { return this.removeThisBun; }
    public void setRemoveThisBun(int bun) { this.removeThisBun = bun; }

    public void setSnapshot(LinkedList<BeepoVac> players, LinkedList<DustBunny> bunnies) {

        // add all movements to the linked list
        for (BeepoVac beepoVac : players) {
            this.vacPositions.add(beepoVac.getX());
            this.vacPositions.add(beepoVac.getY());
            this.vacTypes.add(beepoVac.getVacType());
            this.vacDirections.add(beepoVac.getVacDirection());
            this.underSomething.add(beepoVac.getUnderSomething());
        }

        // add all movements to the linked list
        for (DustBunny dustBunny : bunnies) {
            this.enemyPositions.add(dustBunny.getX());
            this.enemyPositions.add(dustBunny.getY());
        }

    }

}
