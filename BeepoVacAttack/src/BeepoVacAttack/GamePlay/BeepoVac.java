package BeepoVacAttack.GamePlay;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class BeepoVac extends Entity {

    private final Vector up = new Vector(0,-3);
    private final Vector left = new Vector(-3,0);
    private final Vector down = new Vector(0,3);
    private final Vector right = new Vector(3,0);
    Vector move;

    public BeepoVac(final float x, final float y) {
        super(x,y);
        this.move = new Vector(0,0);
    }

    public void setMove(String move) {

        Vector newMove = new Vector(0,0);

        if (move.contains("w")) {
            newMove = newMove.add(this.up);
        }
        if (move.contains("a")) {
            newMove = newMove.add(this.left);
        }
        if (move.contains("s")) {
            newMove = newMove.add(this.down);
        }
        if (move.contains("d")) {
            newMove = newMove.add(this.right);
        }

//        System.out.println(newMove);
        this.translate(newMove);

    }

    public void update(final int delta) {}

}
