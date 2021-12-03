package BeepoVacAttack.GamePlay;

import jig.Vector;

public class LevelSurface extends LevelObject {
    private LevelSurfaceType type;
    public LevelSurfaceType getType() { return type; }
    public void setType(LevelSurfaceType type) { this.type = type; }

    public LevelSurface(Vector pos, Vector dimensions, LevelSurfaceType type) {
        this.setPosition(pos);
        this.setSize(dimensions);
        this.setType(type);
    }

    public LevelObject copy() {
        return new LevelSurface(getPosition(), getSize(), getType());
    }
}
