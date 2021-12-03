package BeepoVacAttack.GamePlay;

import jig.Vector;

public class LevelDustArea extends LevelObject {
    public LevelDustArea(Vector pos, Vector dimensions) {
        this.setPosition(pos);
        this.setSize(dimensions);
    }

    public LevelObject copy() {
        return new LevelDustArea(getPosition(), getSize());
    }
}
