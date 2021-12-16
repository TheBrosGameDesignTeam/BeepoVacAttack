package BeepoVacAttack.GamePlay;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class DustBunny extends Entity {

    private final Vector start = new Vector(1,0);
    Vector move;

    private int timer;
    private int time = 500;

    private Vector defaultPosition;

    public DustBunny(final float x, final float y) {
        super(x,y);
        this.defaultPosition = new Vector(x, y);
        this.move = start;
        timer = time;
    }

    public void resetPosition()
    {
        this.setPosition(defaultPosition);
        reset();
    }

    public void reset() {
        timer = time;
    }

    public void setRandomMove() {
        float length = start.length();
        move = Vector.getRandom(length);

        // collision detection to stay within walls
    }

    public void setFollowMove() {
        int ratio = 100;
        // MapNode current = Map.getMap()[(int)this.getX()/ratio][(int)this.getY()/ratio];
        MapNode current = Map.getMap()[5][5];
        move = current.getPi();
    }

    public void update(final int delta, Vector pos) {
        timer -= delta;
        System.out.println("Setting bun direction to: " + pos);

        if (timer <= 0) {
            // this.setFollowMove();
            if (pos != null) move = pos;
            else setRandomMove();
            reset();
        }

        this.translate(move);
    }

}
