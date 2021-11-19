package BeepoVacAttack.BeepoVacServer;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class BeepoVac extends Entity {

    Vector move;

    public BeepoVac(final float x, final float y) {
        super(x,y);
        this.move = new Vector(0,0);
    }

    public void setMove(String move) {

        if (move.compareTo("w") == 0) {
            this.translate(new Vector(0,-3));
        } else if (move.compareTo("s") == 0) {
            this.translate(new Vector(0, 3));
        } else if ((move.compareTo("d") == 0)) {
            this.translate(new Vector(3, 0));
        } else if ((move.compareTo("a") == 0)) {
            this.translate(new Vector(-3, 0));
        }

    }

    public void update(final int delta) {}

}
