package BeepoVacAttack.GamePlay;

import jig.Entity;
import jig.Vector;

public class MapNode extends Entity {
    private Vector pi;
    private int di;

    public MapNode (float x, float y) {
        super(x, y);
        pi = new Vector(0, 0);
        di = 0;
    }

    public void setPi(Vector pi) { this.pi = pi; }

    public Vector getPi() { return this.pi; }

    public void setDi(int di) { this.di = di; }

    public int getDi() { return this.di; }
}
