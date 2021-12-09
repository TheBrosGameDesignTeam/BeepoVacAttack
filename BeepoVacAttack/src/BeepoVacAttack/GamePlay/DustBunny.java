package BeepoVacAttack.GamePlay;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class DustBunny extends Entity {

    private final Vector start = new Vector(1,0);
    Vector move;

    private int timer;
    private int time = 2000;

    public DustBunny(final float x, final float y) {
        super(x,y);
        this.move = start;
        timer = time;
    }

    public void reset() {
        timer = time;
    }

    public void setMove() {
        float length = start.length();
        move = Vector.getRandom(length);
    }

    public void update(final int delta) {
        timer -= delta;

        if (timer <= 0) {
            this.setMove();
            reset();
        }

        this.translate(move);
    }

}
