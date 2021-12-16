package BeepoVacAttack.GamePlay;

import jig.Vector;

public class LevelWall extends LevelObject {
    private Float radius = null;
    public Float getRadius() { return radius; }

    private boolean onlyForTall = false;
    public boolean getOnlyForTall() { return onlyForTall; }

    public LevelWall(Vector pos, Vector dimensions, Float radius, boolean onlyForTall) {
        this.setPosition(pos);
        this.setSize(dimensions);
        this.radius = radius;
        this.onlyForTall = onlyForTall;
    }

    public LevelObject copy() {
        return new LevelWall(getPosition(), getSize(), getRadius(), getOnlyForTall());
    }
}
