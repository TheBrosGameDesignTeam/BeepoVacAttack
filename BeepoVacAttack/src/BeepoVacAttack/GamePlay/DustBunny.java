package BeepoVacAttack.GamePlay;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class DustBunny extends Entity {

    private final Vector up = new Vector(0,-3);
    private final Vector left = new Vector(-3,0);
    private final Vector down = new Vector(0,3);
    private final Vector right = new Vector(3,0);
    Vector move;

    public DustBunny(final float x, final float y) {
        super(x,y);
        this.move = new Vector(0,0);
    }

    public void setMove(String move) {

        Vector newMove = new Vector(0,0);

        this.translate(down);

    }

    public void update(final int delta) {


    }

}
