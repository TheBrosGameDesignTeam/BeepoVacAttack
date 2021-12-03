package BeepoVacAttack.GamePlay;

import jig.Vector;

public class LevelWall extends LevelObject {
    private Float radius = null;
    public Float getRadius() { return radius; }

    public LevelWall(Vector pos, Vector dimensions, Float radius) {
        this.setPosition(pos);
        this.setSize(dimensions);
        this.radius = radius;
    }

    public LevelObject copy() {
        return new LevelWall(getPosition(), getSize(), getRadius());
    }
}
